package pgu.client.service;

import java.util.ArrayList;

import pgu.shared.domain.Book;
import pgu.shared.domain.ImportResult;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface AdminBooksServiceAsync {

    void importBooks(int start, int length, AsyncCallback<ImportResult> callback);

    void saveBook(Book book, AsyncCallback<Book> callback);

    void deleteBooks(ArrayList<Book> selectedBooks, AsyncCallback<Void> callback);

    void deleteAll(AsyncCallback<Void> callback);

    void updateDocFromIdx(AsyncCallback<Void> asyncCallback);

}
