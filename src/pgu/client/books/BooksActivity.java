package pgu.client.books;

import java.util.ArrayList;

import pgu.client.app.event.HideWaitingIndicatorEvent;
import pgu.client.app.event.SearchBooksEvent;
import pgu.client.app.event.ShowWaitingIndicatorEvent;
import pgu.client.app.mvp.ClientFactory;
import pgu.client.app.utils.AsyncCallbackApp;
import pgu.client.service.BooksServiceAsync;
import pgu.shared.domain.Book;
import pgu.shared.dto.BooksQueryParameters;
import pgu.shared.dto.BooksResult;
import pgu.shared.dto.BooksSearch;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.HandlerRegistration;

public class BooksActivity extends AbstractActivity implements BooksPresenter {

    private EventBus                             eventBus;
    private final ArrayList<HandlerRegistration> handlerRegs = new ArrayList<HandlerRegistration>();
    private final BooksPlace                     place;
    private final BooksView                      view;
    private final BooksServiceAsync              booksService;

    public BooksActivity(final BooksPlace place, final ClientFactory clientFactory) {
        this.place = place;
        view = clientFactory.getBooksView();
        booksService = clientFactory.getBooksService();
    }

    @Override
    public void start(final AcceptsOneWidget panel, final EventBus eventBus) {
        this.eventBus = eventBus;

        view.setPresenter(this);
        panel.setWidget(view.asWidget());

        searchBooks(place.getBooksSearch());
    }

    @Override
    public void goToSearchBooks(final BooksSearch booksSearch) {
        eventBus.fireEvent(new SearchBooksEvent(booksSearch));
    }

    private void searchBooks(final BooksSearch booksSearch) {
        if (booksSearch == null) {
            view.clear();
            return;
        }

        eventBus.fireEvent(new ShowWaitingIndicatorEvent());

        // testBooks(booksSearch);
        final BooksQueryParameters queryParameters = new BooksQueryParameters();
        final int start = booksSearch.getStart();
        final int length = booksSearch.getLength();

        GWT.log("start " + start);
        GWT.log("length " + length);
        queryParameters.setSearchText(booksSearch.getTitle());

        booksService.fetchBooks(queryParameters, start, length, new AsyncCallbackApp<BooksResult>(eventBus) {

            @Override
            public void onSuccess(final BooksResult booksResult) {
                eventBus.fireEvent(new HideWaitingIndicatorEvent());
                booksResult.setBooksSearch(booksSearch);
                view.setBooks(booksResult);

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

}
