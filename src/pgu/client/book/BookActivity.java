package pgu.client.book;

import pgu.client.app.mvp.ClientFactory;
import pgu.client.app.utils.AsyncCallbackApp;
import pgu.client.app.utils.ClientUtils;
import pgu.client.app.utils.Level;
import pgu.client.app.utils.Notification;
import pgu.client.books.BookView;
import pgu.client.service.AdminBooksServiceAsync;
import pgu.shared.domain.Book;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.web.bindery.event.shared.EventBus;

public class BookActivity {

    private final EventBus               eventBus;
    private final BookView               view;
    private final ClientUtils            u = new ClientUtils();
    private final AdminBooksServiceAsync adminBookService;

    public BookActivity(final ClientFactory clientFactory) {
        eventBus = clientFactory.getEventBus();
        view = clientFactory.getBookView();
        adminBookService = clientFactory.getAdminBooksService();

        addSaveHandler();
        addCancelHandler();
    }

    private void addCancelHandler() {
        view.getCancelWidget().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {
                fillViewWithCurrentBook();
            }
        });
    }

    private void addSaveHandler() {
        view.getSaveWidget().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {

                final Book _book = new Book();

                _book.setId(book == null ? null : book.getId());
                _book.setAuthor(view.getBookAuthorWidget().getText().trim());
                _book.setCategory(view.getBookCategoryWidget().getText().trim());
                _book.setComment(view.getBookCommentWidget().getText().trim());
                _book.setEditor(view.getBookEditorWidget().getText().trim());
                _book.setTitle(view.getBookTitleWidget().getText().trim());

                final String yearText = view.getBookYearWidget().getText().trim();
                if (u.isVoid(yearText)) {
                    _book.setYear(null);

                } else {
                    try {
                        final Integer year = Integer.valueOf(yearText);
                        _book.setYear(year);
                    } catch (final IllegalArgumentException e) {
                        _book.setYear(null);
                    }

                }

                view.getWaitingIndicator().setVisible(true);

                adminBookService.saveBook(_book, new AsyncCallbackApp<Book>(eventBus) {

                    @Override
                    public void onSuccess(final Book savedBook) {

                        book = savedBook;
                        fillViewWithCurrentBook();

                        view.getWaitingIndicator().setVisible(false);

                        final Notification notification = view.newNotification();
                        notification.setHTML("El libro ha sido guardado con éxito");
                        notification.setHeading("Success");
                        notification.setLevel(Level.SUCCESS);

                        notification.show();
                    }

                    @Override
                    public void onFailure(final Throwable caught) {
                        view.getWaitingIndicator().setVisible(false);

                        final Notification notification = view.newNotification();
                        notification.setHTML( //
                                "Las modificaciones no han sido guardadas. <br>" + //
                                        "Un error ha ocurrido: " + caught.getMessage());
                        notification.setHeading("Error");
                        notification.setLevel(Level.ERROR);

                        notification.show();

                        super.onFailure(caught);
                    }

                });
            }
        });
    }

    private Book book = null;

    public void start(final Book book) {
        this.book = book;

        fillViewWithCurrentBook();

        view.show();
    }

    private void fillViewWithCurrentBook() {
        if (book == null) {
            view.getFormTitle().setText("Crear un libro");
            view.getBookAuthorWidget().setText("");
            view.getBookCategoryWidget().setText("");
            view.getBookCommentWidget().setText("");
            view.getBookEditorWidget().setText("");
            view.getBookTitleWidget().setText("");
            view.getBookYearWidget().setText("");

        } else {
            view.getFormTitle().setText("Modificar el libro");
            view.getBookAuthorWidget().setText(book.getAuthor());
            view.getBookCategoryWidget().setText(book.getCategory());
            view.getBookCommentWidget().setText(book.getComment());
            view.getBookEditorWidget().setText(book.getEditor());
            view.getBookTitleWidget().setText(book.getTitle());
            final Integer year = book.getYear();
            view.getBookYearWidget().setText(year == 0 ? "" : "" + year);

        }
    }

}
