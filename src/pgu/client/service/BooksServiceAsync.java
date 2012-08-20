package pgu.client.service;

import pgu.shared.domain.BooksCount;
import pgu.shared.dto.BooksResult;
import pgu.shared.dto.BooksSearch;
import pgu.shared.dto.SuggestionsResult;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface BooksServiceAsync {

    void fetchBooks(BooksSearch booksSearch, int page, String cursor, AsyncCallback<BooksResult> callback);

    void searchSuggestions(String text, AsyncCallback<SuggestionsResult> asyncCallbackApp);

    void getBooksCount(AsyncCallback<BooksCount> asyncCallbackApp);

    void searchTagSuggestions(AsyncCallback<SuggestionsResult> asyncCallbackApp);

}
