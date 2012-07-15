package pgu.client.importBooks.ui;

import pgu.client.app.utils.Level;
import pgu.client.importBooks.ImportBooksPresenter;
import pgu.client.importBooks.ImportBooksView;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.SubmitButton;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class ImportBooksViewImpl extends Composite implements ImportBooksView {

    private static ImportBooksViewImplUiBinder uiBinder = GWT.create(ImportBooksViewImplUiBinder.class);

    interface ImportBooksViewImplUiBinder extends UiBinder<Widget, ImportBooksViewImpl> {
    }

    @UiField
    TextBox                      startBox, lengthBox;
    @UiField
    SubmitButton                 importBtn;
    @UiField
    TextBox                      msg;
    @UiField
    Button                       infoBtn, successBtn, warningBtn, errorBtn;

    private ImportBooksPresenter presenter;

    public ImportBooksViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public void setPresenter(final ImportBooksPresenter presenter) {
        this.presenter = presenter;
    }

    @UiHandler("importBtn")
    public void clickImport(final ClickEvent e) {
        final int length = Integer.valueOf(lengthBox.getText());
        final int start = Integer.valueOf(startBox.getText());

        presenter.importBooks(start, length);
    }

    @UiHandler("infoBtn")
    public void clickInfo(final ClickEvent e) {
        presenter.display(Level.INFO, msg.getText());
    }

    @UiHandler("successBtn")
    public void clickSuccess(final ClickEvent e) {
        presenter.display(Level.SUCCESS, msg.getText());
    }

    @UiHandler("warningBtn")
    public void clickWarning(final ClickEvent e) {
        presenter.display(Level.WARNING, msg.getText());
    }

    @UiHandler("errorBtn")
    public void clickError(final ClickEvent e) {
        presenter.display(Level.ERROR, msg.getText());
    }

}
