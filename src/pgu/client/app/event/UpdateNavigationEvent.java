package pgu.client.app.event;

import pgu.shared.dto.BooksSearch;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.google.web.bindery.event.shared.HandlerRegistration;

public class UpdateNavigationEvent extends GwtEvent<UpdateNavigationEvent.Handler> {

    public interface HasUpdateNavigationHandlers extends HasHandlers {
        HandlerRegistration addUpdateNavigationHandler(UpdateNavigationEvent.Handler handler);
    }

    public interface Handler extends EventHandler {
        void onUpdateNavigation(UpdateNavigationEvent event);
    }

    public static final Type<Handler> TYPE = new Type<Handler>();

    private final BooksSearch         search;
    private final int                 nextPage;
    private final String              nextCursor;

    public UpdateNavigationEvent(final BooksSearch search, final int nextPage, final String nextCursor) {
        this.search = search;
        this.nextPage = nextPage;
        this.nextCursor = nextCursor;
    }

    @Override
    public Type<Handler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(final Handler handler) {
        handler.onUpdateNavigation(this);
    }

    public int getNextPage() {
        return nextPage;
    }

    public String getNextCursor() {
        return nextCursor;
    }

    public BooksSearch getSearch() {
        return search;
    }

}
