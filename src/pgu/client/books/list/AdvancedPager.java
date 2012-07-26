package pgu.client.books.list;

import pgu.shared.dto.BooksSearch;

import com.github.gwtbootstrap.client.ui.NavLink;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

public class AdvancedPager {

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
            // presenter.goToSearchBooks(booksSearch);
        }
    }

}
