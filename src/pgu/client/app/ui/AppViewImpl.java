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
        return new Notification() {

            private boolean    hasCloseAction = false;
            private String     heading;
            private String     text;
            private AlertBlock alert;

            @Override
            public String getHTML() {
                return text;
            }

            @Override
            public void setHTML(final String text) {
                this.text = text;
            }

            @Override
            public void show() {
                notification.add(alert);

                if (!hasCloseAction) {
                    addTimerForClosingNotification();
                }
            }

            @Override
            public void hide() {
                alert.close();
            }

            @Override
            public void toggle() {
                throw new UnsupportedOperationException();
            }

            @Override
            public void setLevel(final Level level) {

                String prefixHeading = "";
                if (Level.ERROR == level) {
                    alert = new AlertBlock(AlertType.ERROR);
                    alert.setClose(true);
                    hasCloseAction = true;

                } else if (Level.INFO == level) {
                    alert = new AlertBlock(AlertType.INFO);
                    alert.setClose(false);
                    hasCloseAction = false;

                } else if (Level.SUCCESS == level) {
                    alert = new AlertBlock(AlertType.SUCCESS);
                    alert.setClose(false);
                    hasCloseAction = false;
                    prefixHeading = "<i class=\"icon-thumbs-up\"></i> ";

                } else if (Level.WARNING == level) {
                    alert = new AlertBlock(AlertType.WARNING);
                    alert.setClose(false);
                    hasCloseAction = false;

                } else {
                    alert = new AlertBlock(AlertType.DEFAULT);
                    alert.setClose(false);
                    hasCloseAction = false;

                }
                alert.setAnimation(true);
                alert.setHTML(text);
                alert.setHeading(prefixHeading + heading);
            }

            @Override
            public void setHeading(final String heading) {
                this.heading = heading;
            }

            private void addTimerForClosingNotification() {
                new Timer() {

                    @Override
                    public void run() {
                        alert.close();
                    }

                }.schedule(5000);
            }

            @Override
            public String getText() {
                return text;
            }

            @Override
            public void setText(final String text) {
                this.text = text;
            }
        };
    }

}
