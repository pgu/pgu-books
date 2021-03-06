package pgu.client.app.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.google.web.bindery.event.shared.HandlerRegistration;

public class UpdateResultsPerPageEvent extends GwtEvent<UpdateResultsPerPageEvent.Handler> {

    public interface HasUpdateResultsPerPageHandlers extends HasHandlers {
        HandlerRegistration addUpdateResultsPerPageHandler(UpdateResultsPerPageEvent.Handler handler);
    }

    public interface Handler extends EventHandler {
        void onUpdateResultsPerPage(UpdateResultsPerPageEvent event);
    }

    public static final Type<Handler> TYPE = new Type<Handler>();

    private final int                 resultsPerPage;

    public UpdateResultsPerPageEvent(final int resultsPerPage) {
        this.resultsPerPage = resultsPerPage;
    }

    @Override
    public Type<Handler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(final Handler handler) {
        handler.onUpdateResultsPerPage(this);
    }

    public int getResultsPerPage() {
        return resultsPerPage;
    }

}
