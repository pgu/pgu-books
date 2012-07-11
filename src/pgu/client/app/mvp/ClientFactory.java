package pgu.client.app.mvp;

import pgu.client.books.BooksView;

import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;

public interface ClientFactory {

    EventBus getEventBus();

    PlaceController getPlaceController();

    BooksView getBooksView();
}
