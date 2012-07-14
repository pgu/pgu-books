package pgu.client.books.ui;

import pgu.client.books.BooksPresenter;
import pgu.client.books.BooksView;
import pgu.shared.domain.Book;
import pgu.shared.dto.BooksResult;
import pgu.shared.dto.BooksSearch;

import com.github.gwtbootstrap.client.ui.Column;
import com.github.gwtbootstrap.client.ui.FluidContainer;
import com.github.gwtbootstrap.client.ui.FluidRow;
import com.github.gwtbootstrap.client.ui.NavLink;
import com.github.gwtbootstrap.client.ui.Pagination;
import com.google.gwt.core.client.GWT;
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

    @Override
    public void setPresenter(final BooksPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setBooks(final BooksResult booksResult) {
        readonlyGrid.clear();

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
            row.add(titleCol);
            row.add(authorCol);
            row.add(editorCol);
            row.add(categoryCol);
            row.add(yearCol);
            row.add(commentCol);

            readonlyGrid.add(row);
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

            // with the blockIdx:
            // startBlock: blockIdx - 5
            // compare startBlock with 0 -> take the higher
            final long startBlockIdx = blockIdx - 5;
            final int startIdx = startBlockIdx > 0 ? (int) startBlockIdx : 0;

            // endBlock: blockIdx + 4
            // compare endBlock with lastBlock -> take the lower
            final long lastBlockIdx = nbBlock - 1;
            final long endBlockIdx = blockIdx < 10 ? 9 : blockIdx + 4;
            final int endIdx = endBlockIdx < lastBlockIdx ? (int) endBlockIdx : (int) lastBlockIdx;

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
