package pgu.client.app.utils;

import pgu.client.app.event.HideWaitingIndicatorEvent;
import pgu.client.app.event.TechnicalErrorEvent;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.event.shared.EventBus;

public abstract class AsyncCallbackApp<T> implements AsyncCallback<T> {

    private final EventBus eventBus;

    public AsyncCallbackApp(final EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public void onFailure(final Throwable caught) {
        eventBus.fireEvent(new HideWaitingIndicatorEvent());
        eventBus.fireEvent(new TechnicalErrorEvent(caught));
    }

}
