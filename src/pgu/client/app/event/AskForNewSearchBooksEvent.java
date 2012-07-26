package pgu.client.app.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.google.web.bindery.event.shared.HandlerRegistration;

public class AskForNewSearchBooksEvent extends GwtEvent<AskForNewSearchBooksEvent.Handler> {

    public interface HasAskForNewSearchBooksHandlers extends HasHandlers {
        HandlerRegistration addAskForNewSearchBooksHandler(AskForNewSearchBooksEvent.Handler handler);
    }

    public interface Handler extends EventHandler {
        void onAskForNewSearchBooks(AskForNewSearchBooksEvent event);
    }

    public static final Type<Handler> TYPE           = new Type<Handler>();

    private int                       page           = 0;
    private String                    searchHashcode = null;

    public AskForNewSearchBooksEvent(final int page, final String searchHashcode) {
        this.page = page;
        this.searchHashcode = searchHashcode;
    }

    @Override
    public Type<Handler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(final Handler handler) {
        handler.onAskForNewSearchBooks(this);
    }

    public int getPage() {
        return page;
    }

    public String getSearchHashcode() {
        return searchHashcode;
    }

}
