package pgu.client.setup;

import pgu.client.app.event.NotificationEvent;
import pgu.client.app.mvp.ClientFactory;
import pgu.client.app.utils.AppSetup;
import pgu.client.app.utils.Level;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class SetupActivity extends AbstractActivity implements SetupPresenter {

    private final SetupView view;
    private final AppSetup  appSetup;
    private EventBus        eventBus;

    public SetupActivity(final SetupPlace place, final ClientFactory clientFactory) {
        view = clientFactory.getSetupView();
        appSetup = clientFactory.getAppSetup();
    }

    @Override
    public void start(final AcceptsOneWidget panel, final EventBus eventBus) {
        this.eventBus = eventBus;
        view.setPresenter(this);
        panel.setWidget(view.asWidget());

        view.getResultsPerPage().setResultsNb(appSetup.getResultsPerPage());
    }

    @Override
    public void updateResultsPerPage(final int resultsPerPage) {
        appSetup.setResultsPerPage(resultsPerPage);
        eventBus.fireEvent(new NotificationEvent(Level.INFO, "Preferencias guardadas"));
    }
}
