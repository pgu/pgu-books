package pgu.client.books;

import java.util.HashMap;

import pgu.shared.dto.BooksSearch;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

public class BooksPlace extends Place {

    private static HashMap<String, BooksSearch> token2search = new HashMap<String, BooksSearch>();

    private final BooksSearch                   booksSearch;

    public BooksPlace() {
        booksSearch = null;
    }

    public BooksPlace(final BooksSearch booksSearch) {
        if (booksSearch != null) {

            final BooksSearch copy = booksSearch.copy();
            final String token = getToken(copy);
            if (!token2search.containsKey(token)) {
                token2search.put(token, copy);
            }
        }

        this.booksSearch = booksSearch;
    }

    public String getToken(final BooksSearch booksSearch) {
        if (booksSearch == null) {
            return "";
        }

        return Integer.toString(booksSearch.hashCode());
    }

    public static class Tokenizer implements PlaceTokenizer<BooksPlace> {
        @Override
        public String getToken(final BooksPlace place) {

            final BooksSearch bs = place.getBooksSearch();
            return place.getToken(bs);
        }

        @Override
        public BooksPlace getPlace(final String token) {

            final BooksSearch bs = token2search.get(token);
            return new BooksPlace(bs == null ? null : bs.copy());
        }
    }

    public BooksSearch getBooksSearch() {
        return booksSearch;
    }

}
