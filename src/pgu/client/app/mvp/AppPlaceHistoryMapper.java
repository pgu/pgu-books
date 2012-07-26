package pgu.client.app.mvp;

import pgu.client.books.list.ListBooksPlace;
import pgu.client.books.upload.ImportBooksPlace;
import pgu.client.setup.SetupPlace;

import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.place.shared.WithTokenizers;

@WithTokenizers({
//
        ListBooksPlace.Tokenizer.class //
        , ImportBooksPlace.Tokenizer.class //
        , SetupPlace.Tokenizer.class //

})
public interface AppPlaceHistoryMapper extends PlaceHistoryMapper {

}
