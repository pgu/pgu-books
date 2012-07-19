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
import pgu.shared.domain.Book;
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

        view.getNewBookWidget().setVisible(loginInfo.isLoggedIn());
        view.getEditionBookWidget().setVisible(loginInfo.isLoggedIn());
        view.getDeleteBooksWidget().setVisible(loginInfo.isLoggedIn());

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

        searchBooks(place.getBooksSearch());
    }

    @Override
    public void goToSearchBooks(final BooksSearch booksSearch) {
        eventBus.fireEvent(u.newSearchEvent(booksSearch));
    }

    private void searchBooks(final BooksSearch booksSearch) {
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
                    view.setBooks(booksResult);
                }
            }
        });
    }

    private void testBooks(final BooksSearch booksSearch) {
        new Timer() {

            @Override
            public void run() {

                int total;
                try {
                    total = Integer.parseInt(booksSearch.getTitle());
                } catch (final IllegalArgumentException e) {
                    total = 5;
                }

                final int start = booksSearch.getStart();

                final int delta1 = booksSearch.getLength();
                final int delta2 = total - booksSearch.getStart();

                final int stop = delta2 > delta1 ? delta1 : delta2;

                final ArrayList<Book> books = new ArrayList<Book>();
                for (int i = start + 1; i < start + 1 + stop; i++) {

                    final Book book = new Book() //
                            .title("title " + i) //
                            .author("author " + i) //
                            .editor("editor " + i) //
                            .category("cat " + i) //
                            .year(1980 + i) //
                            .comment("comment " + i) //
                            .id(1L * i) //
                    ;

                    books.add(book);
                }
                final BooksResult booksResult = new BooksResult();
                booksResult.setBooks(books);
                booksResult.setBooksSearch(booksSearch);
                booksResult.setNbFound(total);

                eventBus.fireEvent(new HideWaitingIndicatorEvent());
                view.setBooks(booksResult);
            }

        }.schedule(500);
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
        appSetup.setResultsPerPage(resultsPerPage);
    }

    @Override
    public void updateSort(final SortField sortField, final boolean isAscending) {
        appSetup.setSortField(sortField);
        appSetup.setAscending(isAscending);
    }

    @Override
    public void onRefreshBooks(final RefreshBooksEvent event) {
        searchBooks(place.getBooksSearch());
    }

}
