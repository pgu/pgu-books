package pgu.server.service;

import java.util.ArrayList;

import pgu.client.service.BooksService;
import pgu.server.access.nosql.DocUtils;
import pgu.server.access.nosql.FieldValueIndex;
import pgu.server.access.sql.DAO;
import pgu.server.app.AppLog;
import pgu.server.domain.nosql.BookDoc;
import pgu.server.domain.nosql.DocType;
import pgu.server.domain.nosql.FieldValueDoc;
import pgu.server.utils.AppUtils;
import pgu.shared.domain.Book;
import pgu.shared.domain.BooksCount;
import pgu.shared.dto.BooksResult;
import pgu.shared.dto.BooksSearch;
import pgu.shared.dto.Suggestion;
import pgu.shared.dto.SuggestionsResult;
import pgu.shared.utils.SearchField;

import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.google.appengine.api.search.QueryOptions;
import com.google.appengine.api.search.Results;
import com.google.appengine.api.search.ScoredDocument;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.Query;

@SuppressWarnings("serial")
public class BooksServiceImpl extends RemoteServiceServlet implements BooksService {

    private final DAO             dao   = new DAO();
    private final AppLog          log   = new AppLog();
    private final FieldValueIndex fvIdx = new FieldValueIndex();
    private final DocUtils        docU  = new DocUtils();
    private final AppUtils        u     = new AppUtils();

    /**
     * http://code.google.com/p/google-app-engine-samples/source/browse/trunk/search/java/src/com/google/appengine/
     * demos/search/TextSearchServlet.java
     * 
     * https://developers.google.com/appengine/articles/scaling/overview
     * 
     * https://developers.google.com/appengine/docs/java/datastore/queries#Query_Cursors
     */
    @Override
    public BooksResult fetchBooks(final BooksSearch booksSearch, final int page, final String cursor) {

        final Query<Book> booksQuery = dao.ofy().query(Book.class);

        // order
        final String sortDirection = booksSearch.isAscending() ? "" : "-";
        final String sortField = booksSearch.getSortField().toString().toLowerCase();
        final String order = sortDirection + sortField;
        booksQuery.order(order);

        // cursor
        if (!u.isVoid(cursor)) {
            booksQuery.startCursor(Cursor.fromWebSafeString(cursor));
        }

        // limit
        final int searchLength = booksSearch.getLength();
        final int qLength = searchLength + 1;
        booksQuery.limit(qLength);

        // filters
        addFilterText(BookDoc.AUTHOR, booksSearch.getAuthor(), booksQuery);
        addFilterText(BookDoc.CATEGORY, booksSearch.getCategory(), booksQuery);
        addFilterText(BookDoc.COMMENT, booksSearch.getComment(), booksQuery);
        addFilterText(BookDoc.EDITOR, booksSearch.getEditor(), booksQuery);
        addFilterText(BookDoc.TITLE, booksSearch.getTitle(), booksQuery);
        addFilterNum(BookDoc.YEAR, booksSearch.getYear(), booksQuery);

        // fetch results
        log.info(this, "query: [%s]", booksQuery);
        final QueryResultIterator<Book> bookItr = booksQuery.iterator();

        final ArrayList<Book> books = new ArrayList<Book>(searchLength);
        int counter = 0;
        Cursor newCursor = null;
        boolean hasNextPage = false;

        while (bookItr.hasNext()) {
            counter++;

            if (counter <= searchLength) {
                books.add(bookItr.next());
            }

            if (counter == searchLength) {
                newCursor = bookItr.getCursor();

            } else if (counter == qLength) {
                hasNextPage = true;
                break; // weird: itr is broken
            }
        }

        // search result
        final BooksResult booksResult = new BooksResult();
        booksResult.setBooks(books);

        if (hasNextPage) {
            booksResult.setNextPage(page + 1);
            booksResult.setNextCursor(newCursor.toWebSafeString());
        }

        log.info(this, "...fetch " + books.size());

        // TODO PGU Jul 25, 2012 nb found?
        return booksResult;

    }

    private void addFilterNum(final BookDoc field, final String value, final Query<Book> q) {
        if (!u.isVoid(value)) {
            q.filter(field._(), Integer.valueOf(value));
        }
    }

    private void addFilterText(final BookDoc field, final String value, final Query<Book> q) {
        if (!u.isVoid(value)) {
            q.filter(field._(), "-".equals(value) ? "" : value);
        }
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

        final Results<ScoredDocument> results = fvIdx.idx().search(mainQuery);
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

    @Override
    public SuggestionsResult searchSuggestions(final String text) {

        final QueryOptions mainQueryOptions = QueryOptions.newBuilder() //
                .setLimit(1000) //
                .setNumberFoundAccuracy(1001) //
                .setFieldsToReturn(FieldValueDoc.FIELD._(), FieldValueDoc.VALUE._()) //
                .build();

        final String _query = "~\"" + text + "\"";
        log.info(this, "query [%s]", _query);

        final com.google.appengine.api.search.Query mainQuery = com.google.appengine.api.search.Query.newBuilder() //
                .setOptions(mainQueryOptions) //
                .build(_query);

        final Results<ScoredDocument> docs = fvIdx.idx().search(mainQuery);
        final int numberFound = (int) docs.getNumberFound();

        log.info(this, "resultsSize [%s] numberFound [%s]", docs.getResults().size(), numberFound);

        final ArrayList<Suggestion> suggestions = new ArrayList<Suggestion>(docs.getResults().size());

        for (final ScoredDocument doc : docs) {
            final Suggestion suggestion = new Suggestion();
            suggestion.setField(getFieldNameUI(doc));
            suggestion.setValue(docU.text(FieldValueDoc.VALUE._(), doc));
            suggestions.add(suggestion);
        }

        // TODO PGU Jul 23, 2012 specify if more than 1000 suggestions
        final SuggestionsResult suggestionsResult = new SuggestionsResult();
        suggestionsResult.setSuggestions(suggestions);
        return suggestionsResult;
    }

    private String getFieldNameUI(final ScoredDocument doc) {
        final String fieldName = docU.text(FieldValueDoc.FIELD._(), doc);

        if (BookDoc.AUTHOR._().equals(fieldName)) {
            return SearchField.AUTHOR.toString();

        } else if (BookDoc.CATEGORY._().equals(fieldName)) {
            return SearchField.CATEGORY.toString();

        } else if (BookDoc.COMMENT._().equals(fieldName)) {
            return SearchField.COMMENT.toString();

        } else if (BookDoc.EDITOR._().equals(fieldName)) {
            return SearchField.EDITOR.toString();

        } else if (BookDoc.STR_YEAR._().equals(fieldName)) {
            return SearchField.YEAR.toString();

        } else if (BookDoc.TITLE._().equals(fieldName)) {
            return SearchField.TITLE.toString();

        }

        throw new IllegalArgumentException("Unknown field name: " + fieldName);
    }

    @Override
    public BooksCount getBooksCount() {
        return dao.ofy().query(BooksCount.class).filter("isLast", true).get();
    }

}
