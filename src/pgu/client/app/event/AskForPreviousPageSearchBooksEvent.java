package pgu.client.app.event;

import pgu.client.app.utils.HasClickAndEnable;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.google.web.bindery.event.shared.HandlerRegistration;

public class AskForPreviousPageSearchBooksEvent extends GwtEvent<AskForPreviousPageSearchBooksEvent.Handler> {

    public interface HasAskForPreviousSearchBooksHandlers extends HasHandlers {
        HandlerRegistration addAskForPreviousSearchBooksHandler(AskForPreviousPageSearchBooksEvent.Handler handler);
    }

    public interface Handler extends EventHandler {
        void onAskForPreviousSearchBooks(AskForPreviousPageSearchBooksEvent event);
    }

    public static final Type<Handler> TYPE = new Type<Handler>();

    private final HasClickAndEnable   widget;

    public AskForPreviousPageSearchBooksEvent(final HasClickAndEnable widget) {
        this.widget = widget;
        widget.setEnabled(false);
    }

    @Override
    public Type<Handler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(final Handler handler) {
        widget.setEnabled(true);
        handler.onAskForPreviousSearchBooks(this);
    }

}
