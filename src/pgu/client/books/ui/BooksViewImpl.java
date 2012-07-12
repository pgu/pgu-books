package pgu.client.books.ui;

import pgu.client.books.BooksView;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.ProgressBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class BooksViewImpl extends Composite implements BooksView {

    private static BooksViewImplUiBinder uiBinder = GWT.create(BooksViewImplUiBinder.class);

    interface BooksViewImplUiBinder extends UiBinder<Widget, BooksViewImpl> {
    }

    @UiField
    ProgressBar progressBar;
    @UiField
    Button      searchBtn;

    public BooksViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
        progressBar.setVisible(false);

    }

    @UiHandler("searchBtn")
    public void clickSearch(final ClickEvent e) {
        progressBar.setVisible(true);
    }
}
