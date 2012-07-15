package pgu.client.app.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.google.web.bindery.event.shared.HandlerRegistration;

public class TechnicalErrorEvent extends GwtEvent<TechnicalErrorEvent.Handler> {

    public interface HasTechnicalErrorHandlers extends HasHandlers {
        HandlerRegistration addTechnicalErrorHandler(TechnicalErrorEvent.Handler handler);
    }

    public interface Handler extends EventHandler {
        void onTechnicalError(TechnicalErrorEvent event);
    }

    public static final Type<Handler> TYPE = new Type<Handler>();

    private final Throwable           throwable;

    public TechnicalErrorEvent(final Throwable caught) {
        throwable = caught;
    }

    @Override
    public Type<Handler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(final Handler handler) {
        handler.onTechnicalError(this);
    }

    public Throwable getThrowable() {
        return throwable;
    }

}
