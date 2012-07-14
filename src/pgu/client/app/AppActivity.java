package pgu.client.app;

import java.util.ArrayList;

import pgu.client.app.event.ExceptionEvent;
import pgu.client.app.event.ImportBooksEvent;
import pgu.client.app.event.SearchBooksEvent;
import pgu.client.app.mvp.ClientFactory;
import pgu.client.app.utils.Notification;
import pgu.client.books.BooksPlace;
import pgu.client.importBooks.ImportBooksPlace;
import pgu.client.menu.MenuActivity;
import pgu.client.menu.MenuView;

import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;

public class AppActivity implements SearchBooksEvent.Handler, ExceptionEvent.Handler, ImportBooksEvent.Handler {

    private final AppView                        view;
    private final PlaceController                placeController;
    private final ArrayList<HandlerRegistration> handlerRegs = new ArrayList<HandlerRegistration>();

    public AppActivity(final PlaceController placeController, final AppView view) {
        this.placeController = placeController;
        this.view = view;
    }

    public void start(final EventBus eventBus, final ClientFactory clientFactory) {

        handlerRegs.add(eventBus.addHandler(SearchBooksEvent.TYPE, this));
        handlerRegs.add(eventBus.addHandler(ExceptionEvent.TYPE, this));

        final MenuView menuView = clientFactory.getMenuView();
        final MenuActivity menuActivity = new MenuActivity(menuView, clientFactory.getLoginInfo());
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

    @Override
    public void onException(final ExceptionEvent event) {
        final Throwable th = event.getThrowable();
        final StringBuilder sb = new StringBuilder();
        sb.append(th.getMessage());
        sb.append("<br>");

        for (final StackTraceElement ste : th.getStackTrace()) {
            sb.append(ste);
            sb.append("<br>");
        }

        view.getNotification().setHeading("Technical Error");
        view.getNotification().setText(sb.toString());
        view.getNotification().setLevel(Notification.Level.ERROR);
        view.getNotification().show();

    }

    @Override
    public void onImportBooks(final ImportBooksEvent event) {
        placeController.goTo(new ImportBooksPlace());
    }

}
