package pgu.client.books;

import java.util.ArrayList;

import pgu.client.app.mvp.ClientFactory;
import pgu.shared.domain.Book;
import pgu.shared.dto.BooksResult;
import pgu.shared.dto.BooksSearch;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class BooksActivity extends AbstractActivity implements BooksPresenter {

    private final ClientFactory clientFactory;

    public BooksActivity(final BooksPlace place, final ClientFactory clientFactory) {
        this.clientFactory = clientFactory;
    }

    @Override
    public void start(final AcceptsOneWidget panel, final EventBus eventBus) {
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
                final int stop = booksSearch.getStart() + booksSearch.getLength();
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
                booksResult.setStart(start);
                booksResult.setLength(booksSearch.getLength());
                booksResult.setNbFound(total);

                clientFactory.getBooksView().setBooks(booksResult);
            }

        }.schedule(500);

    }

}
