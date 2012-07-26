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

    void clear();

    HasClickAndVisibility getDeleteBooksWidget();

    HasClickAndVisibility getEditBookWidget();

    HasClickAndVisibility getCreateBookWidget();

    Book getSelectedBook();

    HashSet<Book> getSelectedBooks();

    void setBooks(ArrayList<Book> books, int resultsPerPage, SortField sortField, boolean isAscending,
            boolean isFirstPage, boolean hasNextPage, boolean isEditable);

    HasClickHandlers getPreviousPageWidget();

    HasClickHandlers getNextPageWidget();
}
