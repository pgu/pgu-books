package pgu.client.books;

import pgu.client.app.utils.Notification;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasVisibility;
import com.google.gwt.user.client.ui.IsWidget;

public interface BookView extends IsWidget {

    HasText getBookTitleWidget();

    HasText getBookAuthorWidget();

    HasText getBookCategoryWidget();

    HasText getBookCommentWidget();

    HasText getBookEditorWidget();

    HasText getBookYearWidget();

    HasText getFormTitle();

    HasClickHandlers getSaveWidget();

    void show();

    HasVisibility getWaitingIndicator();

    Notification newNotification();

    HasClickHandlers getCancelWidget();

    HasClickHandlers getCloseHandler();

}
