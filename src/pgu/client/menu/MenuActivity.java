package pgu.client.menu;

import java.util.Date;
import java.util.HashMap;

import pgu.client.app.event.GoToBooksEvent;
import pgu.client.app.event.HideWaitingIndicatorEvent;
import pgu.client.app.event.ImportBooksEvent;
import pgu.client.app.event.SetupEvent;
import pgu.client.app.event.ShowWaitingIndicatorEvent;
import pgu.client.app.mvp.ClientFactory;
import pgu.client.app.utils.AsyncCallbackApp;
import pgu.client.app.utils.ClientUtils;
import pgu.client.service.BooksServiceAsync;
import pgu.shared.domain.BooksCount;
import pgu.shared.dto.LoginInfo;
import pgu.shared.dto.SuggestionsResult;
import pgu.shared.utils.DateUtils;
import pgu.shared.utils.SearchField;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.web.bindery.event.shared.EventBus;

public class MenuActivity implements MenuPresenter //
        , ShowWaitingIndicatorEvent.Handler //
        , HideWaitingIndicatorEvent.Handler //
{

    private final MenuView          view;
    private EventBus                eventBus;
    private final LoginInfo         loginInfo;
    private final BooksServiceAsync booksService;
    private final ClientUtils       u = new ClientUtils();

    public MenuActivity(final ClientFactory clientFactory) {
        view = clientFactory.getMenuView();
        loginInfo = clientFactory.getLoginInfo();
        booksService = clientFactory.getBooksService();
    }

    public void start(final EventBus eventBus) {
        this.eventBus = eventBus;
        view.setPresenter(this);
        setAppTitle();

        eventBus.addHandler(ShowWaitingIndicatorEvent.TYPE, this);
        eventBus.addHandler(HideWaitingIndicatorEvent.TYPE, this);

        if (loginInfo.isLoggedIn()) {

            view.getLoginWidget().hide();

            view.getLogoutWidget().setHref(loginInfo.getLogoutUrl());
            view.getLogoutWidget().show();

            view.getLibraryWidget().show();

        } else {
            view.getLoginWidget().setHref(loginInfo.getLoginUrl());
            view.getLoginWidget().show();

            view.getLogoutWidget().hide();
            view.getLibraryWidget().hide();
        }

        if (loginInfo.isLoggedIn() //
                && "guilcher.pascal.dev@gmail.com".equals(loginInfo.getEmailAddress())) {

            view.getImportWidget().show();
            view.getAppstatsWidget().show();

        } else {
            view.getImportWidget().hide();
            view.getAppstatsWidget().hide();

        }

    }

    public void setAppTitle() {
        booksService.getBooksCount(new AsyncCallbackApp<BooksCount>(eventBus) {

            @Override
            public void onSuccess(final BooksCount booksCount) {
                if (booksCount == null) {
                    view.getBooksCountWidget().hide();
                    return;
                }

                final int count = booksCount.getCount();

                final String str_date = booksCount.getCountDate();
                final Date lastCountDate = DateTimeFormat.getFormat(DateUtils.FULL_DOT_FMT).parseStrict(str_date);

                view.getBooksCountWidget().setCount(count, lastCountDate);
            }

            @Override
            public void onFailure(final Throwable caught) {
                view.getBooksCountWidget().hide();
                super.onFailure(caught);
            }

        });
    }

    @Override
    public void searchBooks() {

        final HashMap<SearchField, String> filters = new HashMap<SearchField, String>();
        filters.put(SearchField.AUTHOR, view.getFilterAuthor());
        filters.put(SearchField.CATEGORY, view.getFilterCategory());
        filters.put(SearchField.COMMENT, view.getFilterComment());
        filters.put(SearchField.EDITOR, view.getFilterEditor());
        filters.put(SearchField.TITLE, view.getFilterTitle());
        filters.put(SearchField.YEAR, view.getFilterYear());

        final GoToBooksEvent event = new GoToBooksEvent();
        event.setFilters(filters);

        u.fire(eventBus, event);
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
        u.fire(eventBus, new ImportBooksEvent());
    }

    @Override
    public void goToSetup() {
        u.fire(eventBus, new SetupEvent());
    }

    @Override
    public void searchSuggestions(final String text) {
        if (text.trim().isEmpty()) {
            return;
        }

        u.fire(eventBus, new ShowWaitingIndicatorEvent());
        booksService.searchSuggestions(text, new AsyncCallbackApp<SuggestionsResult>(eventBus) {

            @Override
            public void onSuccess(final SuggestionsResult result) {
                u.fire(eventBus, new HideWaitingIndicatorEvent());

                view.getSuggestionsWidget().setSuggestions(result.getSuggestions());
                view.getSuggestionsWidget().show();
            }

        });

    }

    @Override
    public void searchCategorySuggestions() {
        u.fire(eventBus, new ShowWaitingIndicatorEvent());
        booksService.searchCategorySuggestions(new AsyncCallbackApp<SuggestionsResult>(eventBus) {

            @Override
            public void onSuccess(final SuggestionsResult result) {
                u.fire(eventBus, new HideWaitingIndicatorEvent());

                view.getSuggestionsWidget().setSuggestions(result.getSuggestions());
                view.getSuggestionsWidget().show();
            }

        });
    }

}
