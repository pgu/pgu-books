package pgu.client.app.event;

import java.util.HashMap;

import pgu.shared.utils.SearchField;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.google.web.bindery.event.shared.HandlerRegistration;

public class GoToBooksEvent extends GwtEvent<GoToBooksEvent.Handler> {

    public interface GoToBooksHandlers extends HasHandlers {
        HandlerRegistration addGoToBooksHandler(GoToBooksEvent.Handler handler);
    }

    public interface Handler extends EventHandler {
        void onGoToBooks(GoToBooksEvent event);
    }

    public static final Type<Handler>          TYPE    = new Type<Handler>();

    private final HashMap<SearchField, String> filters = new HashMap<SearchField, String>();

    @Override
    public Type<Handler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(final Handler handler) {
        handler.onGoToBooks(this);
    }

    public void setFilters(final HashMap<SearchField, String> filters) {
        this.filters.putAll(filters);
    }

    public HashMap<SearchField, String> getFilters() {
        return filters;
    }

}
