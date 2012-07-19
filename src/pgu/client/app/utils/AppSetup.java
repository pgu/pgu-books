package pgu.client.app.utils;

import pgu.shared.utils.SortField;

public class AppSetup {

    private boolean   isAscending    = true;
    private SortField sortField      = SortField.TITLE;
    private int       resultsPerPage = 10;

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

}
