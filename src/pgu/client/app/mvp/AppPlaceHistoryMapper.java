package pgu.client.app.mvp;

import pgu.client.books.BooksPlace;

import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.place.shared.WithTokenizers;

@WithTokenizers({
//
BooksPlace.Tokenizer.class

})
public interface AppPlaceHistoryMapper extends PlaceHistoryMapper {

}
