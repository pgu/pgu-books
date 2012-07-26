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

    public static final Type<Handler> TYPE = new Type<Handler>();

    @Override
    public Type<Handler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(final Handler handler) {
        handler.onAskForNewSearchBooks(this);
    }

}
