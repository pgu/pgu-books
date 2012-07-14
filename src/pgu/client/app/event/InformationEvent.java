package pgu.client.app.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.google.web.bindery.event.shared.HandlerRegistration;

public class InformationEvent extends GwtEvent<InformationEvent.Handler> {

    public interface HasInformationHandlers extends HasHandlers {
        HandlerRegistration addInformationHandler(InformationEvent.Handler handler);
    }

    public interface Handler extends EventHandler {
        void onInformation(InformationEvent event);
    }

    public static final Type<Handler> TYPE = new Type<Handler>();

    private final String              message;

    public InformationEvent(final String message) {
        this.message = message;
    }

    @Override
    public Type<Handler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(final Handler handler) {
        handler.onInformation(this);
    }

    public String getMessage() {
        return message;
    }

}
