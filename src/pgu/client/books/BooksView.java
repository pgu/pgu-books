package pgu.client.books;

import pgu.shared.dto.BooksResult;

import com.google.gwt.user.client.ui.HasVisibility;
import com.google.gwt.user.client.ui.IsWidget;

public interface BooksView extends IsWidget {

    void setPresenter(BooksPresenter presenter);

    void setBooks(BooksResult booksResult);

    void clear();

    HasVisibility getDeleteBooksWidget();

    HasVisibility getEditionBookWidget();

    HasVisibility getNewBookWidget();
}
