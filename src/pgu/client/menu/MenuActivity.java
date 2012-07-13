package pgu.client.menu;

import java.util.ArrayList;

import pgu.client.app.event.SearchBooksEvent;
import pgu.shared.dto.BooksSearch;

import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;

public class MenuActivity implements MenuPresenter, SearchBooksEvent.Handler {

    private EventBus                             eventBus;
    private final ArrayList<HandlerRegistration> handlerRegs = new ArrayList<HandlerRegistration>();

    public void start(final EventBus eventBus) {
        this.eventBus = eventBus;
        handlerRegs.add(eventBus.addHandler(SearchBooksEvent.TYPE, this));
    }

    @Override
    public void searchBooks(final BooksSearch booksSearch) {
        eventBus.fireEvent(new SearchBooksEvent(booksSearch));
    }

    public void stop() {
        for (HandlerRegistration handlerReg : handlerRegs) {
            handlerReg.removeHandler();
            handlerReg = null;
        }
        handlerRegs.clear();
    }

    @Override
    public void onSearchBooks(final SearchBooksEvent event) {
        // TODO PGU Jul 13, 2012
    }

}
