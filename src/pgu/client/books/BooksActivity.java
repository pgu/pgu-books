package pgu.client.books;

import pgu.client.app.mvp.ClientFactory;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class BooksActivity extends AbstractActivity {

    private final ClientFactory clientFactory;

    public BooksActivity(final BooksPlace place, final ClientFactory clientFactory) {
        this.clientFactory = clientFactory;
    }

    @Override
    public void start(final AcceptsOneWidget panel, final EventBus eventBus) {
        final BooksView booksView = clientFactory.getBooksView();
        panel.setWidget(booksView.asWidget());
    }

}
