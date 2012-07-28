package pgu.client.app.utils;

import com.github.gwtbootstrap.client.ui.base.HasVisibility;
import com.google.gwt.user.client.ui.HasHTML;

public interface Notification extends //
        HasHTML //
        , HasVisibility //
{
    void setLevel(Level level);

    void setHeading(String heading);

    void removeFromParent();
}
