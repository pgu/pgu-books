package pgu.server.service;

import pgu.client.service.BooksService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class BooksServiceImpl extends RemoteServiceServlet implements BooksService {

    @Override
    public String greetServer(final String input) {
        return "Hello";
    }

}
