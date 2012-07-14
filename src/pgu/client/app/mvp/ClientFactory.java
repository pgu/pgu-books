package pgu.client.app.mvp;

import pgu.client.app.AppView;
import pgu.client.books.BooksView;
import pgu.client.importBooks.ImportBooksView;
import pgu.client.menu.MenuView;
import pgu.shared.dto.LoginInfo;

import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;

public interface ClientFactory {

    EventBus getEventBus();

    PlaceController getPlaceController();

    LoginInfo getLoginInfo();

    void setLoginInfo(LoginInfo loginInfo);

    BooksView getBooksView();

    AppView getAppView();

    MenuView getMenuView();

    ImportBooksView getImportBooksView();
}
