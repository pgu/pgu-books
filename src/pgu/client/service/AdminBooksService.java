package pgu.client.service;

import java.util.ArrayList;

import pgu.shared.domain.Book;
import pgu.shared.domain.ImportResult;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("adminBooks")
public interface AdminBooksService extends RemoteService {

    ImportResult importBooks(int start, int length);

    Book saveBook(Book book);

    void deleteBooks(ArrayList<Book> selectedBooks);

    void deleteAll();

    void updateDocFromIdx();

}
