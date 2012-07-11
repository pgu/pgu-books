package pgu.client.app.mvp;

import pgu.client.books.BooksView;
import pgu.client.books.ui.BooksViewImpl;

import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;

public class ClientFactoryImpl implements ClientFactory {

    private static EventBus        eventBus        = new SimpleEventBus();
    private static PlaceController placeController = new PlaceController(eventBus);
    private static BooksView       booksView       = new BooksViewImpl();

    @Override
    public EventBus getEventBus() {
        return eventBus;
    }

    @Override
    public PlaceController getPlaceController() {
        return placeController;
    }

    @Override
    public BooksView getBooksView() {
        return booksView;
    }

}
