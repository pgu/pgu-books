package pgu.client.app.event;

import pgu.client.app.utils.Level;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.google.web.bindery.event.shared.HandlerRegistration;

public class NotificationEvent extends GwtEvent<NotificationEvent.Handler> {

    public interface HasNotificationHandlers extends HasHandlers {
        HandlerRegistration addNotificationHandler(NotificationEvent.Handler handler);
    }

    public interface Handler extends EventHandler {
        void onNotification(NotificationEvent event);
    }

    public static final Type<Handler> TYPE = new Type<Handler>();

    private final String              message;
    private final Level               level;

    public NotificationEvent(final Level level, final String message) {
        this.message = message;
        this.level = level;
    }

    @Override
    public Type<Handler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(final Handler handler) {
        handler.onNotification(this);
    }

    public String getMessage() {
        return message;
    }

    public Level getLevel() {
        return level;
    }

}
