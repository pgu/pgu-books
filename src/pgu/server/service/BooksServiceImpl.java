package pgu.server.service;

import java.util.ArrayList;

import pgu.client.service.BooksService;
import pgu.server.access.nosql.DocUtils;
import pgu.server.access.nosql.Search;
import pgu.server.app.AppLog;
import pgu.server.domain.nosql.BookDoc;
import pgu.shared.dto.Book;
import pgu.shared.dto.BooksQueryParameters;
import pgu.shared.dto.BooksResult;

import com.google.appengine.api.search.QueryOptions;
import com.google.appengine.api.search.Results;
import com.google.appengine.api.search.ScoredDocument;
import com.google.appengine.api.search.SortExpression;
import com.google.appengine.api.search.SortExpression.SortDirection;
import com.google.appengine.api.search.SortOptions;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class BooksServiceImpl extends RemoteServiceServlet implements BooksService {

    private final AppLog   log  = new AppLog();
    private final Search   s    = new Search();
    private final DocUtils docU = new DocUtils();

    /**
     * http://code.google.com/p/google-app-engine-samples/source/browse/trunk/search/java/src/com/google/appengine/
     * demos/search/TextSearchServlet.java
     */
    @Override
    public BooksResult fetchBooks(final BooksQueryParameters queryParameters, final int start, final int length) {

        final int limit = length;

        final String expression = queryParameters.getSortField().toString().toUpperCase();

        final SortDirection mainSortDirection = queryParameters.isAscending() ? SortExpression.SortDirection.ASCENDING
                : SortExpression.SortDirection.DESCENDING;

        final SortExpression.Builder mainSortExp = SortExpression.newBuilder() //
                .setExpression(expression) //
                .setDirection(mainSortDirection) //
                .setDefaultValue("");

        final SortOptions mainSortOptions = SortOptions.newBuilder() //
                .addSortExpression(mainSortExp) //
                .build();

        final QueryOptions mainQueryOptions = QueryOptions.newBuilder() //
                // .setOffset(start) //
                // .setLimit(limit) //
                .setSortOptions(mainSortOptions) //
                .build();

        final com.google.appengine.api.search.Query mainQuery = com.google.appengine.api.search.Query.newBuilder()
                .setOptions(mainQueryOptions).build("");

        final Results<ScoredDocument> results = s.idx().search(mainQuery);

        // TODO PGU
        final ArrayList<Book> books = new ArrayList<Book>(limit);
        int idx = 0;
        final int stop = start + length;
        for (final ScoredDocument doc : results) {
            if (idx < start) {
                idx++;
                continue;
            }

            if (idx >= stop) {
                break;
            }

            idx++;

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

        final QueryOptions countQOptions = QueryOptions.newBuilder() //
                .setReturningIdsOnly(true) //
                .setLimit(1) //
                .setNumberFoundAccuracy(10000) //
                .build();

        final com.google.appengine.api.search.Query countQuery = com.google.appengine.api.search.Query.newBuilder()
                .setOptions(countQOptions).build("");
        final Results<ScoredDocument> countResults = s.idx().search(countQuery);

        final BooksResult booksResult = new BooksResult();
        booksResult.setBooks(books);
        booksResult.setNbFound(countResults.getNumberFound());
        return booksResult;
    }

}
