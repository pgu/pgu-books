package pgu.client.menu;

import java.util.ArrayList;
import java.util.Date;

import pgu.shared.dto.Suggestion;

import com.github.gwtbootstrap.client.ui.base.HasHref;
import com.github.gwtbootstrap.client.ui.base.HasVisibility;
import com.google.gwt.user.client.ui.IsWidget;

public interface MenuView extends IsWidget {

    interface LogWidget extends HasVisibility, HasHref {
    }

    interface SuggestionsWidget extends HasVisibility {

        void setSuggestions(ArrayList<Suggestion> suggestions);
    }

    interface WaitingWidget extends HasVisibility {

        boolean isVisible();

    }

    void setPresenter(MenuPresenter presenter);

    WaitingWidget getWaitingIndicator();

    LogWidget getLoginWidget();

    LogWidget getLogoutWidget();

    HasVisibility getImportWidget();

    HasVisibility getLibraryWidget();

    HasVisibility getAppstatsWidget();

    SuggestionsWidget getSuggestionsWidget();

    String getFilterAuthor();

    String getFilterCategory();

    String getFilterComment();

    String getFilterEditor();

    String getFilterTitle();

    String getFilterYear();

    interface BooksCountWidget {

        void hide();

        void setCount(int count, Date lastCountDate);
    }

    BooksCountWidget getBooksCountWidget();

    void showMessageMoreThanOneThousand(boolean shouldShowMessage);

}
