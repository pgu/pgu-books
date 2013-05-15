package pgu.client.books.list;

import java.util.HashSet;

import pgu.shared.domain.Book;
import pgu.shared.utils.SortField;

public interface ListBooksPresenter {

    void updateResultsPerPage(int resultsPerPage);

    void updateSort(SortField sortField, boolean isAscending);

    void deleteDuplicates(HashSet<Book> duplicates);

}
