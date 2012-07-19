package pgu.client.app.utils;

import pgu.client.app.mvp.ClientFactory;
import pgu.shared.dto.BooksSearch;

public class SearchUtils {

    private final ClientFactory clientFactory;

    public SearchUtils(final ClientFactory clientFactory) {
        this.clientFactory = clientFactory;
    }

    public BooksSearch updateSearch(final BooksSearch booksSearch) {
        if (booksSearch == null) {
            return null;
        }

        final AppSetup appSetup = clientFactory.getAppSetup();

        booksSearch.setLength(appSetup.getResultsPerPage());
        booksSearch.setAscending(appSetup.isAscending());
        booksSearch.setSortField(appSetup.getSortField());

        return booksSearch;

    }

    public BooksSearch newBooksSearch() {
        return updateSearch(new BooksSearch());
    }

}
