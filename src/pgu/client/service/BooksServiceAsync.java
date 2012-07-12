package pgu.client.service;

import pgu.shared.dto.BooksQueryParameters;
import pgu.shared.dto.BooksResult;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface BooksServiceAsync {
    void fetchBooks(final BooksQueryParameters queryParameters, final int start, final int length, AsyncCallback<BooksResult> callback);
}
