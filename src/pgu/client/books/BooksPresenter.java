package pgu.client.books;

import pgu.shared.utils.SortField;

public interface BooksPresenter {

    void updateResultsPerPage(int resultsPerPage);

    void updateSort(SortField sortField, boolean isAscending);

}
