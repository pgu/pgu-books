package pgu.client.setup;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

public class SetupPlace extends Place {

    public static class Tokenizer implements PlaceTokenizer<SetupPlace> {
        @Override
        public String getToken(final SetupPlace place) {
            return "";
        }

        @Override
        public SetupPlace getPlace(final String token) {
            return new SetupPlace();
        }
    }

}
