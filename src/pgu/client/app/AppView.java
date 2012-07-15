package pgu.client.app;

import pgu.client.app.utils.Notification;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

public interface AppView extends IsWidget //
        , AcceptsOneWidget //
{

    AcceptsOneWidget getHeader();

    AcceptsOneWidget getBody();

    Notification newNotification();
}
