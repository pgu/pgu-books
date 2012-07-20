package pgu.client.app.event;

import java.util.HashSet;

import pgu.shared.domain.Book;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.google.web.bindery.event.shared.HandlerRegistration;

public class DeleteBooksEvent extends GwtEvent<DeleteBooksEvent.Handler> {

    public interface HasDeleteBooksHandlers extends HasHandlers {
        HandlerRegistration addDeleteBooksHandler(DeleteBooksEvent.Handler handler);
    }

    public interface Handler extends EventHandler {
        void onDeleteBooks(DeleteBooksEvent event);
    }

    public static final Type<Handler> TYPE  = new Type<Handler>();

    private final HashSet<Book>       books = new HashSet<Book>();

    public DeleteBooksEvent(final HashSet<Book> books) {
        this.books.clear();

        if (books != null) {
            this.books.addAll(books);
        }
    }

    @Override
    public Type<Handler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(final Handler handler) {
        handler.onDeleteBooks(this);
    }

    public HashSet<Book> getBooks() {
        return books;
    }

}
