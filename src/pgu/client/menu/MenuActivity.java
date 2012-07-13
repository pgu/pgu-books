package pgu.client.menu;

import pgu.client.app.event.HideWaitingIndicatorEvent;
import pgu.client.app.event.SearchBooksEvent;
import pgu.client.app.event.ShowWaitingIndicatorEvent;
import pgu.shared.dto.BooksSearch;

import com.google.web.bindery.event.shared.EventBus;

public class MenuActivity implements MenuPresenter //
        , ShowWaitingIndicatorEvent.Handler //
        , HideWaitingIndicatorEvent.Handler //
{

    private final MenuView view;
    private EventBus       eventBus;

    public MenuActivity(final MenuView view) {
        this.view = view;
    }

    public void start(final EventBus eventBus) {
        this.eventBus = eventBus;
        view.setPresenter(this);
        eventBus.addHandler(ShowWaitingIndicatorEvent.TYPE, this);
        eventBus.addHandler(HideWaitingIndicatorEvent.TYPE, this);
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
