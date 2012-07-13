package pgu.client.menu.ui;

import pgu.client.menu.MenuPresenter;
import pgu.client.menu.MenuView;
import pgu.shared.dto.BooksSearch;

import com.github.gwtbootstrap.client.ui.Button;
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
    Button                searchBtn;
    @UiField
    NavSearch             sTitle, sAuthor, sEditor, sCategory, sYear, sComment;

    private MenuPresenter presenter;

    public MenuViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
        progressBar.setVisible(false);
    }

    @Override
    public void setPresenter(final MenuPresenter presenter) {
        this.presenter = presenter;
    }

    @UiHandler("searchBtn")
    public void clickSearch(final ClickEvent e) {

        final BooksSearch booksSearch = new BooksSearch();
        booksSearch.setAuthor(sAuthor.getTextBox().getText());
        booksSearch.setCategory(sCategory.getTextBox().getText());
        booksSearch.setComment(sComment.getTextBox().getText());
        booksSearch.setEditor(sEditor.getTextBox().getText());
        booksSearch.setTitle(sTitle.getTextBox().getText());
        booksSearch.setYear(sYear.getTextBox().getText());

        booksSearch.setStart(0);
        booksSearch.setLength(5);

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

}
