package pgu.client.app;

import java.util.ArrayList;

import pgu.client.app.event.SearchBooksEvent;
import pgu.client.app.mvp.ClientFactory;
import pgu.client.books.BooksPlace;
import pgu.client.menu.MenuActivity;
import pgu.client.menu.MenuView;

import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;

public class AppActivity implements SearchBooksEvent.Handler {

    private final AppView                        view;
    private final PlaceController                placeController;
    private final ArrayList<HandlerRegistration> handlerRegs = new ArrayList<HandlerRegistration>();

    public AppActivity(final PlaceController placeController, final AppView view) {
        this.placeController = placeController;
        this.view = view;
    }

    public void start(final EventBus eventBus, final ClientFactory clientFactory) {

        handlerRegs.add(eventBus.addHandler(SearchBooksEvent.TYPE, this));

        final MenuView menuView = clientFactory.getMenuView();
        final MenuActivity menuActivity = new MenuActivity(menuView);
        menuActivity.start(eventBus);

        view.getHeader().setWidget(menuView);
    }

    @Override
    public void onSearchBooks(final SearchBooksEvent event) {
        placeController.goTo(new BooksPlace(event.getBooksSearch()));
    }

    public void onStop() {
        for (HandlerRegistration handlerReg : handlerRegs) {
            handlerReg.removeHandler();
            handlerReg = null;
        }
        handlerRegs.clear();
    }

}
