package pgu.server.service;

import java.util.ArrayList;
import java.util.List;

import pgu.client.service.BooksService;
import pgu.server.access.nosql.DocUtils;
import pgu.server.access.nosql.Search;
import pgu.server.access.sql.DAO;
import pgu.server.app.AppLog;
import pgu.server.domain.nosql.BookDoc;
import pgu.server.domain.nosql.DocType;
import pgu.server.utils.AppUtils;
import pgu.shared.domain.Book;
import pgu.shared.dto.BooksResult;
import pgu.shared.dto.BooksSearch;

import com.google.appengine.api.search.QueryOptions;
import com.google.appengine.api.search.Results;
import com.google.appengine.api.search.ScoredDocument;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.Query;

@SuppressWarnings("serial")
public class BooksServiceImpl extends RemoteServiceServlet implements BooksService {

    private final DAO      dao  = new DAO();
    private final AppLog   log  = new AppLog();
    private final Search   s    = new Search();
    private final DocUtils docU = new DocUtils();
    private final AppUtils u    = new AppUtils();

    /**
     * http://code.google.com/p/google-app-engine-samples/source/browse/trunk/search/java/src/com/google/appengine/
     * demos/search/TextSearchServlet.java
     * 
     * https://developers.google.com/appengine/articles/scaling/overview
     * 
     * https://developers.google.com/appengine/docs/java/datastore/queries#Query_Cursors
     * 
     * <pre>
     * // first page: first X books with a cursor to next
     * 
     * // search doc -> suggestions -> in a htmlpanel above the search fields 
     * // -> selection of suggestions (one by fieldtype)
     * // -> according to selections, fields are filled -> search exact on fields
     * // => cursor available
     * 
     * // next/previous links only in the pager
     * 
     * // case-insensitive => IN => no cursor available
     * 
     * insert a book: for each field/value, is there a doc
     * si je cree un doc for each f/v of the book, je vais avoir des doublons et je n'en veux pas
     * pour avoir des docs avec des valeurs uniques, si je cree des entites sql f/v
     * je check sur ces entites si deja creer
     * si l'entite n'a plus de book associes, supprimer le doc qui le reflete. 
     * donc le doc a l'id du record sql
     * 
     * 
     * 
     * author: 
     * Pierre* -> 
     * .filter("value >=", pierre) //
     * .filter("value <", pierre + "\uFFFD") //
     * .filter("value >=", Pierre) //
     * .filter("value <", Pierre + "\uFFFD") //
     * 
     * author: 
     * Pierre -> 
     * .filter("value =", pierre) //
     * .filter("value =", Pierre) //
     * 
     * author: 
     * ~Pierre -> 
     * doc search
     * => in => no cursor
     * </pre>
     */
    @Override
    public BooksResult fetchBooks(final BooksSearch booksSearch) {

        final int start = booksSearch.getStart();
        final int length = booksSearch.getLength();
        final String sortDirection = booksSearch.isAscending() ? "" : "-";
        final String sortField = booksSearch.getSortField().toString().toLowerCase();
        final String order = sortDirection + sortField;

        final Query<Book> booksQuery = dao.ofy().query(Book.class);
        booksQuery.order(order);
        booksQuery.offset(start);
        booksQuery.limit(length);

        final String author = booksSearch.getAuthor();
        final String category = booksSearch.getCategory();
        final String comment = booksSearch.getComment();
        final String editor = booksSearch.getEditor();
        final String searchText = booksSearch.getSearchText();
        final String title = booksSearch.getTitle();
        final String year = booksSearch.getYear();

        int numberFound = 0;

        if (u.isVoid(author) //
                && u.isVoid(category) //
                && u.isVoid(comment) //
                && u.isVoid(editor) //
                && u.isVoid(searchText) //
                && u.isVoid(title) //
                && u.isVoid(year) //
        ) {

            numberFound = dao.ofy().query(Book.class).count();

        } else {

            // query is either on any text or on fields
            final StringBuilder sb = new StringBuilder();
            if (!u.isVoid(searchText)) {
                sb.append("~\"" + searchText + "\"");

            } else {
                appendFieldText(BookDoc.DOC_TYPE, DocType.BOOK._(), sb);
                appendFieldText(BookDoc.AUTHOR, author, sb);
                appendFieldText(BookDoc.CATEGORY, category, sb);
                appendFieldText(BookDoc.COMMENT, comment, sb);
                appendFieldText(BookDoc.EDITOR, editor, sb);
                appendFieldText(BookDoc.TITLE, title, sb);
                appendFieldInt(BookDoc.YEAR, year, sb);
            }

            final QueryOptions mainQueryOptions = QueryOptions.newBuilder() //
                    .setReturningIdsOnly(true) //
                    .setLimit(1000) // forces the limit because: default=20, max=1000
                    .setNumberFoundAccuracy(1001) //
                    .build();

            final String _query = sb.toString();
            log.info(this, "query [%s]", _query);

            final com.google.appengine.api.search.Query mainQuery = com.google.appengine.api.search.Query.newBuilder() //
                    .setOptions(mainQueryOptions) //
                    .build(_query);

            final Results<ScoredDocument> docs = s.idx().search(mainQuery);
            numberFound = (int) docs.getNumberFound();

            log.info(this, "resultsSize [%s] numberFound [%s]", docs.getResults().size(), numberFound);

            if (numberFound > 1000) {
                final BooksResult booksResult = new BooksResult();
                booksResult.setBooks(new ArrayList<Book>());
                booksResult.setNbFound(numberFound);
                return booksResult;
            }

            if (numberFound == 0) {
                final BooksResult booksResult = new BooksResult();
                booksResult.setBooks(new ArrayList<Book>());
                booksResult.setNbFound(0);
                return booksResult;
            }

            final ArrayList<Long> bookIds = new ArrayList<Long>(numberFound);
            for (final ScoredDocument doc : docs) {
                bookIds.add(Long.valueOf(doc.getId()));
            }

            booksQuery.filter("id in", bookIds);
        }

        final List<Book> _books = booksQuery.list();

        final ArrayList<Book> books;
        if (_books instanceof ArrayList) {
            books = (ArrayList<Book>) _books;

        } else {
            books = new ArrayList<Book>(length);
            books.addAll(_books);
        }

        final BooksResult booksResult = new BooksResult();
        booksResult.setBooks(books);
        booksResult.setNbFound(numberFound);

        return booksResult;

    }

    private void appendFieldText(final BookDoc bookDoc, final String fieldValue, final StringBuilder sb) {
        if (!u.isVoid(fieldValue)) {

            final String[] values = fieldValue.split(" OR ");

            final StringBuilder _sb = new StringBuilder();
            for (final String value : values) {

                if (!u.isVoid(value)) {

                    if (_sb.length() > 0) {
                        _sb.append(" OR ");
                    }
                    _sb.append(bookDoc.toString().toUpperCase());
                    _sb.append(":");
                    _sb.append("\"" + value + "\"");
                }
            }

            if (sb.length() > 0) {
                sb.append(" AND ");
            }
            sb.append("(");
            sb.append(_sb.toString());
            sb.append(")");

        }
    }

    private void appendFieldInt(final BookDoc bookDoc, final String fieldValue, final StringBuilder sb) {
        if (!u.isVoid(fieldValue)) {

            final String[] values = fieldValue.split(" OR "); // 1980 OR 1981

            final StringBuilder _sb = new StringBuilder();
            for (final String value : values) {

                if (!u.isVoid(value)) {

                    if (_sb.length() > 0) {
                        _sb.append(" OR ");
                    }

                    if (value.contains(" AND ")) { // >1980 AND <2010
                        final String[] parts = value.split(" AND ");
                        _sb.append("(");
                        for (int i = 0; i < parts.length; i++) {
                            if (i > 0) {
                                _sb.append(" AND ");
                            }
                            addFilterInt(bookDoc, _sb, parts[i]);
                        }
                        _sb.append(")");

                    } else {
                        addFilterInt(bookDoc, _sb, value);
                    }
                }
            }

            if (sb.length() > 0) {
                sb.append(" AND ");
            }
            sb.append("(");
            sb.append(_sb.toString());
            sb.append(")");
        }
    }

    private void addFilterInt(final BookDoc bookDoc, final StringBuilder _sb, final String value) {
        String operator = "=";
        String _value = value;

        if (value.contains("<")) {
            operator = "<";
            _value = value.replaceAll(operator, "").trim();

        } else if (value.contains("<=")) {
            operator = "<=";
            _value = value.replaceAll(operator, "").trim();

        } else if (value.contains(">")) {
            operator = ">";
            _value = value.replaceAll(operator, "").trim();

        } else if (value.contains(">=")) {
            operator = ">=";
            _value = value.replaceAll(operator, "").trim();
        }

        _sb.append(bookDoc.toString().toUpperCase());
        _sb.append(" ");
        _sb.append(operator);
        _sb.append(" ");
        _sb.append(_value);
    }

    private BooksResult backup() {
        // final String expression = queryParameters.getSortField().toString().toUpperCase();

        // final SortDirection mainSortDirection = queryParameters.isAscending() ?
        // SortExpression.SortDirection.ASCENDING
        // : SortExpression.SortDirection.DESCENDING;

        // final SortExpression.Builder mainSortExp = SortExpression.newBuilder() //
        // .setExpression(expression) //
        // .setDirection(mainSortDirection) //
        // .setDefaultValue("");

        // final SortOptions mainSortOptions = SortOptions.newBuilder() //
        // .addSortExpression(mainSortExp) //
        // .build();

        final QueryOptions mainQueryOptions = QueryOptions.newBuilder() //
                .setReturningIdsOnly(true) //
                .setLimit(1000) // must force the limit, if not, it is set to 20
                .setNumberFoundAccuracy(1000) //
                // .setOffset(start) //
                // .setSortOptions(mainSortOptions) //
                .build();

        final com.google.appengine.api.search.Query mainQuery = com.google.appengine.api.search.Query.newBuilder()
                .setOptions(mainQueryOptions).build(BookDoc.DOC_TYPE._() + ":" + DocType.BOOK._());

        final Results<ScoredDocument> results = s.idx().search(mainQuery);
        log.info(this, "nb found: %s", results.getNumberFound());

        final ArrayList<Book> books = new ArrayList<Book>();
        for (final ScoredDocument doc : results) {
            final Book book = new Book() //
                    .id(docU.numLong(BookDoc.BOOK_ID._(), doc)) //
                    .author(docU.text(BookDoc.AUTHOR._(), doc)) //
                    .title(docU.text(BookDoc.TITLE._(), doc)) //
                    .editor(docU.text(BookDoc.EDITOR._(), doc)) //
                    .year(docU.numInt(BookDoc.YEAR._(), doc)) //
                    .comment(docU.text(BookDoc.COMMENT._(), doc)) //
                    .category(docU.text(BookDoc.CATEGORY._(), doc)) //
            ;
            books.add(book);
        }

        // final QueryOptions countQOptions = QueryOptions.newBuilder() //
        // .setReturningIdsOnly(true) //
        // .setLimit(1) //
        // .setNumberFoundAccuracy(1000) //
        // .build();

        // final com.google.appengine.api.search.Query countQuery = com.google.appengine.api.search.Query.newBuilder()
        // .setOptions(countQOptions).build(BookDoc.DOC_TYPE._() + ":" + DocType.BOOK._());
        // final Results<ScoredDocument> countResults = s.idx().search(countQuery);

        final BooksResult booksResult = new BooksResult();
        booksResult.setBooks(books);
        // booksResult.setNbFound(countResults.getNumberFound());

        return booksResult;
    }
}
