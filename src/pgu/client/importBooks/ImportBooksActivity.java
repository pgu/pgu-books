package pgu.client.importBooks;

import pgu.client.app.mvp.ClientFactory;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class ImportBooksActivity extends AbstractActivity {

    private final ImportBooksView view;

    public ImportBooksActivity(final ImportBooksPlace place, final ClientFactory clientFactory) {
        view = clientFactory.getImportBooksView();
    }

    @Override
    public void start(final AcceptsOneWidget panel, final EventBus eventBus) {
        panel.setWidget(view.asWidget());
    }

}
