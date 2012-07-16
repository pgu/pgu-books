package pgu.client.menu.ui;

import pgu.client.menu.MenuPresenter;
import pgu.client.menu.MenuView;
import pgu.shared.dto.BooksSearch;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.NavLink;
import com.github.gwtbootstrap.client.ui.NavSearch;
import com.github.gwtbootstrap.client.ui.ProgressBar;
import com.github.gwtbootstrap.client.ui.base.HasVisibility;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class MenuViewImpl extends Composite implements MenuView {

    private static MenuViewImplUiBinder uiBinder = GWT.create(MenuViewImplUiBinder.class);

    interface MenuViewImplUiBinder extends UiBinder<Widget, MenuViewImpl> {
    }

    @UiField
    ProgressBar           progressBar;
    @UiField
    Button                searchBtn, searchOnFieldsBtn;
    @UiField
    NavSearch             sText, sTitle, sAuthor, sEditor, sCategory, sYear, sComment;
    @UiField
    NavLink               adminBtn, logoutBtn, goToImportBtn, goToSetupBtn, goToSearchBtn;

    private MenuPresenter presenter;

    public MenuViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
        logoutBtn.setVisible(false);
        goToImportBtn.setVisible(false);
        goToSearchBtn.setVisible(false);
        progressBar.setVisible(false);
    }

    @Override
    public void setPresenter(final MenuPresenter presenter) {
        this.presenter = presenter;
    }

    @UiHandler("goToSetupBtn")
    public void clickGoToSetup(final ClickEvent e) {
        presenter.goToSetup();
    }

    @UiHandler("goToSearchBtn")
    public void clickGoToSearch(final ClickEvent e) {
        final BooksSearch booksSearch = new BooksSearch();
        presenter.searchBooks(booksSearch);
    }

    @UiHandler("goToImportBtn")
    public void clickGoToImport(final ClickEvent e) {
        presenter.importBooks();
    }

    @UiHandler("searchOnFieldsBtn")
    public void clickSearchOnFields(final ClickEvent e) {
        final BooksSearch booksSearch = new BooksSearch();
        booksSearch.setAuthor(sAuthor.getTextBox().getText());
        booksSearch.setCategory(sCategory.getTextBox().getText());
        booksSearch.setComment(sComment.getTextBox().getText());
        booksSearch.setEditor(sEditor.getTextBox().getText());
        booksSearch.setTitle(sTitle.getTextBox().getText());
        booksSearch.setYear(sYear.getTextBox().getText());

        presenter.searchBooks(booksSearch);
    }

    @UiHandler("searchBtn")
    public void clickSearch(final ClickEvent e) {

        final BooksSearch booksSearch = new BooksSearch();
        booksSearch.setSearchText(sText.getTextBox().getText());

        presenter.searchBooks(booksSearch);
    }

    @Override
    public HasVisibility getWaitingIndicator() {
        return new HasVisibility() {

            @Override
            public void toggle() {
                throw new UnsupportedOperationException();
            }

            @Override
            public void show() {
                progressBar.setVisible(true);
            }

            @Override
            public void hide() {
                progressBar.setVisible(false);
            }
        };
    }

    @Override
    public LogWidget getLoginWidget() {
        return new LogWidget() {

            @Override
            public void setTargetHistoryToken(final String targetHistoryToken) {
                throw new UnsupportedOperationException();
            }

            @Override
            public void setHref(final String href) {
                adminBtn.setHref(href);
            }

            @Override
            public String getTargetHistoryToken() {
                throw new UnsupportedOperationException();
            }

            @Override
            public String getHref() {
                throw new UnsupportedOperationException();
            }

            @Override
            public void toggle() {
                throw new UnsupportedOperationException();
            }

            @Override
            public void show() {
                adminBtn.setVisible(true);
            }

            @Override
            public void hide() {
                adminBtn.setVisible(false);
            }
        };
    }

    @Override
    public LogWidget getLogoutWidget() {
        return new LogWidget() {

            @Override
            public void setTargetHistoryToken(final String targetHistoryToken) {
                throw new UnsupportedOperationException();
            }

            @Override
            public void setHref(final String href) {
                logoutBtn.setHref(href);
            }

            @Override
            public String getTargetHistoryToken() {
                throw new UnsupportedOperationException();
            }

            @Override
            public String getHref() {
                throw new UnsupportedOperationException();
            }

            @Override
            public void toggle() {
                throw new UnsupportedOperationException();
            }

            @Override
            public void show() {
                logoutBtn.setVisible(true);
            }

            @Override
            public void hide() {
                logoutBtn.setVisible(false);
            }
        };
    }

    @Override
    public HasVisibility getImportWidget() {
        return new HasVisibility() {

            @Override
            public void toggle() {
                throw new UnsupportedOperationException();
            }

            @Override
            public void show() {
                goToImportBtn.setVisible(true);
            }

            @Override
            public void hide() {
                goToImportBtn.setVisible(false);
            }
        };
    }

    @Override
    public HasVisibility getSearchWidget() {
        return new HasVisibility() {

            @Override
            public void toggle() {
                throw new UnsupportedOperationException();
            }

            @Override
            public void show() {
                goToSearchBtn.setVisible(true);
            }

            @Override
            public void hide() {
                goToSearchBtn.setVisible(false);
            }
        };
    }

}
