package pgu.client.app.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.google.web.bindery.event.shared.HandlerRegistration;

public class ImportBooksEvent extends GwtEvent<ImportBooksEvent.Handler> {

    public interface HasImportBooksHandlers extends HasHandlers {
        HandlerRegistration addImportBooksHandler(ImportBooksEvent.Handler handler);
    }

    public interface Handler extends EventHandler {
        void onImportBooks(ImportBooksEvent event);
    }

    public static final Type<Handler> TYPE = new Type<Handler>();

    @Override
    public Type<Handler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(final Handler handler) {
        handler.onImportBooks(this);
    }

}
