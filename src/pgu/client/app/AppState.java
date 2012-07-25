package pgu.client.app;

import java.util.HashMap;
import java.util.HashSet;

import pgu.client.app.utils.SearchNavigation;
import pgu.shared.dto.BooksSearch;
import pgu.shared.utils.SortField;

public class AppState {

    private boolean                                               isAscending       = true;
    private SortField                                             sortField         = SortField.TITLE;
    private int                                                   resultsPerPage    = 10;

    private final int                                             destinationPage   = 0;
    private final BooksSearch                                     search;

    private final HashMap<BooksSearch, HashSet<SearchNavigation>> search2navigation = new HashMap<BooksSearch, HashSet<SearchNavigation>>();
    private final HashSet<BooksSearch>                            searches          = new HashSet<BooksSearch>();

    public AppState() {
        search = new BooksSearch();
        searches.add(search);
    }

    public void setResultsPerPage(final int resultsPerPage) {
        this.resultsPerPage = resultsPerPage;
    }

    public int getResultsPerPage() {
        return resultsPerPage;
    }

    public boolean isAscending() {
        return isAscending;
    }

    public void setAscending(final boolean isAscending) {
        this.isAscending = isAscending;
    }

    public SortField getSortField() {
        return sortField;
    }

    public void setSortField(final SortField sortField) {
        this.sortField = sortField;
    }

    public int getDestinationPage() {
        return destinationPage;
    }

    public BooksSearch getSearch() {
        return search.copy();
    }

    public HashMap<BooksSearch, HashSet<SearchNavigation>> getSearch2navigation() {
        return search2navigation;
    }

    public HashSet<BooksSearch> getSearches() {
        return searches;
    }

}
