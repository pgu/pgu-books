package pgu.client.app.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.google.web.bindery.event.shared.HandlerRegistration;

public class SetupEvent extends GwtEvent<SetupEvent.Handler> {

    public interface HasSetupHandlers extends HasHandlers {
        HandlerRegistration addSetupHandler(SetupEvent.Handler handler);
    }

    public interface Handler extends EventHandler {
        void onSetup(SetupEvent event);
    }

    public static final Type<Handler> TYPE = new Type<Handler>();

    @Override
    public Type<Handler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(final Handler handler) {
        handler.onSetup(this);
    }

}
