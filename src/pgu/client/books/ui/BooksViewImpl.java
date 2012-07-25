package pgu.client.books.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import pgu.client.app.utils.HasClickAndVisibility;
import pgu.client.books.BooksPresenter;
import pgu.client.books.BooksView;
import pgu.shared.domain.Book;
import pgu.shared.dto.BooksResult;
import pgu.shared.dto.BooksSearch;
import pgu.shared.utils.SortField;

import com.github.gwtbootstrap.client.ui.Badge;
import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Column;
import com.github.gwtbootstrap.client.ui.FluidContainer;
import com.github.gwtbootstrap.client.ui.FluidRow;
import com.github.gwtbootstrap.client.ui.NavLink;
import com.github.gwtbootstrap.client.ui.Pager;
import com.github.gwtbootstrap.client.ui.base.InlineLabel;
import com.github.gwtbootstrap.client.ui.constants.BadgeType;
import com.github.gwtbootstrap.client.ui.constants.ButtonType;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.github.gwtbootstrap.client.ui.resources.ButtonSize;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class BooksViewImpl extends Composite implements BooksView {

    private static BooksViewImplUiBinder uiBinder = GWT.create(BooksViewImplUiBinder.class);

    interface BooksViewImplUiBinder extends UiBinder<Widget, BooksViewImpl> {
    }

    // @UiField
    // Label booksFound;
    @UiField
    FluidContainer         booksGrid, toolBar;
    // @UiField
    // Pagination pagination;
    @UiField
    Pager                  pager;
    @UiField
    Button                 addBtn, editBtn, deleteBtn, refreshBtn;
    @UiField
    Column                 colBadges;

    private BooksPresenter presenter;

    public BooksViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
        // pagination.setVisible(false);
        pager.setVisible(false);
        toolBar.setVisible(false);
        refreshBtn.setVisible(false);
    }

    private void setHeaders(final BooksSearch booksSearch) {

        final SortField sortField = booksSearch.getSortField();
        final boolean isAscending = booksSearch.isAscending();

        final Column titleCol = new Column(2);
        final Column authorCol = new Column(2);
        final Column editorCol = new Column(2);
        final Column categoryCol = new Column(2);
        final Column yearCol = new Column(2);
        final Column commentCol = new Column(2);

        addHeaderWithSort(titleCol, "Título", SortField.TITLE, sortField, isAscending, booksSearch);
        addHeaderWithSort(authorCol, "Autor", SortField.AUTHOR, sortField, isAscending, booksSearch);
        addHeaderWithSort(editorCol, "Editor", SortField.EDITOR, sortField, isAscending, booksSearch);
        addHeaderWithSort(categoryCol, "Categoría", SortField.CATEGORY, sortField, isAscending, booksSearch);
        addHeaderWithSort(yearCol, "Año", SortField.YEAR, sortField, isAscending, booksSearch);
        commentCol.add(getColumnLabel("Comentario"));

        final FluidRow row = new FluidRow();
        row.addStyleName("my-show-grid-header");
        row.addStyleName("row-header");
        row.add(titleCol);
        row.add(authorCol);
        row.add(editorCol);
        row.add(categoryCol);
        row.add(yearCol);
        row.add(commentCol);

        booksGrid.add(row);
    }

    private void addHeaderWithSort(final Column col, final String text, //
            final SortField sortField, //
            final SortField selectedSortField, //
            final boolean isAscending, //
            final BooksSearch booksSearch) {

        final boolean isSelected = sortField == selectedSortField;

        final Button upBtn = new Button();
        upBtn.setSize(ButtonSize.MINI);
        upBtn.setIcon(IconType.SORT_UP);

        final Button downBtn = new Button();
        downBtn.setSize(ButtonSize.MINI);
        downBtn.setIcon(IconType.SORT_DOWN);

        col.add(getColumnLabel(text));
        col.add(upBtn);
        col.add(downBtn);

        if (isSelected) {
            upBtn.setType(isAscending ? ButtonType.INVERSE : ButtonType.DEFAULT);
            downBtn.setType(isAscending ? ButtonType.DEFAULT : ButtonType.INVERSE);
        }

        upBtn.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {
                presenter.updateSort(sortField, true);
                presenter.goToSearchBooks(booksSearch);
            }
        });
        downBtn.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {
                presenter.updateSort(sortField, false);
                presenter.goToSearchBooks(booksSearch);
            }
        });
    }

    private InlineLabel getColumnLabel(final String text) {
        final InlineLabel label = new InlineLabel(text);
        label.getElement().getStyle().setMarginRight(20, Unit.PX);
        return label;
    }

    @Override
    public void setPresenter(final BooksPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setBooks(final BooksResult booksResult, final BooksSearch booksSearch, final boolean isEditable) {
        colBadges.clear();
        booksGrid.clear();
        selectedRows.clear();

        // booksFound.setText("Libros encontrados: " + (int) booksResult.getNbFound());
        setBadgesForResultsPerPage(booksSearch);

        toolBar.setVisible(true);

        setHeaders(booksSearch);

        int count = 0;
        for (final Book book : booksResult.getBooks()) {

            final Column titleCol = new Column(2);
            final Column authorCol = new Column(2);
            final Column editorCol = new Column(2);
            final Column categoryCol = new Column(2);
            final Column yearCol = new Column(2);
            final Column commentCol = new Column(2);

            titleCol.getElement().setInnerText(book.getTitle());
            authorCol.getElement().setInnerText(book.getAuthor());
            editorCol.getElement().setInnerText(book.getEditor());
            categoryCol.getElement().setInnerText(book.getCategory());
            yearCol.getElement().setInnerText("" + (book.getYear() == 0 ? "" : book.getYear()));
            commentCol.getElement().setInnerText(book.getComment());

            final FluidRow row = new FluidRow();
            row.addStyleName("my-show-grid");
            row.addStyleName((count & 1) == 1 ? "row-odd" : "row-even");
            row.add(titleCol);
            row.add(authorCol);
            row.add(editorCol);
            row.add(categoryCol);
            row.add(yearCol);
            row.add(commentCol);

            if (isEditable) {
                row.addStyleName("row-editable");
                row2book.put(row, book);
                handlerRegs.add(row.addDomHandler(new ClickHandler() {

                    @Override
                    public void onClick(final ClickEvent event) {
                        if (selectedRows.contains(row)) {
                            selectedRows.remove(row);
                            row.removeStyleName("row-selected");
                        } else {
                            selectedRows.add(row);
                            row.addStyleName("row-selected");
                        }
                    }
                }, ClickEvent.getType()));
            }

            booksGrid.add(row);
            count++;
        }
        // setAdvancedPager(booksResult);
        setPager(booksSearch);
    }

    private void setPager(final BooksSearch booksResult) {
        // TODO PGU

    }

    private final HashSet<FluidRow>       selectedRows = new HashSet<FluidRow>();
    private final HashMap<FluidRow, Book> row2book     = new HashMap<FluidRow, Book>();

    public void setAdvancedPager(final BooksSearch booksSearch) {

        final int start = 0;
        final long nbFoundTotal = 0;
        // final int start = booksResult.getBooksSearch().getStart();
        final int lengthPerPage = booksSearch.getLength();
        // final long nbFoundTotal = booksResult.getNbFound();

        // pagination.clear();

        if (nbFoundTotal == 0) {
            // pagination.setVisible(false);
            return;
        }

        if (nbFoundTotal <= lengthPerPage) {
            // pagination.setVisible(false);
            return;
        }

        final long blockIdx = start / lengthPerPage;

        long nbBlock = nbFoundTotal / lengthPerPage;
        if (nbFoundTotal % lengthPerPage > 0) {
            nbBlock++;
        }

        if (nbBlock <= 10) {

            addPreviousLinkToPager(booksSearch, blockIdx);
            for (int i = 0; i < nbBlock; i++) {
                addLinkToPager(booksSearch, blockIdx, i);
            }
            addNextLinkToPager(booksSearch, blockIdx, nbBlock);
            // pagination.setVisible(true);
            return;

        } else {

            final long startBlockIdx = blockIdx - 5;
            int startIdx = startBlockIdx > 0 ? (int) startBlockIdx : 0;
            if (blockIdx + 4 > nbBlock - 1) {
                startIdx = (int) (blockIdx - (10 - (nbBlock - blockIdx)));
            }

            final long lastBlockIdx = nbBlock - 1;
            final long endBlockIdx = blockIdx + 4;
            int endIdx = endBlockIdx < lastBlockIdx ? (int) endBlockIdx : (int) lastBlockIdx;
            if (blockIdx < 5) {
                endIdx = 9;
            }

            addPreviousLinkToPager(booksSearch, blockIdx);
            for (int i = startIdx; i < endIdx + 1; i++) {
                addLinkToPager(booksSearch, blockIdx, i);
            }
            addNextLinkToPager(booksSearch, blockIdx, nbBlock);
            // pagination.setVisible(true);
            return;

        }
    }

    private void addLinkToPager(final BooksSearch booksResult, final long blockIdx, final int i) {
        final int numBlock = i + 1;
        final NavLink navLink = new NavLink("" + numBlock);
        // pagination.add(navLink);
        if (blockIdx == i) {
            navLink.setActive(true);
        } else {
            navLink.addClickHandler(new PagerClickHandler(i, booksResult));
        }
    }

    private void addNextLinkToPager(final BooksSearch booksResult, final long blockIdx, final long nbBlock) {
        final NavLink nextLink = new NavLink("Siguiente");
        // pagination.add(nextLink);
        if (blockIdx == nbBlock - 1) {
            nextLink.setActive(true);
        } else {
            nextLink.addClickHandler(new PagerClickHandler((int) blockIdx + 1, booksResult));
        }
    }

    private void addPreviousLinkToPager(final BooksSearch booksResult, final long blockIdx) {
        final NavLink previousLink = new NavLink("Anterior");
        // pagination.add(previousLink);
        if (blockIdx == 0L) {
            previousLink.setActive(true);
        } else {
            previousLink.addClickHandler(new PagerClickHandler((int) blockIdx - 1, booksResult));
        }
    }

    private void setBadgesForResultsPerPage(final BooksSearch booksSearch) {
        final int booksPerPage = booksSearch.getLength();

        final int[] bValues = new int[] { 10, 20, 30, 50 };
        for (final int bValue : bValues) {
            final Badge badge = new Badge();
            badge.setText("" + bValue);
            if (bValue == booksPerPage) {
                badge.setType(BadgeType.INVERSE);

            } else {
                badge.setType(BadgeType.DEFAULT);
                badge.getElement().getStyle().setCursor(Cursor.POINTER);
                badge.addClickHandler(new ClickHandler() {

                    @Override
                    public void onClick(final ClickEvent event) {
                        presenter.updateResultsPerPage(bValue);
                        presenter.goToSearchBooks(booksSearch);
                    }
                });
            }
            colBadges.add(badge);
        }
    }

    private class PagerClickHandler implements ClickHandler {
        private int               i = 0;
        private final BooksSearch booksSearch;

        public PagerClickHandler(final int i, final BooksSearch booksSearch) {
            this.i = i;
            this.booksSearch = booksSearch;
        }

        @Override
        public void onClick(final ClickEvent event) {

            // booksSearch.setStart(i * booksSearch.getLength());
            presenter.goToSearchBooks(booksSearch);
        }
    }

    @Override
    public void clear() {
        booksGrid.clear();
        // pagination.setVisible(false);
        toolBar.setVisible(false);
        pager.setVisible(false);
    }

    @Override
    public HasClickAndVisibility getRefreshBooksWidget() {
        return new HasClickAndVisibility() {

            @Override
            public boolean isVisible() {
                return refreshBtn.isVisible();
            }

            @Override
            public void setVisible(final boolean visible) {
                refreshBtn.setVisible(visible);
            }

            @Override
            public HandlerRegistration addClickHandler(final ClickHandler handler) {
                return refreshBtn.addClickHandler(handler);
            }

            @Override
            public void fireEvent(final GwtEvent<?> event) {
                refreshBtn.fireEvent(event);
            }

        };
    }

    @Override
    public HasClickAndVisibility getDeleteBooksWidget() {
        return new HasClickAndVisibility() {

            @Override
            public boolean isVisible() {
                return deleteBtn.isVisible();
            }

            @Override
            public void setVisible(final boolean visible) {
                deleteBtn.setVisible(visible);
            }

            @Override
            public HandlerRegistration addClickHandler(final ClickHandler handler) {
                return deleteBtn.addClickHandler(handler);
            }

            @Override
            public void fireEvent(final GwtEvent<?> event) {
                deleteBtn.fireEvent(event);
            }

        };
    }

    @Override
    public HasClickAndVisibility getEditBookWidget() {
        return new HasClickAndVisibility() {

            @Override
            public boolean isVisible() {
                return editBtn.isVisible();
            }

            @Override
            public void setVisible(final boolean visible) {
                editBtn.setVisible(visible);
            }

            @Override
            public HandlerRegistration addClickHandler(final ClickHandler handler) {
                return editBtn.addClickHandler(handler);
            }

            @Override
            public void fireEvent(final GwtEvent<?> event) {
                editBtn.fireEvent(event);
            }

        };
    }

    @Override
    public HasClickAndVisibility getCreateBookWidget() {
        return new HasClickAndVisibility() {

            @Override
            public void fireEvent(final GwtEvent<?> event) {
                addBtn.fireEvent(event);
            }

            @Override
            public HandlerRegistration addClickHandler(final ClickHandler handler) {
                return addBtn.addClickHandler(handler);
            }

            @Override
            public void setVisible(final boolean visible) {
                addBtn.setVisible(visible);
            }

            @Override
            public boolean isVisible() {
                return addBtn.isVisible();
            }
        };
    }

    @Override
    public Book getSelectedBook() {
        if (selectedRows.size() != 1) {
            return null;
        }

        return row2book.get(selectedRows.iterator().next());
    }

    private final ArrayList<HandlerRegistration> handlerRegs = new ArrayList<HandlerRegistration>();

    @Override
    public void clearHandlers() {
        for (HandlerRegistration handlerReg : handlerRegs) {
            handlerReg.removeHandler();
            handlerReg = null;
        }
        handlerRegs.clear();
    }

    @Override
    public HashSet<Book> getSelectedBooks() {
        final HashSet<Book> books = new HashSet<Book>();
        for (final FluidRow row : selectedRows) {
            books.add(row2book.get(row));
        }

        return books;
    }

}
