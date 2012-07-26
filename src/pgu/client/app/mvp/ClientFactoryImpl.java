package pgu.client.app.mvp;

import pgu.client.app.AppState;
import pgu.client.app.AppView;
import pgu.client.app.ui.AppViewImpl;
import pgu.client.books.delete.DeleteBooksView;
import pgu.client.books.delete.ui.DeleteBooksViewImpl;
import pgu.client.books.edit.EditBookView;
import pgu.client.books.edit.ui.EditBookViewImpl;
import pgu.client.books.list.ListBooksView;
import pgu.client.books.list.ui.ListBooksViewImpl;
import pgu.client.books.upload.ImportBooksView;
import pgu.client.books.upload.ui.ImportBooksViewImpl;
import pgu.client.menu.MenuView;
import pgu.client.menu.ui.MenuViewImpl;
import pgu.client.service.AdminBooksService;
import pgu.client.service.AdminBooksServiceAsync;
import pgu.client.service.BooksService;
import pgu.client.service.BooksServiceAsync;
import pgu.client.service.LoginService;
import pgu.client.service.LoginServiceAsync;
import pgu.client.setup.SetupView;
import pgu.client.setup.ui.SetupViewImpl;
import pgu.shared.dto.LoginInfo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;

public class ClientFactoryImpl implements ClientFactory {

    private static EventBus               eventBus          = new SimpleEventBus();
    private static PlaceController        placeController   = new PlaceController(eventBus);
    private static AppView                appView           = new AppViewImpl();
    private static MenuView               menuView          = new MenuViewImpl();
    private static ListBooksView              booksView         = new ListBooksViewImpl();
    private static EditBookView               bookView          = new EditBookViewImpl();
    private static DeleteBooksView        deleteBooksView   = new DeleteBooksViewImpl();
    private static ImportBooksView        importBooksView   = new ImportBooksViewImpl();
    private static SetupView              setupView         = new SetupViewImpl();

    private static BooksServiceAsync      booksService      = GWT.create(BooksService.class);
    private static LoginServiceAsync      loginService      = GWT.create(LoginService.class);
    private static AdminBooksServiceAsync adminBooksService = GWT.create(AdminBooksService.class);

    private static AppState               appState          = new AppState();

    private LoginInfo                     loginInfo;

    @Override
    public EventBus getEventBus() {
        return eventBus;
    }

    @Override
    public PlaceController getPlaceController() {
        return placeController;
    }

    @Override
    public ListBooksView getBooksView() {
        return booksView;
    }

    @Override
    public AppView getAppView() {
        return appView;
    }

    @Override
    public MenuView getMenuView() {
        return menuView;
    }

    @Override
    public LoginInfo getLoginInfo() {
        return loginInfo;
    }

    @Override
    public void setLoginInfo(final LoginInfo loginInfo) {
        this.loginInfo = loginInfo;
    }

    @Override
    public ImportBooksView getImportBooksView() {
        return importBooksView;
    }

    @Override
    public BooksServiceAsync getBooksService() {
        return booksService;
    }

    @Override
    public LoginServiceAsync getLoginService() {
        return loginService;
    }

    @Override
    public AdminBooksServiceAsync getAdminBooksService() {
        return adminBooksService;
    }

    @Override
    public SetupView getSetupView() {
        return setupView;
    }

    @Override
    public AppState getAppState() {
        return appState;
    }

    @Override
    public EditBookView getBookView() {
        return bookView;
    }

    @Override
    public DeleteBooksView getDeleteBooksView() {
        return deleteBooksView;
    }

}
