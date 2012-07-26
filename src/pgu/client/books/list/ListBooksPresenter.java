package pgu.client.books.list;

import pgu.shared.utils.SortField;

public interface ListBooksPresenter {

    void updateResultsPerPage(int resultsPerPage);

    void updateSort(SortField sortField, boolean isAscending);

}
