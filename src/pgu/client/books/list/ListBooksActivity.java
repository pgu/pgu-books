package pgu.client.books.list;

import java.util.ArrayList;

import pgu.client.app.event.AskForNewSearchBooksEvent;
import pgu.client.app.event.AskForNextPageSearchBooksEvent;
import pgu.client.app.event.AskForPreviousPageSearchBooksEvent;
import pgu.client.app.event.BookEditEvent;
import pgu.client.app.event.DeleteBooksEvent;
import pgu.client.app.event.DoSearchBooksEvent;
import pgu.client.app.event.HideWaitingIndicatorEvent;
import pgu.client.app.event.RefreshBooksEvent;
import pgu.client.app.event.ShowWaitingIndicatorEvent;
import pgu.client.app.event.UpdateNavigationEvent;
import pgu.client.app.event.UpdateResultsPerPageEvent;
import pgu.client.app.event.UpdateSortEvent;
import pgu.client.app.mvp.ClientFactory;
import pgu.client.app.utils.AsyncCallbackApp;
import pgu.client.app.utils.ClientUtils;
import pgu.client.service.BooksServiceAsync;
import pgu.shared.dto.BooksResult;
import pgu.shared.dto.BooksSearch;
import pgu.shared.dto.LoginInfo;
import pgu.shared.utils.SortField;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.HandlerRegistration;

public class ListBooksActivity extends AbstractActivity implements ListBooksPresenter //
        , RefreshBooksEvent.Handler //
        , DoSearchBooksEvent.Handler //
{

    private EventBus                             eventBus;
    private final ArrayList<HandlerRegistration> handlerRegs = new ArrayList<HandlerRegistration>();
    private final ListBooksPlace                 place;
    private final ListBooksView                  view;
    private final LoginInfo                      loginInfo;
    private boolean                              isEditable  = false;

    private final BooksServiceAsync              booksService;
    private final ClientFactory                  clientFactory;
    private final ClientUtils                    u           = new ClientUtils();

    public ListBooksActivity(final ListBooksPlace place, final ClientFactory clientFactory) {
        this.place = place;
        view = clientFactory.getBooksView();
        loginInfo = clientFactory.getLoginInfo();
        booksService = clientFactory.getBooksService();
        this.clientFactory = clientFactory;
    }

    @Override
    public void start(final AcceptsOneWidget panel, final EventBus eventBus) {
        this.eventBus = eventBus;
        isEditable = loginInfo.isLoggedIn();

        view.setPresenter(this);

        view.getCreateBookWidget().setVisible(isEditable);
        view.getEditBookWidget().setVisible(isEditable);
        view.getDeleteBooksWidget().setVisible(isEditable);
        view.getRefreshBooksWidget().setVisible(isEditable);

        panel.setWidget(view.asWidget());

        handlerRegs.add(eventBus.addHandler(RefreshBooksEvent.TYPE, this));
        handlerRegs.add(eventBus.addHandler(DoSearchBooksEvent.TYPE, this));

        handlerRegs.add(view.getCreateBookWidget().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {
                u.fire(eventBus, new BookEditEvent(null));
            }
        }));
        handlerRegs.add(view.getEditBookWidget().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {
                u.fire(eventBus, new BookEditEvent(view.getSelectedBook()));
            }
        }));
        handlerRegs.add(view.getRefreshBooksWidget().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {
                u.fire(eventBus, new RefreshBooksEvent());
            }
        }));
        handlerRegs.add(view.getDeleteBooksWidget().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {
                u.fire(eventBus, new DeleteBooksEvent(view.getSelectedBooks()));
            }
        }));
        handlerRegs.add(view.getPreviousPageWidget().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {
                u.fire(eventBus, new AskForPreviousPageSearchBooksEvent());
            }
        }));
        handlerRegs.add(view.getNextPageWidget().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {
                u.fire(eventBus, new AskForNextPageSearchBooksEvent());
            }
        }));

        u.fire(eventBus, new AskForNewSearchBooksEvent(place.getPage(), place.getSearchHashcode()));
    }

    @Override
    public void onStop() {

        for (HandlerRegistration handlerReg : handlerRegs) {
            handlerReg.removeHandler();
            handlerReg = null;
        }
        handlerRegs.clear();

        super.onStop();
    }

    @Override
    public void updateResultsPerPage(final int resultsPerPage) {
        u.fire(eventBus, new UpdateResultsPerPageEvent(resultsPerPage));
    }

    @Override
    public void updateSort(final SortField sortField, final boolean isAscending) {
        u.fire(eventBus, new UpdateSortEvent(sortField, isAscending));
    }

    @Override
    public void onRefreshBooks(final RefreshBooksEvent event) {
        new Timer() {

            @Override
            public void run() {
                u.fire(eventBus, new AskForNewSearchBooksEvent(place.getPage(), place.getSearchHashcode()));
            }

        }.schedule(1000);
    }

    @Override
    public void onDoSearchBooks(final DoSearchBooksEvent event) {
        u.fire(eventBus, new ShowWaitingIndicatorEvent());

        final BooksSearch search = event.getBooksSearch();
        final int page = event.getPage();

        booksService.fetchBooks(search, page, event.getCursor(), new AsyncCallbackApp<BooksResult>(eventBus) {

            @Override
            public void onSuccess(final BooksResult booksResult) {
                u.fire(eventBus, new HideWaitingIndicatorEvent());

                u.fire(eventBus,
                        new UpdateNavigationEvent(search, booksResult.getNextPage(), booksResult.getNextCursor()));

                // TODO PGU review
                view.setResultsPerPage(search.getLength());
                view.setCurrentSort(search.getSortField(), search.isAscending());
                view.isFirstPage(page == 0);
                view.hasNextPage(!u.isVoid(booksResult.getNextCursor()));
                view.isEditable(isEditable);
                view.setBooks(booksResult.getBooks());
            }
        });

    }

}
