package pgu.client.books;

import pgu.shared.dto.BooksSearch;

public interface BooksPresenter {

    void goToSearchBooks(BooksSearch booksSearch);

    void updateResultsPerPage(int resultsPerPage);

}
