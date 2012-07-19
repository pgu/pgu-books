package pgu.client.book.ui;

import pgu.client.app.utils.Level;
import pgu.client.app.utils.Notification;
import pgu.client.books.BookView;

import com.github.gwtbootstrap.client.ui.AlertBlock;
import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Modal;
import com.github.gwtbootstrap.client.ui.ProgressBar;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.github.gwtbootstrap.client.ui.constants.AlertType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasVisibility;
import com.google.gwt.user.client.ui.Widget;

public class BookViewImpl extends Composite implements BookView {

    private static BookViewImplUiBinder uiBinder = GWT.create(BookViewImplUiBinder.class);

    interface BookViewImplUiBinder extends UiBinder<Widget, BookViewImpl> {
    }

    @UiField
    Modal       container;
    @UiField
    TextBox     titleBox, authorBox, editorBox, categoryBox, yearBox, commentBox;
    @UiField
    Button      saveBtn, cancelBtn;
    @UiField
    ProgressBar progressBar;
    @UiField
    HTMLPanel   notification;

    public BookViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
        progressBar.setVisible(false);
        notification.setVisible(false);
    }

    @Override
    public HasText getBookTitleWidget() {
        return titleBox;
    }

    @Override
    public HasText getBookAuthorWidget() {
        return authorBox;
    }

    @Override
    public HasText getBookCategoryWidget() {
        return categoryBox;
    }

    @Override
    public HasText getBookCommentWidget() {
        return commentBox;
    }

    @Override
    public HasText getBookEditorWidget() {
        return editorBox;
    }

    @Override
    public HasText getBookYearWidget() {
        return yearBox;
    }

    @Override
    public HasClickHandlers getSaveWidget() {
        return saveBtn;
    }

    @Override
    public void show() {
        container.show();
    }

    @Override
    public HasText getFormTitle() {
        return new HasText() {

            @Override
            public void setText(final String text) {
                container.setTitle(text);
            }

            @Override
            public String getText() {
                return container.getTitle();
            }
        };
    }

    @Override
    public HasVisibility getWaitingIndicator() {
        return progressBar;
    }

    @Override
    public Notification newNotification() {
        return new Notification() {

            private boolean    hasCloseAction = false;
            private String     heading;
            private String     text;
            private AlertBlock alert;

            @Override
            public String getHTML() {
                return text;
            }

            @Override
            public void setHTML(final String text) {
                this.text = text;
            }

            @Override
            public void show() {
                notification.add(alert);

                if (!hasCloseAction) {
                    addTimerForClosingNotification();
                }
            }

            @Override
            public void hide() {
                alert.close();
            }

            @Override
            public void toggle() {
                throw new UnsupportedOperationException();
            }

            @Override
            public void setLevel(final Level level) {
                if (Level.ERROR == level) {
                    alert = new AlertBlock(AlertType.ERROR);
                    alert.setClose(true);
                    hasCloseAction = true;

                } else if (Level.INFO == level) {
                    alert = new AlertBlock(AlertType.INFO);
                    alert.setClose(false);
                    hasCloseAction = false;

                } else if (Level.SUCCESS == level) {
                    alert = new AlertBlock(AlertType.SUCCESS);
                    alert.setClose(false);
                    hasCloseAction = false;

                } else if (Level.WARNING == level) {
                    alert = new AlertBlock(AlertType.WARNING);
                    alert.setClose(false);
                    hasCloseAction = false;

                } else {
                    alert = new AlertBlock(AlertType.DEFAULT);
                    alert.setClose(false);
                    hasCloseAction = false;

                }
                alert.setAnimation(true);
                alert.setHTML(text);
                alert.setHeading(heading);
            }

            @Override
            public void setHeading(final String heading) {
                this.heading = heading;
            }

            private void addTimerForClosingNotification() {
                new Timer() {

                    @Override
                    public void run() {
                        alert.close();
                    }

                }.schedule(5000);
            }

            @Override
            public String getText() {
                return text;
            }

            @Override
            public void setText(final String text) {
                this.text = text;
            }
        };
    }

    @Override
    public HasClickHandlers getCancelWidget() {
        return cancelBtn;
    }

}
