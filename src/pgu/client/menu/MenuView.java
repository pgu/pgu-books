package pgu.client.menu;

import com.github.gwtbootstrap.client.ui.base.HasVisibility;
import com.google.gwt.user.client.ui.IsWidget;

public interface MenuView extends IsWidget {

    void setPresenter(MenuPresenter presenter);

    HasVisibility getWaitingIndicator();

}
