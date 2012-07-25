package pgu.client.books;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

public class BooksPlace extends Place {

    private final String searchHashcode;
    private final int    page;

    public BooksPlace() {
        searchHashcode = "";
        page = 0;
    }

    public BooksPlace(final String searchHashcode, final int page) {
        this.searchHashcode = searchHashcode;
        this.page = page;
    }

    public static class Tokenizer implements PlaceTokenizer<BooksPlace> {

        @Override
        public String getToken(final BooksPlace place) {
            return "h:" + place.searchHashcode + "&p:" + place.page;
        }

        @Override
        public BooksPlace getPlace(final String token) {
            final String[] parts = token.split("&");

            if (!token.contains("h:") //
                    && !token.contains("p:") //
                    && parts.length != 2 //
            ) {
                return new BooksPlace();
            }

            String hashcode, page;
            if (parts[0].contains("h:")) {
                hashcode = parts[0];
                page = parts[1];
            } else {
                hashcode = parts[1];
                page = parts[0];
            }

            return new BooksPlace(hashcode, Integer.valueOf(page));
        }
    }

}
