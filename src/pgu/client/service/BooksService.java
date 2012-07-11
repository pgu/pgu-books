package pgu.client.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("books")
public interface BooksService extends RemoteService {
    String greetServer(String name);
}
