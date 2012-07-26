package pgu.client.setup;

import pgu.client.app.event.NotificationEvent;
import pgu.client.app.mvp.ClientFactory;
import pgu.client.app.utils.ClientUtils;
import pgu.client.app.utils.Level;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class SetupActivity extends AbstractActivity implements SetupPresenter {

    private final SetupView     view;
    private EventBus            eventBus;
    private final ClientFactory clientFactory;
    private final ClientUtils   u = new ClientUtils();

    public SetupActivity(final SetupPlace place, final ClientFactory clientFactory) {
        view = clientFactory.getSetupView();
        this.clientFactory = clientFactory;
    }

    @Override
    public void start(final AcceptsOneWidget panel, final EventBus eventBus) {
        this.eventBus = eventBus;
        view.setPresenter(this);
        panel.setWidget(view.asWidget());

        view.getResultsPerPage().setResultsNb(clientFactory.getAppState().getResultsPerPage());
    }

    @Override
    public void updateResultsPerPage(final int resultsPerPage) {
        // appSetup.setResultsPerPage(resultsPerPage);
        u.fire(eventBus, new NotificationEvent(Level.INFO, "Preferencias guardadas"));
    }
}
