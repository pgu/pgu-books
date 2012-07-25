package pgu.client.books;

import java.util.HashMap;

import pgu.client.app.AppState;
import pgu.client.app.mvp.ClientFactory;
import pgu.shared.dto.BooksSearch;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

public class BooksPlace extends Place {

    public static ClientFactory                 clientFactory;

    private static HashMap<String, BooksSearch> token2search = new HashMap<String, BooksSearch>();
    private static HashMap<String, Integer>     token2page   = new HashMap<String, Integer>();

    private BooksSearch                         search;
    private int                                 page;

    public BooksPlace() {

        final AppState appState = clientFactory.getAppState();
        search = appState.getSearch();
        page = appState.getDestinationPage();
    }

    public String getToken(final BooksSearch search, final int page) {
        return Integer.toString(search.hashCode() + page);
    }

    public BooksPlace(final BooksSearch search, final int destinationPage) {
        clientFactory.getAppState().setCurrentSearch(search, destinationPage);
    }

    public static class Tokenizer implements PlaceTokenizer<BooksPlace> {

        @Override
        public String getToken(final BooksPlace place) {
            return BooksPlace.clientFactory.getHistoryToken(place.search, place.page);
        }

        @Override
        public BooksPlace getPlace(final String token) {

            final BooksSearch search = token2search.get(token);
            final Integer page = token2page.get(token);

            return new BooksPlace(search, page);
        }
    }

    public BooksSearch getBooksSearch() {
        return search;
    }

    public int getPage() {
        return page;
    }

}
