package pgu.client.app.ui;

import pgu.client.app.AppView;
import pgu.client.app.utils.Notification;
import pgu.client.app.utils.NotificationImpl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class AppViewImpl extends Composite implements AppView {

    private static AppViewImplUiBinder uiBinder = GWT.create(AppViewImplUiBinder.class);

    interface AppViewImplUiBinder extends UiBinder<Widget, AppViewImpl> {
    }

    @UiField
    SimplePanel header, body;
    @UiField
    HTMLPanel   notification;

    public AppViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public void setWidget(final IsWidget w) {
        body.setWidget(w);
    }

    @Override
    public AcceptsOneWidget getHeader() {
        return header;
    }

    @Override
    public AcceptsOneWidget getBody() {
        return body;
    }

    @Override
    public Notification newNotification() {
        return new NotificationImpl(notification);
    }

}
