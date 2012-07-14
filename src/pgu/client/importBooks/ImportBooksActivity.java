package pgu.client.importBooks;

import pgu.client.app.event.HideWaitingIndicatorEvent;
import pgu.client.app.event.InformationEvent;
import pgu.client.app.event.ShowWaitingIndicatorEvent;
import pgu.client.app.mvp.ClientFactory;
import pgu.client.app.utils.AsyncCallbackApp;
import pgu.client.service.AdminBooksServiceAsync;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class ImportBooksActivity extends AbstractActivity implements ImportBooksPresenter {

    private final ImportBooksView        view;
    private final AdminBooksServiceAsync adminBooksService;
    private EventBus                     eventBus;

    public ImportBooksActivity(final ImportBooksPlace place, final ClientFactory clientFactory) {
        view = clientFactory.getImportBooksView();
        adminBooksService = clientFactory.getAdminBooksService();
    }

    @Override
    public void start(final AcceptsOneWidget panel, final EventBus eventBus) {
        this.eventBus = eventBus;
        view.setPresenter(this);
        panel.setWidget(view.asWidget());
    }

    @Override
    public void importBooks(final int start, final int length) {
        eventBus.fireEvent(new ShowWaitingIndicatorEvent());
        adminBooksService.importBooks(start, length, new AsyncCallbackApp<String>(eventBus) {

            @Override
            public void onSuccess(final String result) {
                eventBus.fireEvent(new HideWaitingIndicatorEvent());
                eventBus.fireEvent(new InformationEvent(result));
            }

        });
    }

}
