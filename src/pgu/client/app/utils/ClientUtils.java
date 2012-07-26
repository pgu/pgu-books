package pgu.client.app.utils;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.Command;

public class ClientUtils {

    public boolean isVoid(final String str) {
        return null == str || str.trim().isEmpty();
    }

    public void fire(final com.google.gwt.event.shared.EventBus eventBus, final GwtEvent event) {
        Scheduler.get().scheduleDeferred(new Command() {
            @Override
            public void execute() {
                eventBus.fireEvent(event);
            }
        });
    }

    public void fire(final com.google.web.bindery.event.shared.EventBus eventBus, final GwtEvent event) {
        Scheduler.get().scheduleDeferred(new Command() {
            @Override
            public void execute() {
                eventBus.fireEvent(event);
            }
        });
    }

}
