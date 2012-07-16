package pgu.client.setup;

import pgu.client.app.mvp.ClientFactory;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class SetupActivity extends AbstractActivity implements SetupPresenter {

    private final SetupView view;

    public SetupActivity(final SetupPlace place, final ClientFactory clientFactory) {
        view = clientFactory.getSetupView();
    }

    @Override
    public void start(final AcceptsOneWidget panel, final EventBus eventBus) {
        view.setPresenter(this);
        panel.setWidget(view.asWidget());
    }

}
