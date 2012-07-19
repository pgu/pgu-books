package pgu.client.books;

import pgu.shared.dto.BooksSearch;
import pgu.shared.utils.SortField;

public interface BooksPresenter {

    void goToSearchBooks(BooksSearch booksSearch);

    void updateResultsPerPage(int resultsPerPage);

    void updateSort(SortField sortField, boolean isAscending);

}
