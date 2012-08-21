package pgu.client.menu.ui;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;

import pgu.client.app.utils.ClientUtils;
import pgu.client.menu.MenuPresenter;
import pgu.client.menu.MenuView;
import pgu.shared.dto.Suggestion;
import pgu.shared.utils.SearchField;

import com.github.gwtbootstrap.client.ui.Brand;
import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Label;
import com.github.gwtbootstrap.client.ui.NavLink;
import com.github.gwtbootstrap.client.ui.NavPills;
import com.github.gwtbootstrap.client.ui.NavSearch;
import com.github.gwtbootstrap.client.ui.Popover;
import com.github.gwtbootstrap.client.ui.ProgressBar;
import com.github.gwtbootstrap.client.ui.Row;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.github.gwtbootstrap.client.ui.base.HasVisibility;
import com.github.gwtbootstrap.client.ui.constants.IconSize;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.github.gwtbootstrap.client.ui.constants.Placement;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
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
    Brand                                      appTitle;
    @UiField
    ProgressBar                                progressBar;
    @UiField
    Button                                     searchSuggestionsBtn, searchBooksBtn, clearBooksBtn,
            clearSuggestionsBtn, tagSuggestionsBtn;
    @UiField
    NavSearch                                  sText, sTitle, sAuthor, sEditor, sCategory, sYear, sComment;
    @UiField
    NavLink                                    adminBtn, logoutBtn, goToImportBtn, goToSetupBtn, goToLibraryBtn,
            goToAppstatsBtn, //
            sTitleIcon, sAuthorIcon, sEditorIcon, sCategoryIcon, sYearIcon, sCommentIcon;

    @UiField
    Popover                                    searchInfo, suggestionsInfo;
    @UiField
    NavPills                                   suggestionsContainer;
    @UiField
    Label                                      suggestionsFound;
    @UiField
    Row                                        suggestionsRow;

    private MenuPresenter                      presenter;
    private final ClientUtils                  u                = new ClientUtils();
    private final HashMap<NavSearch, NavLink>  field2link       = new HashMap<NavSearch, NavLink>();
    private final HashMap<NavSearch, TextBox>  field2box        = new HashMap<NavSearch, TextBox>();
    private final HashMap<IconType, NavSearch> icon2field       = new HashMap<IconType, NavSearch>();
    private final HashMap<String, IconType>    searchField2icon = new HashMap<String, IconType>();

    public MenuViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
        logoutBtn.setVisible(false);
        goToSetupBtn.setVisible(false); // wait for new options to setup
        goToImportBtn.setVisible(false);
        goToLibraryBtn.setVisible(false);
        goToAppstatsBtn.setVisible(false);
        progressBar.setVisible(false);
        suggestionsContainer.setVisible(false);
        suggestionsFound.setVisible(false);

        field2box.put(sTitle, sTitle.getTextBox());
        field2box.put(sAuthor, sAuthor.getTextBox());
        field2box.put(sEditor, sEditor.getTextBox());
        field2box.put(sCategory, sCategory.getTextBox());
        field2box.put(sYear, sYear.getTextBox());
        field2box.put(sComment, sComment.getTextBox());

        field2link.put(sTitle, sTitleIcon);
        field2link.put(sAuthor, sAuthorIcon);
        field2link.put(sEditor, sEditorIcon);
        field2link.put(sCategory, sCategoryIcon);
        field2link.put(sYear, sYearIcon);
        field2link.put(sComment, sCommentIcon);

        icon2field.put(IconType.USER, sAuthor);
        icon2field.put(IconType.TAG, sCategory);
        icon2field.put(IconType.COMMENT, sComment);
        icon2field.put(IconType.PRINT, sEditor);
        icon2field.put(IconType.CALENDAR, sYear);
        icon2field.put(IconType.BOOK, sTitle);

        searchField2icon.put(SearchField.AUTHOR.toString(), IconType.USER);
        searchField2icon.put(SearchField.CATEGORY.toString(), IconType.TAG);
        searchField2icon.put(SearchField.COMMENT.toString(), IconType.COMMENT);
        searchField2icon.put(SearchField.EDITOR.toString(), IconType.PRINT);
        searchField2icon.put(SearchField.YEAR.toString(), IconType.CALENDAR);
        searchField2icon.put(SearchField.TITLE.toString(), IconType.BOOK);

        onFieldsKeyPress();
        onSearchTextKeyPress();
        onSearchTextKeyUp();

        initSearchInfo();
        initSuggestionsInfo();

    }

    private void initSuggestionsInfo() {
        suggestionsInfo.setAnimation(true);
        suggestionsInfo.setPlacement(Placement.BOTTOM);
        suggestionsInfo.setHeading("Sugerencias");
        suggestionsInfo.setText("Se puede utilizar palabras o frases" + //
                " para recuperar los valores que se puede utilizar" + //
                " en las casillas de búsqueda<br>" + //
                "<br>" + //
                "Por ejemplo: \"john\"<br>" + //
                "puede dar como sugerencias<br>" + //
                "\"Saint John Perse\"<br>" + //
                "<br>" + //
                "Al pinchar una sugerencia, la casilla de búsqueda correspondente se rellena automáticamente" + //
                "" //
        );
    }

    private void initSearchInfo() {
        searchInfo.setAnimation(true);
        searchInfo.setPlacement(Placement.BOTTOM);
        searchInfo.setHeading("Búsqueda");
        searchInfo.setText("La búsqueda es sensible al uso de mayúsculas y minúsculas." + //
                " Para encontrar los valores exactos, se puede usar las sugerencias." + //
                "<br>También se puede buscar para un campo vacío si se pone \"-\" la casilla." //
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

    @UiHandler("goToLibraryBtn")
    public void clickGoToSearch(final ClickEvent e) {
        searchBooks();
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

    @UiHandler("clearBooksBtn")
    public void clickClearFields(final ClickEvent e) {
        sTitle.getTextBox().setText("");
        sAuthor.getTextBox().setText("");
        sEditor.getTextBox().setText("");
        sCategory.getTextBox().setText("");
        sYear.getTextBox().setText("");
        sComment.getTextBox().setText("");

        sTitleIcon.setActive(false);
        sAuthorIcon.setActive(false);
        sEditorIcon.setActive(false);
        sCategoryIcon.setActive(false);
        sYearIcon.setActive(false);
        sCommentIcon.setActive(false);
    }

    @UiHandler("clearSuggestionsBtn")
    public void clickClearSuggestions(final ClickEvent e) {
        sText.getTextBox().setText("");
        getSuggestionsWidget().hide();
    }

    private void searchBooks() {
        presenter.searchBooks();
    }

    private void onFieldsKeyPress() {

        for (final Entry<NavSearch, TextBox> e : field2box.entrySet()) {
            final NavSearch field = e.getKey();
            final TextBox box = e.getValue();
            final NavLink link = field2link.get(field);

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
                    link.setActive(!box.getText().isEmpty());
                }
            });
        }
    }

    @UiHandler("tagSuggestionsBtn")
    public void clickSearchTagSuggestions(final ClickEvent e) {
        presenter.searchTagSuggestions();
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
    public HasVisibility getLibraryWidget() {
        return new HasVisibility() {

            @Override
            public void toggle() {
                throw new UnsupportedOperationException();
            }

            @Override
            public void show() {
                goToLibraryBtn.setVisible(true);
            }

            @Override
            public void hide() {
                goToLibraryBtn.setVisible(false);
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
                    suggestionsRow.addStyleName("suggestionsRow");
                    suggestionsContainer.setVisible(true);
                    suggestionsFound.setVisible(true);
                }

                @Override
                public void hide() {
                    suggestionsRow.removeStyleName("suggestionsRow");
                    suggestionsContainer.clear();
                    suggestionsContainer.setVisible(false);
                    suggestionsFound.setVisible(false);
                }

                @Override
                public void setSuggestions(final ArrayList<Suggestion> suggestions) {
                    suggestionsContainer.clear();

                    suggestionsFound.setText("Sugerencias encontradas: " + suggestions.size());

                    for (final Suggestion suggestion : suggestions) {
                        suggestionsContainer.add(new SuggestionNavLink(suggestion));
                    }
                }

            };

        }
        return suggestionsWidget;
    }

    private class SuggestionNavLink extends NavLink {

        public SuggestionNavLink(final Suggestion suggestion) {

            final IconType icon = getSuggestionIcon(suggestion);
            setIcon(icon);
            setIconSize(IconSize.LARGE);
            setText(suggestion.getValue());
            addClickHandler(new ClickHandler() {

                @Override
                public void onClick(final ClickEvent event) {
                    setActive(!isActive());
                    setSearchBoxText(icon);
                }

                private void setSearchBoxText(final IconType icon) {
                    final NavSearch navSearch = icon2field.get(icon);

                    navSearch.getTextBox().setText(isActive() ? getText() : "");
                    field2link.get(navSearch).setActive(isActive());
                }
            });
        }

        private IconType getSuggestionIcon(final Suggestion suggestion) {
            final String fieldName = suggestion.getField();
            return searchField2icon.get(fieldName);
        }

    }

    @Override
    public String getFilterAuthor() {
        return sAuthor.getTextBox().getText();
    }

    @Override
    public String getFilterCategory() {
        return sCategory.getTextBox().getText();
    }

    @Override
    public String getFilterComment() {
        return sComment.getTextBox().getText();
    }

    @Override
    public String getFilterEditor() {
        return sEditor.getTextBox().getText();
    }

    @Override
    public String getFilterTitle() {
        return sTitle.getTextBox().getText();
    }

    @Override
    public String getFilterYear() {
        return sYear.getTextBox().getText();
    }

    @Override
    public BooksCountWidget getBooksCountWidget() {
        return new BooksCountWidget() {

            @Override
            public void setCount(final int count, final Date lastCountDate) {
                final String date = DateTimeFormat.getFormat("dd/MM/yyyy").format(lastCountDate);
                appTitle.setHTML("Biblioteca de " + count + " libros <span style=\"font-size:x-small\">(" + date
                        + ")</span>");
            }

            @Override
            public void hide() {
                appTitle.setText("Biblioteca");
            }
        };
    }

}
