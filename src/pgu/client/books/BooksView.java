package pgu.client.books;

import java.util.HashSet;

import pgu.client.app.utils.HasClickAndVisibility;
import pgu.shared.domain.Book;
import pgu.shared.dto.BooksResult;

import com.google.gwt.user.client.ui.IsWidget;

public interface BooksView extends IsWidget {

    void setPresenter(BooksPresenter presenter);

    void setBooks(BooksResult booksResult, boolean isEditable);

    void clear();

    HasClickAndVisibility getDeleteBooksWidget();

    HasClickAndVisibility getEditBookWidget();

    HasClickAndVisibility getCreateBookWidget();

    Book getSelectedBook();

    void clearHandlers();

    HashSet<Book> getSelectedBooks();

    HasClickAndVisibility getRefreshBooksWidget();
}
