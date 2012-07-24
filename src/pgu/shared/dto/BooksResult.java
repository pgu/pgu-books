package pgu.shared.dto;

import java.util.ArrayList;

import pgu.shared.domain.Book;

import com.google.gwt.user.client.rpc.IsSerializable;

public class BooksResult implements IsSerializable {

    private ArrayList<Book> books;
    private BooksSearch     booksSearch;
    private String          newCursor;

    public ArrayList<Book> getBooks() {
        return books;
    }

    public void setBooks(final ArrayList<Book> books) {
        this.books = books;
    }

    public BooksSearch getBooksSearch() {
        return booksSearch;
    }

    public void setBooksSearch(final BooksSearch booksSearch) {
        this.booksSearch = booksSearch;
    }

    public void setNewCursor(final String newCursor) {
        this.newCursor = newCursor;
    }

    public String getNewCursor() {
        return newCursor;
    }

}
