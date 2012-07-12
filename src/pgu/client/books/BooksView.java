package pgu.client.books;

import pgu.shared.dto.BooksResult;

import com.google.gwt.user.client.ui.IsWidget;

public interface BooksView extends IsWidget {

    void setPresenter(BooksPresenter presenter);

    void setBooks(BooksResult booksResult);

}
