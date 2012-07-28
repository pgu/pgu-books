package pgu.client.books.list;

import java.util.ArrayList;
import java.util.HashSet;

import pgu.client.app.utils.HasClickAndVisibility;
import pgu.shared.domain.Book;
import pgu.shared.utils.SortField;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.IsWidget;

public interface ListBooksView extends IsWidget {

    void setPresenter(ListBooksPresenter presenter);

    HasClickAndVisibility getDeleteBooksWidget();

    HasClickAndVisibility getEditBookWidget();

    HasClickAndVisibility getCreateBookWidget();

    HasClickAndVisibility getRefreshBooksWidget();

    Book getSelectedBook();

    HashSet<Book> getSelectedBooks();

    HasClickHandlers getPreviousPageWidget();

    HasClickHandlers getNextPageWidget();

    void setResultsPerPage(int length);

    void setCurrentSort(SortField sortField, boolean isAscending);

    void isFirstPage(boolean b);

    void hasNextPage(boolean b);

    void isEditable(boolean isEditable);

    void setBooks(ArrayList<Book> books);

}
