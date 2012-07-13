package pgu.client.app.mvp;

import pgu.client.app.AppView;
import pgu.client.books.BooksView;
import pgu.client.menu.MenuView;

import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;

public interface ClientFactory {

    EventBus getEventBus();

    PlaceController getPlaceController();

    BooksView getBooksView();

    AppView getAppView();

    MenuView getMenuView();
}
