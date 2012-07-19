package pgu.client.books;

import java.util.ArrayList;

import pgu.client.app.event.BookEditEvent;
import pgu.client.app.event.HideWaitingIndicatorEvent;
import pgu.client.app.event.NotificationEvent;
import pgu.client.app.event.RefreshBooksEvent;
import pgu.client.app.event.ShowWaitingIndicatorEvent;
import pgu.client.app.mvp.ClientFactory;
import pgu.client.app.utils.AppSetup;
import pgu.client.app.utils.AsyncCallbackApp;
import pgu.client.app.utils.Level;
import pgu.client.app.utils.SearchUtils;
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
    private final BooksServiceAsync              booksService;
    private final AppSetup                       appSetup;
    private final LoginInfo                      loginInfo;
    private final SearchUtils                    u;
    private boolean                              isEditable  = false;

    public BooksActivity(final BooksPlace place, final ClientFactory clientFactory) {
        this.place = place;
        view = clientFactory.getBooksView();
        booksService = clientFactory.getBooksService();
        appSetup = clientFactory.getAppSetup();
        loginInfo = clientFactory.getLoginInfo();
        u = new SearchUtils(clientFactory);
    }

    @Override
    public void start(final AcceptsOneWidget panel, final EventBus eventBus) {
        this.eventBus = eventBus;

        view.setPresenter(this);

        isEditable = loginInfo.isLoggedIn();
        view.getNewBookWidget().setVisible(isEditable);
        view.getEditionBookWidget().setVisible(isEditable);
        view.getDeleteBooksWidget().setVisible(isEditable);

        panel.setWidget(view.asWidget());

        handlerRegs.add(eventBus.addHandler(RefreshBooksEvent.TYPE, this));

        handlerRegs.add(view.getNewBookWidget().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {
                eventBus.fireEvent(new BookEditEvent(null));
            }
        }));
        handlerRegs.add(view.getEditionBookWidget().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {
                eventBus.fireEvent(new BookEditEvent(view.getSelectedBook()));
            }
        }));

        searchBooks(place.getBooksSearch(), isEditable);
    }

    @Override
    public void goToSearchBooks(final BooksSearch booksSearch) {
        eventBus.fireEvent(u.newSearchEvent(booksSearch));
    }

    private void searchBooks(final BooksSearch booksSearch, final boolean isEditable) {
        final BooksSearch search = booksSearch == null ? u.newBooksSearch() : u.updateSearch(booksSearch);

        eventBus.fireEvent(new ShowWaitingIndicatorEvent());

        booksService.fetchBooks(search, new AsyncCallbackApp<BooksResult>(eventBus) {

            @Override
            public void onSuccess(final BooksResult booksResult) {
                eventBus.fireEvent(new HideWaitingIndicatorEvent());

                if (booksResult.getNbFound() > 1000) {
                    eventBus.fireEvent(new NotificationEvent(Level.WARNING, //
                            "Más de 1000 resultados correspondan a su búsqueda. <br>Modifiquen los criterios, por favor"));
                } else {
                    booksResult.setBooksSearch(search);
                    view.setBooks(booksResult, isEditable);
                }
            }
        });
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
        searchBooks(place.getBooksSearch(), isEditable);
    }

}
