package pgu.shared.dto;

import java.util.ArrayList;

import pgu.shared.domain.Book;

import com.google.gwt.user.client.rpc.IsSerializable;

public class BooksResult implements IsSerializable {

    private ArrayList<Book> books;
    private int             nextPageDestination;
    private String          nextCursor;

    public ArrayList<Book> getBooks() {
        return books;
    }

    public void setBooks(final ArrayList<Book> books) {
        this.books = books;
    }

    public void setNextPageDestination(final int i) {
        nextPageDestination = i;
    }

    public void setNextCursor(final String nextCursor) {
        this.nextCursor = nextCursor;
    }

    public String getNextCursor() {
        return nextCursor;
    }

    public int getNextDestinationPage() {
        return nextPageDestination;
    }

}
