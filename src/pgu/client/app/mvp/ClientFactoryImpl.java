package pgu.client.app.mvp;

import pgu.client.app.AppView;
import pgu.client.app.ui.AppViewImpl;
import pgu.client.books.BooksView;
import pgu.client.books.ui.BooksViewImpl;
import pgu.client.menu.MenuView;
import pgu.client.menu.ui.MenuViewImpl;

import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;

public class ClientFactoryImpl implements ClientFactory {

    private static EventBus        eventBus        = new SimpleEventBus();
    private static PlaceController placeController = new PlaceController(eventBus);
    private static AppView         appView         = new AppViewImpl();
    private static MenuView        menuView        = new MenuViewImpl();
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

    @Override
    public AppView getAppView() {
        return appView;
    }

    @Override
    public MenuView getMenuView() {
        return menuView;
    }

}
