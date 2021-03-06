package pgu.client.books.delete.ui;

import pgu.client.app.utils.HasClickAndVisibility;
import pgu.client.app.utils.Notification;
import pgu.client.app.utils.NotificationImpl;
import pgu.client.books.delete.DeleteBooksView;

import com.github.gwtbootstrap.client.ui.Alert;
import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Modal;
import com.github.gwtbootstrap.client.ui.ProgressBar;
import com.github.gwtbootstrap.client.ui.base.HasVisibleHandlers;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.HasVisibility;
import com.google.gwt.user.client.ui.Widget;

public class DeleteBooksViewImpl extends Composite implements DeleteBooksView {

    private static DeleteBooksViewImplUiBinder uiBinder = GWT.create(DeleteBooksViewImplUiBinder.class);

    interface DeleteBooksViewImplUiBinder extends UiBinder<Widget, DeleteBooksViewImpl> {
    }

    @UiField
    Modal       container;
    @UiField
    Alert       message;
    @UiField
    Button      confirmBtn, cancelBtn;
    @UiField
    ProgressBar progressBar;
    @UiField
    HTMLPanel   notification;

    public DeleteBooksViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
        progressBar.setVisible(false);
    }

    @Override
    public void show() {
        container.show();
    }

    @Override
    public void hide() {
        container.hide();
    }

    @Override
    public void toggle() {
        container.toggle();
    }

    @Override
    public HasClickAndVisibility getConfirmWidget() {
        return new HasClickAndVisibility() {

            @Override
            public boolean isVisible() {
                return confirmBtn.isVisible();
            }

            @Override
            public void setVisible(final boolean visible) {
                confirmBtn.setVisible(visible);
            }

            @Override
            public HandlerRegistration addClickHandler(final ClickHandler handler) {
                return confirmBtn.addClickHandler(handler);
            }

            @Override
            public void fireEvent(final GwtEvent<?> event) {
                confirmBtn.fireEvent(event);
            }

        };
    }

    @Override
    public HasClickAndVisibility getCancelWidget() {
        return new HasClickAndVisibility() {

            @Override
            public boolean isVisible() {
                return cancelBtn.isVisible();
            }

            @Override
            public void setVisible(final boolean visible) {
                cancelBtn.setVisible(visible);
            }

            @Override
            public HandlerRegistration addClickHandler(final ClickHandler handler) {
                return cancelBtn.addClickHandler(handler);
            }

            @Override
            public void fireEvent(final GwtEvent<?> event) {
                cancelBtn.fireEvent(event);
            }

        };
    }

    @Override
    public HasVisibility getWaitingIndicator() {
        return progressBar;
    }

    @Override
    public Notification newNotification() {
        return new NotificationImpl(notification, 3000);
    }

    @Override
    public HasHTML getMessageWidget() {
        return new HasHTML() {

            @Override
            public void setText(final String text) {
                message.setHeading("<i class=\"icon-warning-sign\"></i> Warning<br>");
                message.setText(text);
            }

            @Override
            public String getText() {
                return message.getText();
            }

            @Override
            public void setHTML(final String html) {
                message.setHeading("<i class=\"icon-warning-sign\"></i> Warning<br>");
                message.setHTML(html);
            }

            @Override
            public String getHTML() {
                return message.getHTML();
            }
        };
    }

    @Override
    public HasVisibleHandlers getCloseHandler() {
        return container;
    }

    @Override
    public void clear() {
        notification.clear();
    }

}
