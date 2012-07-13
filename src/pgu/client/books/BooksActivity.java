package pgu.client.books;

import java.util.ArrayList;

import pgu.client.app.event.SearchBooksEvent;
import pgu.client.app.mvp.ClientFactory;
import pgu.shared.domain.Book;
import pgu.shared.dto.BooksResult;
import pgu.shared.dto.BooksSearch;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.HandlerRegistration;

public class BooksActivity extends AbstractActivity implements BooksPresenter, SearchBooksEvent.Handler {

    private final ClientFactory                  clientFactory;
    private EventBus                             eventBus;

    private final ArrayList<HandlerRegistration> handlerRegs = new ArrayList<HandlerRegistration>();

    public BooksActivity(final BooksPlace place, final ClientFactory clientFactory) {
        this.clientFactory = clientFactory;
    }

    @Override
    public void start(final AcceptsOneWidget panel, final EventBus eventBus) {
        this.eventBus = eventBus;
        handlerRegs.add(eventBus.addHandler(SearchBooksEvent.TYPE, this));

        final BooksView booksView = clientFactory.getBooksView();

        booksView.setPresenter(this);

        panel.setWidget(booksView.asWidget());
    }

    @Override
    public void searchBooks(final BooksSearch booksSearch) {
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

                final int delta1 = booksSearch.getLength() - booksSearch.getStart();
                final int delta2 = total - booksSearch.getStart();

                final int stop = delta2 > delta1 ? delta1 : delta2;
                GWT.log("start " + start);
                GWT.log("stop " + stop);

                final ArrayList<Book> books = new ArrayList<Book>();
                for (int i = start + 1; i < stop + 1; i++) {

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

                clientFactory.getBooksView().setBooks(booksResult);
            }

        }.schedule(500);

    }

    @Override
    public void onSearchBooks(final SearchBooksEvent event) {
        // TODO PGU Jul 13, 2012
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
