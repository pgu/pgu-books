package pgu.client.books.list;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

public class ListBooksPlace extends Place {

    private final String searchHashcode;
    private final int    page;

    public ListBooksPlace() {
        searchHashcode = "";
        page = 0;
    }

    public ListBooksPlace(final String searchHashcode, final int page) {
        this.searchHashcode = searchHashcode;
        this.page = page;
    }

    public static class Tokenizer implements PlaceTokenizer<ListBooksPlace> {

        @Override
        public String getToken(final ListBooksPlace place) {
            return "h:" + place.searchHashcode + "&p:" + place.page;
        }

        @Override
        public ListBooksPlace getPlace(final String token) {
            final String[] parts = token.split("&");

            if (!token.contains("h:") //
                    && !token.contains("p:") //
                    && parts.length != 2 //
            ) {
                return new ListBooksPlace();
            }

            String hashcode, page;
            if (parts[0].contains("h:")) {
                hashcode = parts[0].substring(2);
                page = parts[1].substring(2);
            } else {
                hashcode = parts[1].substring(2);
                page = parts[0].substring(2);
            }

            return new ListBooksPlace(hashcode, Integer.valueOf(page));
        }
    }

    public String getSearchHashcode() {
        return searchHashcode;
    }

    public int getPage() {
        return page;
    }

}
