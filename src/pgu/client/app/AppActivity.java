package pgu.client.app;

import pgu.client.app.ui.AppViewImpl;
import pgu.client.menu.MenuActivity;

import com.google.web.bindery.event.shared.EventBus;

public class AppActivity {

    private static AppView appView = new AppViewImpl();

    public AppView startAndGetView(final EventBus eventBus) {

        final MenuActivity menuActivity = new MenuActivity();
        menuActivity.start(eventBus);

        appView.getHeader().setWidget(menuActivity.getView());

        return appView;
    }

}
