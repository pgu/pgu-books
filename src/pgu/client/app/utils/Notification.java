package pgu.client.app.utils;

import com.github.gwtbootstrap.client.ui.base.HasVisibility;
import com.google.gwt.user.client.ui.HasText;

public interface Notification extends //
        HasText //
        , HasVisibility //
{
    void setLevel(Level level);

    void setHeading(String heading);
}
