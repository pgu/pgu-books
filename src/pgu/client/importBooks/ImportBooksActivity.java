package pgu.client.importBooks;

import pgu.client.app.event.HideWaitingIndicatorEvent;
import pgu.client.app.event.NotificationEvent;
import pgu.client.app.event.ShowWaitingIndicatorEvent;
import pgu.client.app.mvp.ClientFactory;
import pgu.client.app.utils.AsyncCallbackApp;
import pgu.client.app.utils.Level;
import pgu.client.service.AdminBooksServiceAsync;
import pgu.shared.domain.ImportResult;

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
        adminBooksService.importBooks(start, length, new AsyncCallbackApp<ImportResult>(eventBus) {

            @Override
            public void onSuccess(final ImportResult result) {
                final boolean isSuccess = result.getMisseds().isEmpty();
                if (isSuccess) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("All the books have been imported");
                    sb.append("<br> .start " + result.getStart());
                    sb.append("<br> .length " + result.getLength());
                    sb.append("<br> .lastbook " + result.getLastBook());

                    eventBus.fireEvent(new NotificationEvent(Level.SUCCESS, sb.toString()));
                } else {
                    eventBus.fireEvent(new NotificationEvent(Level.WARNING, "Not all the books have been imported"));
                }

                eventBus.fireEvent(new HideWaitingIndicatorEvent());
            }

        });
    }

    @Override
    public void display(final Level level, final String text) {
        eventBus.fireEvent(new NotificationEvent(level, text));
    }

}
