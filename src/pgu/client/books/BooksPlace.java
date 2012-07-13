package pgu.client.books;

import pgu.shared.dto.BooksSearch;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

public class BooksPlace extends Place {

    private final BooksSearch booksSearch;

    public BooksPlace() {
        booksSearch = null;
    }

    public BooksPlace(final BooksSearch booksSearch) {
        this.booksSearch = booksSearch;
    }

    public static class Tokenizer implements PlaceTokenizer<BooksPlace> {
        @Override
        public String getToken(final BooksPlace place) {
            return "";
        }

        @Override
        public BooksPlace getPlace(final String token) {
            return new BooksPlace();
        }
    }

    public BooksSearch getBooksSearch() {
        return booksSearch;
    }

}
