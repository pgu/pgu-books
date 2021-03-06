package pgu.client.books.edit.ui;

import java.util.ArrayList;

import pgu.client.app.utils.Notification;
import pgu.client.app.utils.NotificationImpl;
import pgu.client.books.edit.EditBookView;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Modal;
import com.github.gwtbootstrap.client.ui.ProgressBar;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.github.gwtbootstrap.client.ui.base.HasVisibleHandlers;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasVisibility;
import com.google.gwt.user.client.ui.Widget;

public class EditBookViewImpl extends Composite implements EditBookView {

    private static EditBookViewImplUiBinder uiBinder = GWT.create(EditBookViewImplUiBinder.class);

    interface EditBookViewImplUiBinder extends UiBinder<Widget, EditBookViewImpl> {
    }

    @UiField
    Modal                                 container;
    @UiField
    TextBox                               titleBox, authorBox, editorBox, categoryBox, yearBox, commentBox;
    @UiField
    Button                                saveBtn, cancelBtn;
    @UiField
    ProgressBar                           progressBar;
    @UiField
    HTMLPanel                             notification;

    private final ArrayList<Notification> notifications = new ArrayList<Notification>();

    public EditBookViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
        progressBar.setVisible(false);
    }

    @Override
    public HasText getBookTitleWidget() {
        return titleBox;
    }

    @Override
    public HasText getBookAuthorWidget() {
        return authorBox;
    }

    @Override
    public HasText getBookCategoryWidget() {
        return categoryBox;
    }

    @Override
    public HasText getBookCommentWidget() {
        return commentBox;
    }

    @Override
    public HasText getBookEditorWidget() {
        return editorBox;
    }

    @Override
    public HasText getBookYearWidget() {
        return yearBox;
    }

    @Override
    public HasClickHandlers getSaveWidget() {
        return saveBtn;
    }

    @Override
    public void show() {
        container.show();
    }

    @Override
    public HasText getFormTitle() {
        return new HasText() {

            @Override
            public void setText(final String text) {
                container.setTitle(text);
            }

            @Override
            public String getText() {
                return container.getTitle();
            }
        };
    }

    @Override
    public HasVisibility getWaitingIndicator() {
        return progressBar;
    }

    @Override
    public Notification newNotification() {
        final NotificationImpl notif = new NotificationImpl(notification, 3000);
        notifications.add(notif);
        return notif;
    }

    @Override
    public HasClickHandlers getCancelWidget() {
        return cancelBtn;
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
    public HasVisibleHandlers getCloseHandler() {
        return container;
    }

    @Override
    public ArrayList<Notification> getNotifications() {
        return notifications;
    }
}
