package pgu.client.service;

import java.util.ArrayList;

import pgu.shared.domain.Book;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("adminBooks")
public interface AdminBooksService extends RemoteService {

    String importBooks(int start, int length);

    void saveBook(Book book);

    void deleteBooks(ArrayList<Book> selectedBooks);

    void deleteAll();

}
