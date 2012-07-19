package pgu.client.app.event;

import pgu.shared.domain.Book;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.google.web.bindery.event.shared.HandlerRegistration;

public class BookEditEvent extends GwtEvent<BookEditEvent.Handler> {

    public interface HasBookEditHandlers extends HasHandlers {
        HandlerRegistration addBookEditHandler(BookEditEvent.Handler handler);
    }

    public interface Handler extends EventHandler {
        void onBookEdit(BookEditEvent event);
    }

    public static final Type<Handler> TYPE = new Type<Handler>();

    private final Book                book;

    public BookEditEvent(final Book book) {
        this.book = book;
    }

    @Override
    public Type<Handler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(final Handler handler) {
        handler.onBookEdit(this);
    }

    public Book getBook() {
        return book;
    }

}
