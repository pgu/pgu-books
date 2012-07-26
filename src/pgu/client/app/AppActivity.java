package pgu.client.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import pgu.client.app.event.BookEditEvent;
import pgu.client.app.event.DeleteBooksEvent;
import pgu.client.app.event.GetConfigAndSearchEvent;
import pgu.client.app.event.GoToBooksEvent;
import pgu.client.app.event.ImportBooksEvent;
import pgu.client.app.event.NotificationEvent;
import pgu.client.app.event.SearchBooksEvent;
import pgu.client.app.event.SetupEvent;
import pgu.client.app.event.TechnicalErrorEvent;
import pgu.client.app.event.UpdateNavigationEvent;
import pgu.client.app.mvp.ClientFactory;
import pgu.client.app.utils.Level;
import pgu.client.app.utils.Notification;
import pgu.client.app.utils.SearchNavigation;
import pgu.client.book.BookActivity;
import pgu.client.books.BooksPlace;
import pgu.client.deleteBooks.DeleteBooksActivity;
import pgu.client.importBooks.ImportBooksPlace;
import pgu.client.menu.MenuActivity;
import pgu.client.menu.MenuView;
import pgu.client.setup.SetupPlace;
import pgu.shared.dto.BooksSearch;
import pgu.shared.utils.SearchField;

import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;

public class AppActivity implements GoToBooksEvent.Handler //
        , TechnicalErrorEvent.Handler //
        , ImportBooksEvent.Handler //
        , SetupEvent.Handler //
        , NotificationEvent.Handler //
        , BookEditEvent.Handler //
        , DeleteBooksEvent.Handler //
        , GetConfigAndSearchEvent.Handler //
        , UpdateNavigationEvent.Handler //
{

    private final ClientFactory                  clientFactory;
    private final AppView                        view;
    private final MenuView                       menuView;
    private final PlaceController                placeController;
    private final ArrayList<HandlerRegistration> handlerRegs = new ArrayList<HandlerRegistration>();
    private EventBus                             eventBus;

    public AppActivity(final PlaceController placeController, final ClientFactory clientFactory) {
        this.clientFactory = clientFactory;
        this.placeController = placeController;

        view = clientFactory.getAppView();
        menuView = clientFactory.getMenuView();

    }

    public void start(final EventBus eventBus) {
        this.eventBus = eventBus;

        handlerRegs.add(eventBus.addHandler(GoToBooksEvent.TYPE, this));
        handlerRegs.add(eventBus.addHandler(TechnicalErrorEvent.TYPE, this));
        handlerRegs.add(eventBus.addHandler(ImportBooksEvent.TYPE, this));
        handlerRegs.add(eventBus.addHandler(NotificationEvent.TYPE, this));
        handlerRegs.add(eventBus.addHandler(SetupEvent.TYPE, this));
        handlerRegs.add(eventBus.addHandler(BookEditEvent.TYPE, this));
        handlerRegs.add(eventBus.addHandler(DeleteBooksEvent.TYPE, this));

        final MenuActivity menuActivity = new MenuActivity(clientFactory);
        menuActivity.start(eventBus);

        view.getHeader().setWidget(menuView);
    }

    private final HashSet<BooksSearch> searches      = new HashSet<BooksSearch>();
    private BooksSearch                currentSearch = new BooksSearch();

    @Override
    public void onGoToBooks(final GoToBooksEvent event) {

        final HashMap<SearchField, String> filters = event.getFilters();
        if (!filters.isEmpty()) {

            final BooksSearch search = new BooksSearch();
            search.setAuthor(filters.get(SearchField.AUTHOR));
            search.setCategory(filters.get(SearchField.CATEGORY));
            search.setComment(filters.get(SearchField.COMMENT));
            search.setEditor(filters.get(SearchField.EDITOR));
            search.setTitle(filters.get(SearchField.TITLE));
            search.setYear(filters.get(SearchField.YEAR));

            search.setLength(currentSearch.getLength());
            search.setSortField(currentSearch.getSortField());
            search.setAscending(currentSearch.isAscending());

            searches.add(currentSearch);
            currentSearch = search;

        }

        placeController.goTo(new BooksPlace());
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
        notification.setHTML(message);

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

    @Override
    public void onBookEdit(final BookEditEvent event) {
        final BookActivity bookActivity = new BookActivity(clientFactory);
        bookActivity.start(event.getBook());
    }

    @Override
    public void onDeleteBooks(final DeleteBooksEvent event) {
        final DeleteBooksActivity deleteBooksActivity = new DeleteBooksActivity(clientFactory);
        deleteBooksActivity.start(event.getBooks());
    }

    @Override
    public void onGetConfigAndSearch(final GetConfigAndSearchEvent event) {
        // TODO PGU Jul 26, 2012
        final int page = 0;
        final String cursor = "cursor";
        eventBus.fireEvent(new SearchBooksEvent(currentSearch.copy(), page, cursor));
    }

    @Override
    public void onUpdateNavigation(final UpdateNavigationEvent event) {
        // TODO PGU Jul 26, 2012
        final String nextCursor = event.getNextCursor();
        final int nextPage = event.getNextPage();

        // TODO PGU
        if (nextCursor != null) {
            if (search2navigation.containsKey(currentSearch)) {

                final SearchNavigation navigation = new SearchNavigation();
                navigation.cursor = nextCursor;
                navigation.pageNb = nextDestinationPage;
                search2navigation.get(currentSearch).add(navigation);

            } else {
                final SearchNavigation navigation = new SearchNavigation();
                navigation.cursor = nextCursor;
                navigation.pageNb = nextDestinationPage;

                final HashSet<SearchNavigation> navigations = new HashSet<SearchNavigation>();
                navigations.add(navigation);

                search2navigation.put(currentSearch.copy(), navigations);
            }
        }

    }

}
