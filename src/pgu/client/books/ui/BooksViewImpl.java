package pgu.client.books.ui;

import java.util.ArrayList;

import pgu.client.books.BooksView;
import pgu.shared.domain.Book;
import pgu.shared.dto.BooksSearch;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Column;
import com.github.gwtbootstrap.client.ui.FluidContainer;
import com.github.gwtbootstrap.client.ui.FluidRow;
import com.github.gwtbootstrap.client.ui.ProgressBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class BooksViewImpl extends Composite implements BooksView {

    private static BooksViewImplUiBinder uiBinder = GWT.create(BooksViewImplUiBinder.class);

    interface BooksViewImplUiBinder extends UiBinder<Widget, BooksViewImpl> {
    }

    @UiField
    ProgressBar    progressBar;
    @UiField
    Button         searchBtn;
    @UiField
    FluidContainer readonlyGrid;

    public BooksViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
        progressBar.setVisible(false);

    }

    @UiHandler("searchBtn")
    public void clickSearch(final ClickEvent e) {
        readonlyGrid.clear();
        progressBar.setVisible(true);
        final BooksSearch booksSearch = new BooksSearch();
        // booksSearch.setAuthor(author);
        // booksSearch.setCategory(category);
        // booksSearch.setComentario(comentario);
        // booksSearch.setEditor(editor);
        // booksSearch.setTitle(title);
        // booksSearch.setYear(year);

        new Timer() {

            @Override
            public void run() {
                final ArrayList<Book> books = new ArrayList<Book>();
                for (int i = 0; i < 5; i++) {
                    final Book book = new Book() //
                            .title("title " + i) //
                            .author("author " + i) //
                            .editor("editor " + i) //
                            .category("cat " + i) //
                            .year(1980 + i) //
                            .comment("comment " + i) //
                            .id(1L * i) //
                    ;
                    books.add(book);
                }
                setBooks(books);
            }

        }.schedule(750);
    }

    private void setBooks(final ArrayList<Book> books) {
        progressBar.setVisible(false);

        for (final Book book : books) {

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
    }

}
