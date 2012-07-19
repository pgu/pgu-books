package pgu.client.app.mvp;

import pgu.client.app.AppView;
import pgu.client.app.utils.AppSetup;
import pgu.client.books.BookView;
import pgu.client.books.BooksView;
import pgu.client.importBooks.ImportBooksView;
import pgu.client.menu.MenuView;
import pgu.client.service.AdminBooksServiceAsync;
import pgu.client.service.BooksServiceAsync;
import pgu.client.service.LoginServiceAsync;
import pgu.client.setup.SetupView;
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

    LoginServiceAsync getLoginService();

    BooksServiceAsync getBooksService();

    AdminBooksServiceAsync getAdminBooksService();

    SetupView getSetupView();

    AppSetup getAppSetup();

    BookView getBookView();

}
