package pgu.shared.dto;

import java.util.ArrayList;

import pgu.shared.domain.Book;

import com.google.gwt.user.client.rpc.IsSerializable;

public class BooksResult implements IsSerializable {

    private ArrayList<Book> books;

    public ArrayList<Book> getBooks() {
        return books;
    }

    public void setBooks(final ArrayList<Book> books) {
        this.books = books;
    }

}
