package pgu.client.books;

import java.util.ArrayList;
import java.util.HashSet;

import pgu.client.app.event.BookEditEvent;
import pgu.client.app.event.DeleteBooksEvent;
import pgu.client.app.event.HideWaitingIndicatorEvent;
import pgu.client.app.event.RefreshBooksEvent;
import pgu.client.app.event.SearchBooksEvent;
import pgu.client.app.event.ShowWaitingIndicatorEvent;
import pgu.client.app.mvp.ClientFactory;
import pgu.client.app.utils.AppSetup;
import pgu.client.app.utils.AsyncCallbackApp;
import pgu.client.app.utils.SearchNavigation;
import pgu.client.service.BooksServiceAsync;
import pgu.shared.dto.BooksResult;
import pgu.shared.dto.BooksSearch;
import pgu.shared.dto.LoginInfo;
import pgu.shared.utils.SortField;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.HandlerRegistration;

public class BooksActivity extends AbstractActivity implements BooksPresenter //
        , RefreshBooksEvent.Handler //
{

    private EventBus                             eventBus;
    private final ArrayList<HandlerRegistration> handlerRegs = new ArrayList<HandlerRegistration>();
    private final BooksPlace                     place;
    private final BooksView                      view;
    private final AppSetup                       appSetup;
    private final LoginInfo                      loginInfo;
    private boolean                              isEditable  = false;

    private BooksSearch                          booksSearch;
    private int                                  destinationPage;

    private final BooksServiceAsync              booksService;
    private final ClientFactory                  clientFactory;

    public BooksActivity(final BooksPlace place, final ClientFactory clientFactory) {
        this.place = place;
        view = clientFactory.getBooksView();
        appSetup = clientFactory.getAppSetup();
        loginInfo = clientFactory.getLoginInfo();
        booksService = clientFactory.getBooksService();
        this.clientFactory = clientFactory;
    }

    @Override
    public void start(final AcceptsOneWidget panel, final EventBus eventBus) {
        this.eventBus = eventBus;
        booksSearch = place.getBooksSearch();
        destinationPage = place.getPage();
        isEditable = loginInfo.isLoggedIn();

        view.setPresenter(this);

        view.getCreateBookWidget().setVisible(isEditable);
        view.getEditBookWidget().setVisible(isEditable);
        view.getDeleteBooksWidget().setVisible(isEditable);
        view.getRefreshBooksWidget().setVisible(isEditable);

        panel.setWidget(view.asWidget());

        handlerRegs.add(eventBus.addHandler(RefreshBooksEvent.TYPE, this));

        handlerRegs.add(view.getCreateBookWidget().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {
                eventBus.fireEvent(new BookEditEvent(null));
            }
        }));
        handlerRegs.add(view.getEditBookWidget().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {
                eventBus.fireEvent(new BookEditEvent(view.getSelectedBook()));
            }
        }));
        handlerRegs.add(view.getDeleteBooksWidget().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {
                eventBus.fireEvent(new DeleteBooksEvent(view.getSelectedBooks()));
            }
        }));
        handlerRegs.add(view.getRefreshBooksWidget().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {
                eventBus.fireEvent(new RefreshBooksEvent());
            }
        }));
        // handlerRegs.add(view.getPreviousPageWidget().addClickHandler(new ClickHandler() {
        //
        // @Override
        // public void onClick(final ClickEvent event) {
        // // TODO PGU Jul 25, 2012
        // searchBooks();
        // }
        //
        // }));

        searchBooks();
    }

    @Override
    public void goToSearchBooks(final BooksSearch booksSearch) {
        eventBus.fireEvent(new SearchBooksEvent());
    }

    private void searchBooks() {
        eventBus.fireEvent(new ShowWaitingIndicatorEvent());

        booksService.fetchBooks(currentSearch.copy(), new AsyncCallbackApp<BooksResult>(eventBus) {

            @Override
            public void onSuccess(final BooksResult booksResult) {
                eventBus.fireEvent(new HideWaitingIndicatorEvent());

                final String nextCursor = booksResult.getNextCursor();
                final int nextDestinationPage = booksResult.getNextDestinationPage();

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

                placeController.goTo(new BooksPlace(currentSearch.copy(), destinationPage));
            }
        });

        view.setBooks(books, booksSearch, isEditable);
    }

    @Override
    public void onStop() {
        view.clearHandlers();
        for (HandlerRegistration handlerReg : handlerRegs) {
            handlerReg.removeHandler();
            handlerReg = null;
        }
        handlerRegs.clear();

        super.onStop();
    }

    @Override
    public void updateResultsPerPage(final int resultsPerPage) {
        appSetup.setResultsPerPage(resultsPerPage);
    }

    @Override
    public void updateSort(final SortField sortField, final boolean isAscending) {
        appSetup.setSortField(sortField);
        appSetup.setAscending(isAscending);
    }

    @Override
    public void onRefreshBooks(final RefreshBooksEvent event) {
        searchBooks();
    }

}
