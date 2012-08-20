package pgu.client.service;

import pgu.shared.domain.BooksCount;
import pgu.shared.dto.BooksResult;
import pgu.shared.dto.BooksSearch;
import pgu.shared.dto.SuggestionsResult;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("books")
public interface BooksService extends RemoteService {

    BooksResult fetchBooks(BooksSearch booksSearch, int page, String cursor);

    SuggestionsResult searchSuggestions(String text);

    BooksCount getBooksCount();

    SuggestionsResult searchTagSuggestions();
}
