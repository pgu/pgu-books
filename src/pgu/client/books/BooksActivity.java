package pgu.client.books;

import java.util.ArrayList;

import pgu.client.app.event.AskForNewSearchBooksEvent;
import pgu.client.app.event.BookEditEvent;
import pgu.client.app.event.DeleteBooksEvent;
import pgu.client.app.event.DoSearchBooksEvent;
import pgu.client.app.event.GoToBooksEvent;
import pgu.client.app.event.HideWaitingIndicatorEvent;
import pgu.client.app.event.RefreshBooksEvent;
import pgu.client.app.event.ShowWaitingIndicatorEvent;
import pgu.client.app.event.UpdateNavigationEvent;
import pgu.client.app.event.UpdateResultsPerPageEvent;
import pgu.client.app.event.UpdateSortEvent;
import pgu.client.app.mvp.ClientFactory;
import pgu.client.app.utils.AsyncCallbackApp;
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
        , DoSearchBooksEvent.Handler //
{

    private EventBus                             eventBus;
    private final ArrayList<HandlerRegistration> handlerRegs = new ArrayList<HandlerRegistration>();
    private final BooksPlace                     place;
    private final BooksView                      view;
    private final LoginInfo                      loginInfo;
    private boolean                              isEditable  = false;

    private BooksSearch                          booksSearch;
    private int                                  destinationPage;

    private final BooksServiceAsync              booksService;
    private final ClientFactory                  clientFactory;

    public BooksActivity(final BooksPlace place, final ClientFactory clientFactory) {
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

        eventBus.fireEvent(new AskForNewSearchBooksEvent());
    }

    @Override
    public void goToSearchBooks(final BooksSearch booksSearch) {
        eventBus.fireEvent(new GoToBooksEvent());
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
        eventBus.fireEvent(new UpdateResultsPerPageEvent(resultsPerPage));
    }

    @Override
    public void updateSort(final SortField sortField, final boolean isAscending) {
        eventBus.fireEvent(new UpdateSortEvent(sortField, isAscending));
    }

    @Override
    public void onRefreshBooks(final RefreshBooksEvent event) {
        // TODO PGU Jul 26, 2012 TBD
    }

    @Override
    public void onDoSearchBooks(final DoSearchBooksEvent event) {
        eventBus.fireEvent(new ShowWaitingIndicatorEvent());

        final BooksSearch search = event.getBooksSearch();

        booksService.fetchBooks(search, event.getPage(), event.getCursor(),
                new AsyncCallbackApp<BooksResult>(eventBus) {

                    @Override
                    public void onSuccess(final BooksResult booksResult) {
                        eventBus.fireEvent(new HideWaitingIndicatorEvent());

                        eventBus.fireEvent(new UpdateNavigationEvent(search, booksResult.getNextPage(), booksResult
                                .getNextCursor()));

                        // TODO PGU Jul 26, 2012 review booksSearch here...
                        view.setBooks(booksResult.getBooks(), booksSearch, isEditable);
                    }
                });

    }

}
