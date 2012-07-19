package pgu.client.books;

import pgu.client.app.utils.HasClickAndVisibility;
import pgu.shared.domain.Book;
import pgu.shared.dto.BooksResult;

import com.google.gwt.user.client.ui.HasVisibility;
import com.google.gwt.user.client.ui.IsWidget;

public interface BooksView extends IsWidget {

    void setPresenter(BooksPresenter presenter);

    void setBooks(BooksResult booksResult, boolean isEditable);

    void clear();

    HasVisibility getDeleteBooksWidget();

    HasClickAndVisibility getEditionBookWidget();

    HasClickAndVisibility getNewBookWidget();

    Book getSelectedBook();

    void clearHandlers();
}
