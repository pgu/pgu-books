package pgu.client.importBooks.ui;

import pgu.client.importBooks.ImportBooksView;

import com.github.gwtbootstrap.client.ui.base.TextBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class ImportBooksViewImpl extends Composite implements ImportBooksView {

    private static ImportBooksViewImplUiBinder uiBinder = GWT.create(ImportBooksViewImplUiBinder.class);

    interface ImportBooksViewImplUiBinder extends UiBinder<Widget, ImportBooksViewImpl> {
    }

    @UiField
    TextBox startBox, lengthBox;

    public ImportBooksViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
    }

}
