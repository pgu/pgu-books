package pgu.client.deleteBooks;

import pgu.client.app.utils.Notification;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.HasVisibility;

public interface DeleteBooksView extends com.github.gwtbootstrap.client.ui.base.HasVisibility {

    HasClickHandlers getConfirmWidget();

    HasClickHandlers getCancelWidget();

    HasVisibility getWaitingIndicator();

    Notification newNotification();

    HasHTML getMessageWidget();

}
