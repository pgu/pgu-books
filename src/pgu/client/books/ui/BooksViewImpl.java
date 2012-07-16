package pgu.client.books.ui;

import pgu.client.books.BooksPresenter;
import pgu.client.books.BooksView;
import pgu.shared.domain.Book;
import pgu.shared.dto.BooksResult;
import pgu.shared.dto.BooksSearch;
import pgu.shared.utils.SortField;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Column;
import com.github.gwtbootstrap.client.ui.FluidContainer;
import com.github.gwtbootstrap.client.ui.FluidRow;
import com.github.gwtbootstrap.client.ui.NavLink;
import com.github.gwtbootstrap.client.ui.Pagination;
import com.github.gwtbootstrap.client.ui.base.InlineLabel;
import com.github.gwtbootstrap.client.ui.constants.ButtonType;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.github.gwtbootstrap.client.ui.resources.ButtonSize;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class BooksViewImpl extends Composite implements BooksView {

    private static BooksViewImplUiBinder uiBinder = GWT.create(BooksViewImplUiBinder.class);

    interface BooksViewImplUiBinder extends UiBinder<Widget, BooksViewImpl> {
    }

    @UiField
    FluidContainer         readonlyGrid;
    @UiField
    Pagination             pager;

    private BooksPresenter presenter;

    public BooksViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
        pager.setVisible(false);
    }

    private void setHeaders(final BooksResult booksResult) {

        final SortField sortField = booksResult.getBooksSearch().getSortField();
        final boolean isAscending = booksResult.getBooksSearch().isAscending();

        final Column titleCol = new Column(2);
        final Column authorCol = new Column(2);
        final Column editorCol = new Column(2);
        final Column categoryCol = new Column(2);
        final Column yearCol = new Column(2);
        final Column commentCol = new Column(2);

        addHeaderWithSort(titleCol, "Título", SortField.TITLE, sortField, isAscending, booksResult);
        addHeaderWithSort(authorCol, "Autor", SortField.AUTHOR, sortField, isAscending, booksResult);
        addHeaderWithSort(editorCol, "Editor", SortField.EDITOR, sortField, isAscending, booksResult);
        addHeaderWithSort(categoryCol, "Categoría", SortField.CATEGORY, sortField, isAscending, booksResult);
        addHeaderWithSort(yearCol, "Año", SortField.YEAR, sortField, isAscending, booksResult);
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

        readonlyGrid.add(row);
    }

    private void addHeaderWithSort(final Column col, final String text, //
            final SortField sortField, //
            final SortField selectedSortField, //
            final boolean isAscending, //
            final BooksResult booksResult) {

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
                final BooksSearch booksSearch = booksResult.getBooksSearch();
                booksSearch.setSortField(sortField);
                booksSearch.setAscending(true);
                presenter.goToSearchBooks(booksSearch);
            }
        });
        downBtn.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {
                final BooksSearch booksSearch = booksResult.getBooksSearch();
                booksSearch.setSortField(sortField);
                booksSearch.setAscending(false);
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
    public void setBooks(final BooksResult booksResult) {
        readonlyGrid.clear();

        setHeaders(booksResult);

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
            yearCol.getElement().setInnerText("" + book.getYear());
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

            readonlyGrid.add(row);
            count++;
        }
        // Anterior 1..10 Posterior
        final int start = booksResult.getBooksSearch().getStart();
        final int length = booksResult.getBooksSearch().getLength();
        final long nbFound = booksResult.getNbFound();

        // numero record / length -> n° bloc
        // numero record % length -> if > 0 then +1 to n° bloc
        // 5 links before the current block then 4 links
        // 01..10 -> 1 (records -> block)
        // 11..20 -> 2
        // 21..30 -> 3
        // 31..40 -> 4
        // 1..10 -> 5 current 4

        pager.clear();

        if (nbFound == 0) {
            pager.setVisible(false);
        } else {
            final long blockIdx = start / length;

            long nbBlock = nbFound / length;
            if (nbFound % length > 0) {
                nbBlock++;
            }

            final long startBlockIdx = blockIdx - 5;
            int startIdx = startBlockIdx > 0 ? (int) startBlockIdx : 0;
            if (blockIdx + 4 > nbBlock - 1 //
                    && nbBlock > 9) {
                startIdx = (int) (blockIdx - (10 - (nbBlock - blockIdx)));
            }

            final long lastBlockIdx = nbBlock - 1;
            final long endBlockIdx = blockIdx + 4;
            int endIdx = endBlockIdx < lastBlockIdx ? (int) endBlockIdx : (int) lastBlockIdx;
            if (blockIdx < 5 //
                    && nbBlock > 9) {
                endIdx = 9;
            }

            if (nbBlock > 1) {

                final NavLink previousLink = new NavLink("Anterior");
                pager.add(previousLink);
                if (blockIdx == 0L) {
                    previousLink.setActive(true);
                } else {
                    previousLink.addClickHandler(new PagerClickHandler((int) blockIdx - 1, booksResult));
                }

                for (int i = startIdx; i < endIdx + 1; i++) {
                    final int numBlock = i + 1;
                    final NavLink navLink = new NavLink("" + numBlock);
                    pager.add(navLink);
                    if (blockIdx == i) {
                        navLink.setActive(true);
                    } else {
                        navLink.addClickHandler(new PagerClickHandler(i, booksResult));
                    }
                }

                final NavLink nextLink = new NavLink("Siguiente");
                pager.add(nextLink);
                if (blockIdx == nbBlock - 1) {
                    nextLink.setActive(true);
                } else {
                    nextLink.addClickHandler(new PagerClickHandler((int) blockIdx + 1, booksResult));
                }
            }

            pager.setVisible(true);
        }
    }

    private class PagerClickHandler implements ClickHandler {
        private int               i = 0;
        private final BooksResult booksResult;

        public PagerClickHandler(final int i, final BooksResult booksResult) {
            this.i = i;
            this.booksResult = booksResult;
        }

        @Override
        public void onClick(final ClickEvent event) {

            final BooksSearch booksSearch = booksResult.getBooksSearch();
            booksSearch.setStart(i * booksSearch.getLength());
            presenter.goToSearchBooks(booksSearch);
        }
    }

    @Override
    public void clear() {
        readonlyGrid.clear();
        pager.setVisible(false);
    }
}
