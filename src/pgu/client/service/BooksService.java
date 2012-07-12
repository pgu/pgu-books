package pgu.client.service;

import pgu.shared.dto.BooksQueryParameters;
import pgu.shared.dto.BooksResult;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("books")
public interface BooksService extends RemoteService {

    BooksResult fetchBooks(BooksQueryParameters queryParameters, int start, int length);
}
