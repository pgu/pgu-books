package pgu.client.books.list;

import java.util.ArrayList;
import java.util.HashSet;

import pgu.client.app.utils.HasClickAndEnable;
import pgu.client.app.utils.HasClickAndVisibility;
import pgu.shared.domain.Book;
import pgu.shared.utils.SortField;

import com.google.gwt.user.client.ui.IsWidget;

public interface ListBooksView extends IsWidget {

    void setPresenter(ListBooksPresenter presenter);

    HasClickAndVisibility getDeleteBooksWidget();

    HasClickAndVisibility getEditBookWidget();

    HasClickAndVisibility getCreateBookWidget();

    HasClickAndVisibility getRefreshBooksWidget();

    HasClickAndVisibility getSearchPriceBooksWidget();

    Book getSelectedBook();

    HashSet<Book> getSelectedBooks();

    HasClickAndEnable getPreviousPageWidget();

    HasClickAndEnable getNextPageWidget();

    void setResultsPerPage(int length);

    void setCurrentSort(SortField sortField, boolean isAscending);

    void setBooks(ArrayList<Book> books, boolean isEditable);

    void updatePager(boolean isFirstPage, boolean hasNextPage, int nbBooks);

}
