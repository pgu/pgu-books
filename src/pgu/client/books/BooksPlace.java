package pgu.client.books;

import java.util.ArrayList;

import pgu.shared.dto.BooksSearch;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

public class BooksPlace extends Place {

    private static ArrayList<BooksSearch> booksSearches      = new ArrayList<BooksSearch>();
    private static ArrayList<String>      booksSearchesToken = new ArrayList<String>();

    private final BooksSearch             booksSearch;

    public BooksPlace() {
        booksSearch = null;
    }

    public BooksPlace(final BooksSearch booksSearch) {
        if (booksSearch != null) {

            final BooksSearch copy = booksSearch.copy();
            if (!booksSearches.contains(copy)) {
                booksSearches.add(copy);
                booksSearchesToken.add(getToken(copy));
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
            if (bs == null) {
                return "";
            }

            return place.getToken(bs);
        }

        @Override
        public BooksPlace getPlace(final String token) {

            final int tokenIdx = booksSearchesToken.indexOf(token);
            final BooksSearch bs = booksSearches.get(tokenIdx);
            return new BooksPlace(bs.copy());
        }
    }

    public BooksSearch getBooksSearch() {
        return booksSearch;
    }

}
