package pgu.client.menu;

import pgu.client.app.event.SearchBooksEvent;
import pgu.client.menu.ui.MenuViewImpl;
import pgu.shared.dto.BooksSearch;

import com.google.web.bindery.event.shared.EventBus;

public class MenuActivity implements MenuPresenter {

    private static MenuView menuView = new MenuViewImpl();

    private EventBus        eventBus;

    public void start(final EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public MenuView getView() {
        return menuView;
    }

    @Override
    public void searchBooks(final BooksSearch booksSearch) {
        eventBus.fireEvent(new SearchBooksEvent(booksSearch));
    }

    // TODO PGU Jul 13, 2012 hashandler on searchbooksevent: show progressbar
}
