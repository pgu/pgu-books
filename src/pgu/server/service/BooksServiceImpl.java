package pgu.server.service;

import java.util.ArrayList;

import pgu.client.service.BooksService;
import pgu.server.access.nosql.DocUtils;
import pgu.server.access.nosql.Search;
import pgu.server.app.AppLog;
import pgu.server.domain.nosql.BookDoc;
import pgu.shared.domain.Book;
import pgu.shared.dto.BooksQueryParameters;
import pgu.shared.dto.BooksResult;

import com.google.appengine.api.search.QueryOptions;
import com.google.appengine.api.search.Results;
import com.google.appengine.api.search.ScoredDocument;
import com.google.appengine.api.search.SortExpression;
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

        final SortOptions sortOptions = SortOptions
                .newBuilder()
                .addSortExpression(
                        SortExpression
                        .newBuilder()
                        .setExpression(queryParameters.getSortField().toString().toUpperCase())
                        .setDirection(
                                queryParameters.isAscending() ? SortExpression.SortDirection.DESCENDING
                                        : SortExpression.SortDirection.ASCENDING).setDefaultValue("")).build();

        final com.google.appengine.api.search.Query query = com.google.appengine.api.search.Query.newBuilder()
                .setOptions(QueryOptions.newBuilder() //
                        .setOffset(start) //
                        .setLimit(limit) //
                        .setSortOptions(sortOptions) //
                        .build()).build("");
        final Results<ScoredDocument> results = s.idx().search(query);

        final ArrayList<Book> books = new ArrayList<Book>(limit);
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

        final BooksResult booksResult = new BooksResult();
        booksResult.setBooks(books);
        booksResult.setNbFound(results.getNumberFound());
        return booksResult;
    }

}
