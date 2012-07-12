package pgu.shared.dto;

import java.util.ArrayList;

import pgu.shared.domain.Book;

import com.google.gwt.user.client.rpc.IsSerializable;

public class BooksResult implements IsSerializable {

    private ArrayList<Book> books;
    private long            nbFound;
    private int             start;
    private int             length;

    public ArrayList<Book> getBooks() {
        return books;
    }

    public void setBooks(final ArrayList<Book> books) {
        this.books = books;
    }

    public long getNbFound() {
        return nbFound;
    }

    public void setNbFound(final long nbFound) {
        this.nbFound = nbFound;
    }

    public int getStart() {
        return start;
    }

    public void setStart(final int start) {
        this.start = start;
    }

    public int getLength() {
        return length;
    }

    public void setLength(final int length) {
        this.length = length;
    }

}
