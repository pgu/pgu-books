package pgu.client.app.event;

import pgu.shared.utils.SortField;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.google.web.bindery.event.shared.HandlerRegistration;

public class UpdateSortEvent extends GwtEvent<UpdateSortEvent.Handler> {

    public interface HasUpdateSortHandlers extends HasHandlers {
        HandlerRegistration addUpdateSortHandler(UpdateSortEvent.Handler handler);
    }

    public interface Handler extends EventHandler {
        void onUpdateSort(UpdateSortEvent event);
    }

    public static final Type<Handler> TYPE = new Type<Handler>();

    private final SortField           sortField;
    private final boolean             isAscending;

    public UpdateSortEvent(final SortField sortField, final boolean isAscending) {
        this.sortField = sortField;
        this.isAscending = isAscending;
    }

    @Override
    public Type<Handler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(final Handler handler) {
        handler.onUpdateSort(this);
    }

    public SortField getSortField() {
        return sortField;
    }

    public boolean isAscending() {
        return isAscending;
    }

}
