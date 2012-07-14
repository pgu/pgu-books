package pgu.client.importBooks;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

public class ImportBooksPlace extends Place {

    public static class Tokenizer implements PlaceTokenizer<ImportBooksPlace> {
        @Override
        public String getToken(final ImportBooksPlace place) {
            return "";
        }

        @Override
        public ImportBooksPlace getPlace(final String token) {
            return new ImportBooksPlace();
        }
    }

}
