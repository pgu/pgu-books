package pgu.client.app.mvp;

import pgu.client.books.BooksActivity;
import pgu.client.books.BooksPlace;
import pgu.client.importBooks.ImportBooksActivity;
import pgu.client.importBooks.ImportBooksPlace;
import pgu.client.setup.SetupActivity;
import pgu.client.setup.SetupPlace;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;

public class AppActivityMapper implements ActivityMapper {

    private final ClientFactory clientFactory;

    public AppActivityMapper(final ClientFactory clientFactory) {
        this.clientFactory = clientFactory;
    }

    @Override
    public Activity getActivity(final Place place) {
        if (place instanceof BooksPlace) {
            return new BooksActivity((BooksPlace) place, clientFactory);

        } else if (place instanceof ImportBooksPlace) {
            return new ImportBooksActivity((ImportBooksPlace) place, clientFactory);

        } else if (place instanceof SetupPlace) {
            return new SetupActivity((SetupPlace) place, clientFactory);

        }

        return null;
    }

}
