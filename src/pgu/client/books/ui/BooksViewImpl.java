package pgu.client.books.ui;

import pgu.client.books.BooksView;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;

public class BooksViewImpl extends Composite implements HasText, BooksView {

    private static BooksViewImplUiBinder uiBinder = GWT.create(BooksViewImplUiBinder.class);

    interface BooksViewImplUiBinder extends UiBinder<Widget, BooksViewImpl> {
    }

    public BooksViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @UiField
    Button button;

    public BooksViewImpl(final String firstName) {
        initWidget(uiBinder.createAndBindUi(this));
        button.setText(firstName);
    }

    @UiHandler("button")
    void onClick(final ClickEvent e) {
        Window.alert("Hello!");
    }

    @Override
    public void setText(final String text) {
        button.setText(text);
    }

    @Override
    public String getText() {
        return button.getText();
    }

}
