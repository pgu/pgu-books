package pgu.client.menu;

import pgu.client.app.event.HideWaitingIndicatorEvent;
import pgu.client.app.event.ImportBooksEvent;
import pgu.client.app.event.SetupEvent;
import pgu.client.app.event.ShowWaitingIndicatorEvent;
import pgu.client.app.mvp.ClientFactory;
import pgu.client.app.utils.AsyncCallbackApp;
import pgu.client.app.utils.SearchUtils;
import pgu.client.service.BooksServiceAsync;
import pgu.shared.dto.BooksSearch;
import pgu.shared.dto.LoginInfo;
import pgu.shared.dto.SuggestionsResult;

import com.google.web.bindery.event.shared.EventBus;

public class MenuActivity implements MenuPresenter //
        , ShowWaitingIndicatorEvent.Handler //
        , HideWaitingIndicatorEvent.Handler //
{

    private final MenuView          view;
    private EventBus                eventBus;
    private final LoginInfo         loginInfo;
    private final SearchUtils       u;
    private final BooksServiceAsync booksService;

    public MenuActivity(final MenuView view, final ClientFactory clientFactory) {
        this.view = view;
        loginInfo = clientFactory.getLoginInfo();
        u = new SearchUtils(clientFactory);
        booksService = clientFactory.getBooksService();
    }

    public void start(final EventBus eventBus) {
        this.eventBus = eventBus;
        view.setPresenter(this);
        eventBus.addHandler(ShowWaitingIndicatorEvent.TYPE, this);
        eventBus.addHandler(HideWaitingIndicatorEvent.TYPE, this);

        if (loginInfo.isLoggedIn()) {

            view.getLoginWidget().hide();

            view.getLogoutWidget().setHref(loginInfo.getLogoutUrl());
            view.getLogoutWidget().show();

            view.getImportWidget().show();
            view.getSearchWidget().show();
            view.getAppstatsWidget().show();

        } else {
            view.getLoginWidget().setHref(loginInfo.getLoginUrl());
            view.getLoginWidget().show();

            view.getLogoutWidget().hide();
            view.getImportWidget().hide();
            view.getSearchWidget().hide();
            view.getAppstatsWidget().hide();
        }

    }

    @Override
    public void searchBooks(final BooksSearch booksSearch) {
        eventBus.fireEvent(u.newSearchEvent(booksSearch));
    }

    @Override
    public void onHideWaitingIndicator(final HideWaitingIndicatorEvent event) {
        view.getWaitingIndicator().hide();
    }

    @Override
    public void onShowWaitingIndicator(final ShowWaitingIndicatorEvent event) {
        view.getWaitingIndicator().show();
    }

    @Override
    public void importBooks() {
        eventBus.fireEvent(new ImportBooksEvent());
    }

    @Override
    public void goToSetup() {
        eventBus.fireEvent(new SetupEvent());
    }

    @Override
    public void searchSuggestions(final String text) {
        if (text.trim().isEmpty()) {
            return;
        }

        booksService.searchSuggestions(text, new AsyncCallbackApp<SuggestionsResult>(eventBus) {

            @Override
            public void onSuccess(final SuggestionsResult result) {

                view.getSuggestionsWidget().setSuggestions(result.getSuggestions());
                view.getSuggestionsWidget().show();
            }

        });

    }

}
