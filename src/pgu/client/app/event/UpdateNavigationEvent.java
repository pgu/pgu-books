package pgu.client.app.event;

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

    @Override
    public Type<Handler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(final Handler handler) {
        handler.onUpdateNavigation(this);
    }

    public void setNextPage(final int nextPage) {
        // TODO PGU
    }

    public void setNextCursor(final String nextCursor) {
        // TODO PGU
    }

}
