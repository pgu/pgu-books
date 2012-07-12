package pgu.client.books;

import java.util.ArrayList;

import pgu.shared.domain.Book;

import com.google.gwt.user.client.ui.IsWidget;

public interface BooksView extends IsWidget {

    void setPresenter(BooksPresenter presenter);

    void setBooks(ArrayList<Book> books);

}
