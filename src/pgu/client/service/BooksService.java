package pgu.client.service;

import pgu.shared.dto.BooksResult;
import pgu.shared.dto.BooksSearch;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("books")
public interface BooksService extends RemoteService {

    BooksResult fetchBooks(BooksSearch booksSearch);
}
