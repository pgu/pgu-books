package pgu.client.books.delete;

import pgu.client.app.utils.HasClickAndVisibility;
import pgu.client.app.utils.Notification;

import com.github.gwtbootstrap.client.ui.base.HasVisibleHandlers;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.HasVisibility;

public interface DeleteBooksView extends com.github.gwtbootstrap.client.ui.base.HasVisibility {

    HasClickAndVisibility getConfirmWidget();

    HasClickAndVisibility getCancelWidget();

    HasVisibility getWaitingIndicator();

    Notification newNotification();

    HasHTML getMessageWidget();

    HasVisibleHandlers getCloseHandler();

    void clear();

}
