package pgu.client.app;

import java.util.ArrayList;
import java.util.HashMap;

import pgu.client.app.event.AskForNewSearchBooksEvent;
import pgu.client.app.event.AskForNextPageSearchBooksEvent;
import pgu.client.app.event.AskForPreviousPageSearchBooksEvent;
import pgu.client.app.event.BookEditEvent;
import pgu.client.app.event.DeleteBooksEvent;
import pgu.client.app.event.DoSearchBooksEvent;
import pgu.client.app.event.GoToBooksEvent;
import pgu.client.app.event.ImportBooksEvent;
import pgu.client.app.event.NotificationEvent;
import pgu.client.app.event.SetupEvent;
import pgu.client.app.event.TechnicalErrorEvent;
import pgu.client.app.event.UpdateNavigationEvent;
import pgu.client.app.event.UpdateResultsPerPageEvent;
import pgu.client.app.event.UpdateSortEvent;
import pgu.client.app.mvp.ClientFactory;
import pgu.client.app.utils.ClientUtils;
import pgu.client.app.utils.Level;
import pgu.client.app.utils.Notification;
import pgu.client.books.delete.DeleteBooksActivity;
import pgu.client.books.edit.EditBookActivity;
import pgu.client.books.list.ListBooksPlace;
import pgu.client.books.upload.ImportBooksPlace;
import pgu.client.menu.MenuActivity;
import pgu.client.menu.MenuView;
import pgu.client.setup.SetupPlace;
import pgu.shared.dto.BooksSearch;
import pgu.shared.utils.SearchField;

import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;

public class AppActivity implements //
        GoToBooksEvent.Handler //
        , TechnicalErrorEvent.Handler //
        , ImportBooksEvent.Handler //
        , SetupEvent.Handler //
        , NotificationEvent.Handler //
        , BookEditEvent.Handler //
        , DeleteBooksEvent.Handler //
        , AskForNewSearchBooksEvent.Handler //
        , AskForNextPageSearchBooksEvent.Handler //
        , AskForPreviousPageSearchBooksEvent.Handler //
        , UpdateNavigationEvent.Handler //
        , UpdateResultsPerPageEvent.Handler //
        , UpdateSortEvent.Handler //
{

    private final ClientFactory                  clientFactory;
    private final AppView                        view;
    private final MenuView                       menuView;
    private final PlaceController                placeController;
    private final ArrayList<HandlerRegistration> handlerRegs = new ArrayList<HandlerRegistration>();
    private EventBus                             eventBus;
    private final ClientUtils                    u           = new ClientUtils();

    private static final int                     PAGE_INIT   = 0;
    private static final String                  CURSOR_INIT = null;

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
        handlerRegs.add(eventBus.addHandler(SetupEvent.TYPE, this));
        handlerRegs.add(eventBus.addHandler(NotificationEvent.TYPE, this));
        handlerRegs.add(eventBus.addHandler(BookEditEvent.TYPE, this));
        handlerRegs.add(eventBus.addHandler(DeleteBooksEvent.TYPE, this));

        handlerRegs.add(eventBus.addHandler(AskForNewSearchBooksEvent.TYPE, this));
        handlerRegs.add(eventBus.addHandler(AskForNextPageSearchBooksEvent.TYPE, this));
        handlerRegs.add(eventBus.addHandler(AskForPreviousPageSearchBooksEvent.TYPE, this));

        handlerRegs.add(eventBus.addHandler(UpdateNavigationEvent.TYPE, this));
        handlerRegs.add(eventBus.addHandler(UpdateResultsPerPageEvent.TYPE, this));
        handlerRegs.add(eventBus.addHandler(UpdateSortEvent.TYPE, this));

        final MenuActivity menuActivity = new MenuActivity(clientFactory);
        menuActivity.start(eventBus);

        view.getHeader().setWidget(menuView);
    }

    private BooksSearch currentSearch = new BooksSearch();

    @Override
    public void onGoToBooks(final GoToBooksEvent event) {

        final HashMap<SearchField, String> filters = event.getFilters();

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

        currentSearch = search;

        updateSearchAndGoToPlace();
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
        final EditBookActivity bookActivity = new EditBookActivity(clientFactory);
        bookActivity.start(event.getBook());
    }

    @Override
    public void onDeleteBooks(final DeleteBooksEvent event) {
        final DeleteBooksActivity deleteBooksActivity = new DeleteBooksActivity(clientFactory);
        deleteBooksActivity.start(event.getBooks());
    }

    private int                                                   page               = PAGE_INIT;

    private static HashMap<BooksSearch, HashMap<Integer, String>> search2page2cursor = new HashMap<BooksSearch, HashMap<Integer, String>>();

    @Override
    public void onAskForNewSearchBooks(final AskForNewSearchBooksEvent event) {

        if (!u.isVoid(event.getSearchHashcode())) {

            final int eventPage = event.getPage();
            final int searchHashcode = Integer.valueOf(event.getSearchHashcode());

            BooksSearch askedSearch = null;
            for (final BooksSearch search : search2page2cursor.keySet()) {
                if (search.hashCode() == searchHashcode) {
                    askedSearch = search;
                    break;
                }
            }

            if (askedSearch != null) {
                final HashMap<Integer, String> page2cursor = search2page2cursor.get(askedSearch);

                if (page2cursor.containsKey(eventPage)) {
                    final String eventCursor = page2cursor.get(eventPage);

                    currentSearch = askedSearch;
                    page = eventPage;

                    u.fire(eventBus, new DoSearchBooksEvent(askedSearch.copy(), eventPage, eventCursor));
                    return;
                }

            }
        }
        //
        // else
        final BooksSearch search = currentSearch.copy();

        if (!search2page2cursor.containsKey(search)) {
            final HashMap<Integer, String> page2cursor = new HashMap<Integer, String>();
            page2cursor.put(PAGE_INIT, CURSOR_INIT);
            search2page2cursor.put(search, page2cursor);
        }

        page = PAGE_INIT;

        u.fire(eventBus, new DoSearchBooksEvent(search, PAGE_INIT, CURSOR_INIT));
    }

    @Override
    public void onAskForPreviousSearchBooks(final AskForPreviousPageSearchBooksEvent event) {
        placeController.goTo(new ListBooksPlace(currentSearch.hashCode(), page - 1));
    }

    @Override
    public void onAskForNextSearchBooks(final AskForNextPageSearchBooksEvent event) {
        placeController.goTo(new ListBooksPlace(currentSearch.hashCode(), page + 1));
    }

    @Override
    public void onUpdateNavigation(final UpdateNavigationEvent event) {

        final String nextCursor = event.getNextCursor();
        if (nextCursor != null) {
            search2page2cursor.get(event.getSearch()).put( //
                    event.getNextPage() //
                    , nextCursor //
                    );
        }
    }

    @Override
    public void onUpdateSort(final UpdateSortEvent event) {

        currentSearch.setSortField(event.getSortField());
        currentSearch.setAscending(event.isAscending());

        updateSearchAndGoToPlace();
    }

    @Override
    public void onUpdateResultsPerPage(final UpdateResultsPerPageEvent event) {

        currentSearch.setLength(event.getResultsPerPage());

        updateSearchAndGoToPlace();
    }

    private void updateSearchAndGoToPlace() {

        if (!search2page2cursor.containsKey(currentSearch)) {
            final HashMap<Integer, String> page2cursor = new HashMap<Integer, String>();
            page2cursor.put(PAGE_INIT, CURSOR_INIT);
            search2page2cursor.put(currentSearch.copy(), page2cursor);
        }

        page = PAGE_INIT;

        placeController.goTo(new ListBooksPlace(currentSearch.hashCode(), PAGE_INIT));
    }

}
