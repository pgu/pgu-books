package pgu.client.books;

import pgu.client.app.utils.Notification;

import com.github.gwtbootstrap.client.ui.base.HasVisibleHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasVisibility;

public interface BookView extends com.github.gwtbootstrap.client.ui.base.HasVisibility {

    HasText getBookTitleWidget();

    HasText getBookAuthorWidget();

    HasText getBookCategoryWidget();

    HasText getBookCommentWidget();

    HasText getBookEditorWidget();

    HasText getBookYearWidget();

    HasText getFormTitle();

    HasClickHandlers getSaveWidget();

    HasVisibility getWaitingIndicator();

    Notification newNotification();

    HasClickHandlers getCancelWidget();

    HasVisibleHandlers getCloseHandler();

}
