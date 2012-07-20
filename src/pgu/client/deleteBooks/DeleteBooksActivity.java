package pgu.client.deleteBooks;

import java.util.ArrayList;
import java.util.HashSet;

import pgu.client.app.event.RefreshBooksEvent;
import pgu.client.app.mvp.ClientFactory;
import pgu.client.app.utils.AsyncCallbackApp;
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

public class DeleteBooksActivity {

    private final EventBus                       eventBus;
    private final DeleteBooksView                view;
    private final AdminBooksServiceAsync         adminBookService;
    private final ArrayList<HandlerRegistration> handlerRegs = new ArrayList<HandlerRegistration>();

    public DeleteBooksActivity(final ClientFactory clientFactory) {
        eventBus = clientFactory.getEventBus();
        view = clientFactory.getDeleteBooksView();
        adminBookService = clientFactory.getAdminBooksService();

        handlerRegs.add(addCancelHandler());
        handlerRegs.add(addConfirmHandler());
        handlerRegs.add(addCloseHandler());
    }

    private HandlerRegistration addCloseHandler() {
        return view.getCloseHandler().addHiddenHandler(new HiddenHandler() {

            @Override
            public void onHidden(final HiddenEvent hiddenEvent) {
                for (HandlerRegistration handlerReg : handlerRegs) {
                    handlerReg.removeHandler();
                    handlerReg = null;
                }
                handlerRegs.clear();

                eventBus.fireEvent(new RefreshBooksEvent());
            }

        });
    }

    private final ArrayList<Book> selectedBooks = new ArrayList<Book>();

    public void start(final HashSet<Book> books) {
        selectedBooks.clear();

        if (books.isEmpty()) {
            return;
        }

        final StringBuilder sb = new StringBuilder();
        sb.append("Es usted seguro de querer borrar los libros seleccionados?<br>");
        sb.append("<ul>");
        for (final Book book : books) {
            sb.append("<li>");
            sb.append(book.getTitle());
            sb.append("</li>");
        }
        sb.append("</ul>");

        view.getMessageWidget().setHTML(sb.toString());
        view.show();

        selectedBooks.addAll(books);
    }

    private HandlerRegistration addConfirmHandler() {
        return view.getConfirmWidget().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {

                view.getWaitingIndicator().setVisible(true);

                adminBookService.deleteBooks(selectedBooks, new AsyncCallbackApp<Void>(eventBus) {

                    @Override
                    public void onSuccess(final Void result) {
                        view.getWaitingIndicator().setVisible(false);

                        final Notification notification = view.newNotification();
                        notification.setHTML("Los libros han sido borrados con éxito");
                        notification.setHeading("Success");
                        notification.setLevel(Level.SUCCESS);

                        notification.show();

                    }

                    @Override
                    public void onFailure(final Throwable caught) {
                        view.getWaitingIndicator().setVisible(false);

                        final Notification notification = view.newNotification();
                        notification.setHTML( //
                                "Los libros no han sido borrados. <br>" + //
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

    private HandlerRegistration addCancelHandler() {
        return view.getCancelWidget().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {
                view.hide();
            }

        });
    }

}
