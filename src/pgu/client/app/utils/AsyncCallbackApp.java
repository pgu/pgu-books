package pgu.client.app.utils;

import pgu.client.app.event.HideWaitingIndicatorEvent;
import pgu.client.app.event.TechnicalErrorEvent;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.event.shared.EventBus;

public abstract class AsyncCallbackApp<T> implements AsyncCallback<T> {

    private final EventBus    eventBus;
    private final ClientUtils u = new ClientUtils();

    public AsyncCallbackApp(final EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public void onFailure(final Throwable caught) {
        u.fire(eventBus, new HideWaitingIndicatorEvent());
        u.fire(eventBus, new TechnicalErrorEvent(caught));
    }

}
