package pgu.client.menu;

import pgu.client.app.event.HideWaitingIndicatorEvent;
import pgu.client.app.event.SearchBooksEvent;
import pgu.client.app.event.ShowWaitingIndicatorEvent;
import pgu.client.menu.ui.MenuViewImpl;
import pgu.shared.dto.BooksSearch;

import com.google.web.bindery.event.shared.EventBus;

public class MenuActivity implements MenuPresenter //
        , ShowWaitingIndicatorEvent.Handler //
        , HideWaitingIndicatorEvent.Handler //
{

    private static MenuView view = new MenuViewImpl();

    private EventBus        eventBus;

    public void start(final EventBus eventBus) {
        this.eventBus = eventBus;
        eventBus.addHandler(ShowWaitingIndicatorEvent.TYPE, this);
        eventBus.addHandler(HideWaitingIndicatorEvent.TYPE, this);
    }

    public MenuView getView() {
        return view;
    }

    @Override
    public void searchBooks(final BooksSearch booksSearch) {
        eventBus.fireEvent(new SearchBooksEvent(booksSearch));
    }

    @Override
    public void onHideWaitingIndicator(final HideWaitingIndicatorEvent event) {
        view.getWaitingIndicator().hide();
    }

    @Override
    public void onShowWaitingIndicator(final ShowWaitingIndicatorEvent event) {
        view.getWaitingIndicator().show();
    }

}
