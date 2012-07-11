package pgu.client.books;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

public class BooksPlace extends Place {

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
}
