package pgu.client.app.utils;

import com.github.gwtbootstrap.client.ui.AlertBlock;
import com.github.gwtbootstrap.client.ui.constants.AlertType;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HTMLPanel;

public class NotificationImpl implements Notification {

    private final int       timeInMs;
    private boolean         hasCloseAction = false;
    private String          heading;
    private String          text;
    private AlertBlock      alert;
    private final HTMLPanel container;

    public NotificationImpl(final HTMLPanel container) {
        this(container, 5000);
    }

    public NotificationImpl(final HTMLPanel container, final int timeInMs) {
        this.timeInMs = timeInMs;
        this.container = container;
    }

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
        container.add(alert);

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

        String iconHeading = "";
        if (Level.ERROR == level) {
            alert = new AlertBlock(AlertType.ERROR);
            alert.setClose(true);
            hasCloseAction = true;
            iconHeading = "icon-bolt";

        } else if (Level.INFO == level) {
            alert = new AlertBlock(AlertType.INFO);
            alert.setClose(false);
            hasCloseAction = false;
            iconHeading = "icon-info-sign";

        } else if (Level.SUCCESS == level) {
            alert = new AlertBlock(AlertType.SUCCESS);
            alert.setClose(false);
            hasCloseAction = false;
            iconHeading = "icon-thumbs-up";

        } else if (Level.WARNING == level) {
            alert = new AlertBlock(AlertType.WARNING);
            alert.setClose(false);
            hasCloseAction = false;
            iconHeading = "icon-warning-sign";

        } else {
            alert = new AlertBlock(AlertType.DEFAULT);
            alert.setClose(false);
            hasCloseAction = false;
            iconHeading = "icon-exclamation-sign";

        }
        alert.setAnimation(true);
        alert.setHTML(text);
        alert.setHeading("<i class=\"" + iconHeading + "\"></i> " + heading);
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

        }.schedule(timeInMs);
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public void setText(final String text) {
        this.text = text;
    }

}
