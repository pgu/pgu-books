package pgu.client.menu;

import pgu.client.app.event.HideWaitingIndicatorEvent;
import pgu.client.app.event.SearchBooksEvent;
import pgu.client.menu.ui.MenuViewImpl;
import pgu.shared.dto.BooksSearch;

import com.google.web.bindery.event.shared.EventBus;

public class MenuActivity implements MenuPresenter //
        , SearchBooksEvent.Handler //
        , HideWaitingIndicatorEvent.Handler //
{

    private static MenuView view = new MenuViewImpl();

    private EventBus        eventBus;

    public void start(final EventBus eventBus) {
        this.eventBus = eventBus;
        eventBus.addHandler(SearchBooksEvent.TYPE, this);
    }

    public MenuView getView() {
        return view;
    }

    @Override
    public void searchBooks(final BooksSearch booksSearch) {
        eventBus.fireEvent(new SearchBooksEvent(booksSearch));
    }

    @Override
    public void onSearchBooks(final SearchBooksEvent event) {
        view.getWaitingIndicator().show();
    }

    @Override
    public void onHideWaitingIndicator(final HideWaitingIndicatorEvent event) {
        view.getWaitingIndicator().hide();
    }

}
