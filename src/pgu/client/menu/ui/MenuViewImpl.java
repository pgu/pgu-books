package pgu.client.menu.ui;

import java.util.ArrayList;

import pgu.client.menu.MenuPresenter;
import pgu.client.menu.MenuView;
import pgu.shared.dto.BooksSearch;
import pgu.shared.dto.Suggestion;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.NavLink;
import com.github.gwtbootstrap.client.ui.NavPills;
import com.github.gwtbootstrap.client.ui.NavSearch;
import com.github.gwtbootstrap.client.ui.Popover;
import com.github.gwtbootstrap.client.ui.ProgressBar;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.github.gwtbootstrap.client.ui.base.HasVisibility;
import com.github.gwtbootstrap.client.ui.constants.Placement;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class MenuViewImpl extends Composite implements MenuView {

    private static MenuViewImplUiBinder uiBinder = GWT.create(MenuViewImplUiBinder.class);

    interface MenuViewImplUiBinder extends UiBinder<Widget, MenuViewImpl> {
    }

    @UiField
    ProgressBar           progressBar;
    @UiField
    Button                searchSuggestionsBtn, searchBooksBtn;
    @UiField
    NavSearch             sText, sTitle, sAuthor, sEditor, sCategory, sYear, sComment;
    @UiField
    NavLink               adminBtn, logoutBtn, goToImportBtn, goToSetupBtn, goToSearchBtn, goToAppstatsBtn;
    @UiField
    Popover               popoverInfo;
    @UiField
    NavPills              suggestionsContainer;

    private MenuPresenter presenter;

    public MenuViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
        logoutBtn.setVisible(false);
        goToSetupBtn.setVisible(false); // wait for new options to setup
        goToImportBtn.setVisible(false);
        goToSearchBtn.setVisible(false);
        goToAppstatsBtn.setVisible(false);
        progressBar.setVisible(false);
        suggestionsContainer.setVisible(false);

        onFieldsKeyPress();
        onSearchTextKeyPress();
        onSearchTextKeyUp();
        popoverInfo.setAnimation(true);
        popoverInfo.setPlacement(Placement.BOTTOM);
        popoverInfo.setText("Para todos los campos<br>" + //
                "se puede utilizar \"OR\"<br>" + //
                "Ej: angel <b>OR</b> adolfo<br>" + //
                "<br>" + //
                "Para el campo <i>Año</i>,<br>" + //
                "también se puede utilizar<br>" + //
                "Ej: <b>>1986 AND <1989</b><br>" + //
                "o <b>1982 OR >1986 AND <1989</b>" //
        );
    }

    private void onSearchTextKeyUp() {
        sText.getTextBox().addKeyUpHandler(new KeyUpHandler() {

            @Override
            public void onKeyUp(final KeyUpEvent event) {
                sText.setTitle(sText.getTextBox().getText());
            }
        });
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
        presenter.searchBooks(new BooksSearch());
    }

    @UiHandler("goToAppstatsBtn")
    public void clickGoToAppstats(final ClickEvent e) {
        Window.open("appstats/", "appstats", null);
    }

    @UiHandler("goToImportBtn")
    public void clickGoToImport(final ClickEvent e) {
        presenter.importBooks();
    }

    @UiHandler("searchBooksBtn")
    public void clickSearchOnFields(final ClickEvent e) {
        searchBooks();
    }

    private void searchBooks() {
        final BooksSearch booksSearch = new BooksSearch();
        booksSearch.setAuthor(sAuthor.getTextBox().getText());
        booksSearch.setCategory(sCategory.getTextBox().getText());
        booksSearch.setComment(sComment.getTextBox().getText());
        booksSearch.setEditor(sEditor.getTextBox().getText());
        booksSearch.setTitle(sTitle.getTextBox().getText());
        booksSearch.setYear(sYear.getTextBox().getText());

        presenter.searchBooks(booksSearch);
    }

    private void onFieldsKeyPress() {
        final ArrayList<TextBox> boxes = new ArrayList<TextBox>();
        boxes.add(sTitle.getTextBox());
        boxes.add(sAuthor.getTextBox());
        boxes.add(sEditor.getTextBox());
        boxes.add(sCategory.getTextBox());
        boxes.add(sYear.getTextBox());
        boxes.add(sComment.getTextBox());
        for (final TextBox box : boxes) {
            box.addKeyPressHandler(new KeyPressHandler() {

                @Override
                public void onKeyPress(final KeyPressEvent event) {
                    if (event.getCharCode() == KeyCodes.KEY_ENTER) {
                        searchBooks();
                    }
                }
            });
            box.addKeyUpHandler(new KeyUpHandler() {

                @Override
                public void onKeyUp(final KeyUpEvent event) {
                    box.setTitle(box.getText());
                }
            });
        }
    }

    @UiHandler("searchSuggestionsBtn")
    public void clickSearch(final ClickEvent e) {
        searchSuggestions();
    }

    private void onSearchTextKeyPress() {
        sText.getTextBox().addKeyPressHandler(new KeyPressHandler() {

            @Override
            public void onKeyPress(final KeyPressEvent event) {
                if (event.getCharCode() == KeyCodes.KEY_ENTER) {
                    searchSuggestions();
                }
            }
        });
    }

    private void searchSuggestions() {
        presenter.searchSuggestions(sText.getTextBox().getText());
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

    @Override
    public HasVisibility getAppstatsWidget() {
        return new HasVisibility() {

            @Override
            public void toggle() {
                throw new UnsupportedOperationException();
            }

            @Override
            public void show() {
                goToAppstatsBtn.setVisible(true);
            }

            @Override
            public void hide() {
                goToAppstatsBtn.setVisible(false);
            }
        };
    }

    private SuggestionsWidget suggestionsWidget = null;

    @Override
    public SuggestionsWidget getSuggestionsWidget() {
        if (suggestionsWidget == null) {
            suggestionsWidget = new SuggestionsWidget() {

                @Override
                public void toggle() {
                    throw new UnsupportedOperationException();
                }

                @Override
                public void show() {
                    suggestionsContainer.setVisible(true);
                }

                @Override
                public void hide() {
                    suggestionsContainer.clear();
                    suggestionsContainer.setVisible(false);
                }

                @Override
                public void setSuggestions(final ArrayList<Suggestion> suggestions) {
                    suggestionsContainer.clear();
                    GWT.log("suggestions size " + suggestions.size());

                    // int counter = 1;
                    // FluidRow currentRow = new FluidRow();
                    // TODO PGU Jul 23, 2012 voir booksviewimpl line 195 add dom handler
                    for (final Suggestion suggestion : suggestions) {
                        // final Column col = new Column(2);
                        final NavLink navLink = new NavLink();
                        navLink.setText(suggestion.getField() + ": " + suggestion.getValue());
                        suggestionsContainer.add(navLink);
                        // col.getElement().setInnerHTML();
                        // currentRow.add(col);

                        // if (counter % 6 == 1) {
                        // suggestionsContainer.add(currentRow);
                        //
                        // } else if (counter % 6 == 0) {
                        // currentRow = new FluidRow();
                        //
                        // }
                        // counter++;
                    }

                }
            };

        }
        return suggestionsWidget;
    }

}
