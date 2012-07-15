package pgu.client.app;

import java.util.ArrayList;

import pgu.client.app.event.ImportBooksEvent;
import pgu.client.app.event.NotificationEvent;
import pgu.client.app.event.SearchBooksEvent;
import pgu.client.app.event.TechnicalErrorEvent;
import pgu.client.app.mvp.ClientFactory;
import pgu.client.app.utils.Level;
import pgu.client.books.BooksPlace;
import pgu.client.importBooks.ImportBooksPlace;
import pgu.client.menu.MenuActivity;
import pgu.client.menu.MenuView;

import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;

public class AppActivity implements SearchBooksEvent.Handler //
        , TechnicalErrorEvent.Handler //
        , ImportBooksEvent.Handler //
        , NotificationEvent.Handler //
{

    private final AppView                        view;
    private final PlaceController                placeController;
    private final ArrayList<HandlerRegistration> handlerRegs = new ArrayList<HandlerRegistration>();

    public AppActivity(final PlaceController placeController, final AppView view) {
        this.placeController = placeController;
        this.view = view;
    }

    public void start(final EventBus eventBus, final ClientFactory clientFactory) {

        handlerRegs.add(eventBus.addHandler(SearchBooksEvent.TYPE, this));
        handlerRegs.add(eventBus.addHandler(TechnicalErrorEvent.TYPE, this));
        handlerRegs.add(eventBus.addHandler(ImportBooksEvent.TYPE, this));
        handlerRegs.add(eventBus.addHandler(NotificationEvent.TYPE, this));

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
    public void onTechnicalError(final TechnicalErrorEvent event) {
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
        view.getNotification().setLevel(Level.ERROR);
        view.getNotification().show();

    }

    @Override
    public void onImportBooks(final ImportBooksEvent event) {
        placeController.goTo(new ImportBooksPlace());
    }

    @Override
    public void onNotification(final NotificationEvent event) {

        final Level level = event.getLevel();

        if (Level.INFO == level) {
            view.getNotification().setHeading("Info");
            view.getNotification().setText(event.getMessage());
            view.getNotification().setLevel(Level.INFO);
            view.getNotification().show();

        } else if (Level.SUCCESS == level) {
            view.getNotification().setHeading("Success");
            view.getNotification().setText(event.getMessage());
            view.getNotification().setLevel(Level.SUCCESS);
            view.getNotification().show();

        } else if (Level.WARNING == level) {
            view.getNotification().setHeading("Warning");
            view.getNotification().setText(event.getMessage());
            view.getNotification().setLevel(Level.WARNING);
            view.getNotification().show();

        } else if (Level.ERROR == level) {
            view.getNotification().setHeading("Ups!");
            view.getNotification().setText(event.getMessage());
            view.getNotification().setLevel(Level.ERROR);
            view.getNotification().show();

        }
    }

}
