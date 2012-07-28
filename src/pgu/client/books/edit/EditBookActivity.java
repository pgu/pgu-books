package pgu.client.books.edit;

import java.util.ArrayList;

import pgu.client.app.event.RefreshBooksEvent;
import pgu.client.app.mvp.ClientFactory;
import pgu.client.app.utils.AsyncCallbackApp;
import pgu.client.app.utils.ClientUtils;
import pgu.client.app.utils.Level;
import pgu.client.app.utils.Notification;
import pgu.client.service.AdminBooksServiceAsync;
import pgu.shared.domain.Book;

import com.github.gwtbootstrap.client.ui.event.HiddenEvent;
import com.github.gwtbootstrap.client.ui.event.HiddenHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;

public class EditBookActivity {

    private final EventBus                       eventBus;
    private final EditBookView                   view;
    private final ClientUtils                    u           = new ClientUtils();
    private final AdminBooksServiceAsync         adminBookService;
    private final ArrayList<HandlerRegistration> handlerRegs = new ArrayList<HandlerRegistration>();

    public EditBookActivity(final ClientFactory clientFactory) {
        eventBus = clientFactory.getEventBus();
        view = clientFactory.getBookView();
        adminBookService = clientFactory.getAdminBooksService();

        handlerRegs.add(addSaveHandler());
        handlerRegs.add(addCancelHandler());
        handlerRegs.add(addCloseHandler());
    }

    private HandlerRegistration addCloseHandler() {
        return view.getCloseHandler().addHiddenHandler(new HiddenHandler() {

            @Override
            public void onHidden(final HiddenEvent hiddenEvent) {
                for (final Notification notif : view.getNotifications()) {
                    if (notif != null) {
                        notif.removeFromParent();
                    }
                }

                for (HandlerRegistration handlerReg : handlerRegs) {
                    handlerReg.removeHandler();
                    handlerReg = null;
                }
                handlerRegs.clear();
            }

        });
    }

    private HandlerRegistration addCancelHandler() {
        return view.getCancelWidget().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {
                fillViewWithCurrentBook();
            }
        });
    }

    private HandlerRegistration addSaveHandler() {
        return view.getSaveWidget().addClickHandler(new ClickHandler() {

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

                        u.fire(eventBus, new RefreshBooksEvent());
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
