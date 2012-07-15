package pgu.client.app.ui;

import pgu.client.app.AppView;
import pgu.client.app.utils.Level;
import pgu.client.app.utils.Notification;

import com.github.gwtbootstrap.client.ui.AlertBlock;
import com.github.gwtbootstrap.client.ui.constants.AlertType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class AppViewImpl extends Composite implements AppView {

    private static AppViewImplUiBinder uiBinder = GWT.create(AppViewImplUiBinder.class);

    interface AppViewImplUiBinder extends UiBinder<Widget, AppViewImpl> {
    }

    @UiField
    AlertBlock notification;
    @UiField
    SimplePanel header, body;

    public AppViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
        notification.close();
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
    public Notification getNotification() {
        return new Notification() {

            private boolean hasCloseAction = false;

            @Override
            public String getText() {
                return notification.getText();
            }

            @Override
            public void setText(final String text) {
                notification.setText(text);
            }

            @Override
            public void show() {
                notification.setVisible(true);
                if (!hasCloseAction) {
                    addTimerForClosingNotification();
                }
            }

            @Override
            public void hide() {
                notification.close();
            }

            @Override
            public void toggle() {
                throw new UnsupportedOperationException();
            }

            @Override
            public void setLevel(final Level level) {
                if (Level.ERROR == level) {
                    notification.setType(AlertType.ERROR);
                    notification.setClose(true);
                    hasCloseAction = true;

                } else if (Level.INFO == level) {
                    notification.setType(AlertType.INFO);
                    notification.setClose(false);
                    hasCloseAction = false;

                } else if (Level.SUCCESS == level) {
                    notification.setType(AlertType.SUCCESS);
                    notification.setClose(false);
                    hasCloseAction = false;

                } else if (Level.WARNING == level) {
                    notification.setType(AlertType.WARNING);
                    notification.setClose(false);
                    hasCloseAction = false;

                } else {
                    notification.setType(AlertType.DEFAULT);
                    notification.setClose(false);
                    hasCloseAction = false;

                }
            }

            @Override
            public void setHeading(final String heading) {
                notification.setHeading(heading);
            }

        };
    }

    private void addTimerForClosingNotification() {
        new Timer() {

            @Override
            public void run() {
                notification.close();
            }

        }.schedule(700);
    }
}
