package pgu.client.service;

import pgu.shared.dto.BooksResult;
import pgu.shared.dto.BooksSearch;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface BooksServiceAsync {

    void fetchBooks(BooksSearch booksSearch, AsyncCallback<BooksResult> callback);

}
