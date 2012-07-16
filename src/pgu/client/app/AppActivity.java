package pgu.client.app;

import java.util.ArrayList;

import pgu.client.app.event.ImportBooksEvent;
import pgu.client.app.event.NotificationEvent;
import pgu.client.app.event.SearchBooksEvent;
import pgu.client.app.event.SetupEvent;
import pgu.client.app.event.TechnicalErrorEvent;
import pgu.client.app.mvp.ClientFactory;
import pgu.client.app.utils.Level;
import pgu.client.app.utils.Notification;
import pgu.client.books.BooksPlace;
import pgu.client.importBooks.ImportBooksPlace;
import pgu.client.menu.MenuActivity;
import pgu.client.menu.MenuView;
import pgu.client.setup.SetupPlace;

import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;

public class AppActivity implements SearchBooksEvent.Handler //
        , TechnicalErrorEvent.Handler //
        , ImportBooksEvent.Handler //
        , SetupEvent.Handler //
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
        handlerRegs.add(eventBus.addHandler(SetupEvent.TYPE, this));

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

        final Notification notif = view.newNotification();
        notif.setHeading("Technical Error");
        notif.setText(sb.toString());
        notif.setLevel(Level.ERROR);
        notif.show();

    }

    @Override
    public void onImportBooks(final ImportBooksEvent event) {
        placeController.goTo(new ImportBooksPlace());
    }

    @Override
    public void onNotification(final NotificationEvent event) {

        final Level level = event.getLevel();
        final String message = event.getMessage();

        final Notification notification = view.newNotification();
        notification.setText(message);

        if (Level.INFO == level) {
            notification.setHeading("Info");
            notification.setLevel(Level.INFO);

        } else if (Level.SUCCESS == level) {
            notification.setHeading("Success");
            notification.setLevel(Level.SUCCESS);

        } else if (Level.WARNING == level) {
            notification.setHeading("Warning");
            notification.setLevel(Level.WARNING);

        } else if (Level.ERROR == level) {
            notification.setHeading("Ups!");
            notification.setLevel(Level.ERROR);
        }

        notification.show();
    }

    @Override
    public void onSetup(final SetupEvent event) {
        placeController.goTo(new SetupPlace());
    }

}
