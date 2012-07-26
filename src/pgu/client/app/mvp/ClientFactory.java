package pgu.client.app.mvp;

import pgu.client.app.AppState;
import pgu.client.app.AppView;
import pgu.client.books.delete.DeleteBooksView;
import pgu.client.books.edit.EditBookView;
import pgu.client.books.list.ListBooksView;
import pgu.client.books.upload.ImportBooksView;
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

    ListBooksView getBooksView();

    AppView getAppView();

    MenuView getMenuView();

    ImportBooksView getImportBooksView();

    LoginServiceAsync getLoginService();

    BooksServiceAsync getBooksService();

    AdminBooksServiceAsync getAdminBooksService();

    SetupView getSetupView();

    EditBookView getBookView();

    DeleteBooksView getDeleteBooksView();

    AppState getAppState();

}
