package pgu.server.service;

import java.util.ArrayList;

import pgu.client.service.BooksService;
import pgu.server.access.nosql.DocUtils;
import pgu.server.access.nosql.Search;
import pgu.server.access.sql.DAO;
import pgu.server.app.AppLog;
import pgu.server.domain.nosql.BookDoc;
import pgu.server.domain.nosql.DocType;
import pgu.shared.domain.Book;
import pgu.shared.dto.BooksQueryParameters;
import pgu.shared.dto.BooksResult;

import com.google.appengine.api.datastore.QueryResultIterator;
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

    /**
     * http://code.google.com/p/google-app-engine-samples/source/browse/trunk/search/java/src/com/google/appengine/
     * demos/search/TextSearchServlet.java
     */
    @Override
    public BooksResult fetchBooks(final BooksQueryParameters queryParameters, final int start, final int length) {

        final String searchText = queryParameters.getSearchText();
        if (null == searchText || searchText.trim().isEmpty()) {

            final Query<Book> query = dao.ofy().query(Book.class);
            final String sortDirection = queryParameters.isAscending() ? "" : "-";
            final String sortField = queryParameters.getSortField().toString().toLowerCase();
            query.order(sortDirection + sortField);

            final QueryResultIterator<Book> itr = query.offset(start).limit(length).iterator();

            final ArrayList<Book> books = new ArrayList<Book>(length);
            while (itr.hasNext()) {
                books.add(itr.next());
            }

            final Query<Book> countQuery = dao.ofy().query(Book.class);
            final int numberFound = countQuery.count();

            final BooksResult booksResult = new BooksResult();
            booksResult.setBooks(books);
            booksResult.setNbFound(numberFound);

            return booksResult;

        } else {
            // TODO PGU check that results not > 1000 else throw exception

            final QueryOptions mainQueryOptions = QueryOptions.newBuilder() //
                    .setReturningIdsOnly(true) //
                    .setLimit(1000) // must force the limit, if not, it is set to 20
                    .setNumberFoundAccuracy(1000) //
                    .build();

            final com.google.appengine.api.search.Query mainQuery = com.google.appengine.api.search.Query.newBuilder()
                    .setOptions(mainQueryOptions).build( //
                            // BookDoc.DOC_TYPE._() + ":" + DocType.BOOK._() + " " + //
                            searchText //
                    );

            final Results<ScoredDocument> results = s.idx().search(mainQuery);
            final int numberFound = (int) results.getNumberFound();

            System.out.println("resultsSize " + results.getResults().size());
            System.out.println("numberFound " + numberFound);

            final ArrayList<Long> bookIds = new ArrayList<Long>(numberFound);
            for (final ScoredDocument doc : results) {
                bookIds.add(Long.valueOf(doc.getId()));
            }

            if (bookIds.isEmpty()) {
                final BooksResult booksResult = new BooksResult();
                booksResult.setBooks(new ArrayList<Book>());
                booksResult.setNbFound(0);
                return booksResult;

            } else {
                final Query<Book> query = dao.ofy().query(Book.class);
                final String sortDirection = queryParameters.isAscending() ? "" : "-";
                final String sortField = queryParameters.getSortField().toString().toLowerCase();
                query.order(sortDirection + sortField);
                query.filter("id in", bookIds);

                final QueryResultIterator<Book> itr = query.offset(start).limit(length).iterator();

                final ArrayList<Book> books = new ArrayList<Book>(length);
                while (itr.hasNext()) {
                    books.add(itr.next());
                }

                final BooksResult booksResult = new BooksResult();
                booksResult.setBooks(books);
                booksResult.setNbFound(numberFound);

                return booksResult;
            }

        }

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
        System.out.println("nb found: " + results.getNumberFound());

        final ArrayList<Book> books = new ArrayList<Book>();
        for (final ScoredDocument doc : results) {
            final Book book = new Book() //
                    .id(docU.numLong(BookDoc.BOOK_ID, doc)) //
                    .author(docU.text(BookDoc.AUTHOR, doc)) //
                    .title(docU.text(BookDoc.TITLE, doc)) //
                    .editor(docU.text(BookDoc.EDITOR, doc)) //
                    .year(docU.numInt(BookDoc.YEAR, doc)) //
                    .comment(docU.text(BookDoc.COMMENT, doc)) //
                    .category(docU.text(BookDoc.CATEGORY, doc)) //
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
