package pgu.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface BooksServiceAsync {
    void greetServer(String input, AsyncCallback<String> callback);
}
