package pgu.client.app;

import com.google.gwt.core.client.GWT;

public class AppActivity {

    private final AppView view = GWT.create(AppView.class);

    public AppView getView() {
        return view;
    }

}
