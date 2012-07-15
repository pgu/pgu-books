package pgu.client.service;

import java.util.ArrayList;

import pgu.shared.domain.ImportResult;
import pgu.shared.dto.Book;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface AdminBooksServiceAsync {

    void importBooks(int start, int length, AsyncCallback<ImportResult> callback);

    void saveBook(Book book, AsyncCallback<Void> callback);

    void deleteBooks(ArrayList<Book> selectedBooks, AsyncCallback<Void> callback);

    void deleteAll(AsyncCallback<Void> callback);

}
