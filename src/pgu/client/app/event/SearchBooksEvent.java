package pgu.client.app.event;

import pgu.shared.dto.BooksSearch;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.google.web.bindery.event.shared.HandlerRegistration;

public class SearchBooksEvent extends GwtEvent<SearchBooksEvent.Handler> {

    public interface HasSearchBooksHandlers extends HasHandlers {
        HandlerRegistration addSearchBooksHandler(SearchBooksEvent.Handler handler);
    }

    public interface Handler extends EventHandler {
        void onSearchBooks(SearchBooksEvent event);
    }

    public static final Type<Handler> TYPE = new Type<Handler>();

    private final BooksSearch         booksSearch;

    public SearchBooksEvent(final BooksSearch booksSearch) {
        this.booksSearch = booksSearch;
    }

    @Override
    public Type<Handler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(final Handler handler) {
        handler.onSearchBooks(this);
    }

    public BooksSearch getBooksSearch() {
        return booksSearch;
    }

}
