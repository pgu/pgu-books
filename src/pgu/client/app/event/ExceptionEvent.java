package pgu.client.app.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.google.web.bindery.event.shared.HandlerRegistration;

public class ExceptionEvent extends GwtEvent<ExceptionEvent.Handler> {

    public interface HasExceptionHandlers extends HasHandlers {
        HandlerRegistration addExceptionHandler(ExceptionEvent.Handler handler);
    }

    public interface Handler extends EventHandler {
        void onException(ExceptionEvent event);
    }

    public static final Type<Handler> TYPE = new Type<Handler>();

    private final Throwable           throwable;

    public ExceptionEvent(final Throwable caught) {
        throwable = caught;
    }

    @Override
    public Type<Handler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(final Handler handler) {
        handler.onException(this);
    }

    public Throwable getThrowable() {
        return throwable;
    }

}
