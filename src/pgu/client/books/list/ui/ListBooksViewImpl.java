package pgu.client.books.list.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;

import pgu.client.app.utils.ClientUtils;
import pgu.client.app.utils.HasClickAndEnable;
import pgu.client.app.utils.HasClickAndVisibility;
import pgu.client.books.list.ListBooksPresenter;
import pgu.client.books.list.ListBooksView;
import pgu.client.service.AdminBooksService;
import pgu.client.service.AdminBooksServiceAsync;
import pgu.shared.domain.Book;
import pgu.shared.utils.SortField;

import com.github.gwtbootstrap.client.ui.Badge;
import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Column;
import com.github.gwtbootstrap.client.ui.FluidContainer;
import com.github.gwtbootstrap.client.ui.FluidRow;
import com.github.gwtbootstrap.client.ui.Pager;
import com.github.gwtbootstrap.client.ui.base.InlineLabel;
import com.github.gwtbootstrap.client.ui.constants.ButtonType;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.github.gwtbootstrap.client.ui.resources.ButtonSize;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class ListBooksViewImpl extends Composite implements ListBooksView {

    private static ListBooksViewImplUiBinder uiBinder = GWT.create(ListBooksViewImplUiBinder.class);

    interface ListBooksViewImplUiBinder extends UiBinder<Widget, ListBooksViewImpl> {
    }

    @UiField
    FluidContainer                              booksGrid, toolBar;
    @UiField
    Pager                                       pager, pagerTop;
    @UiField
    Button                                      addBtn, editBtn, deleteBtn, refreshBtn, priceBtn;
    @UiField
    Button                                      updateDocBtn, deleteDuplicatesBtn;
    @UiField
    Badge                                       results10, results20, results30, results50;

    private ListBooksPresenter                  presenter;

    private SortHeader                          titleCol, authorCol, editorCol, categoryCol, yearCol;

    private final HashSet<FluidRow>             selectedRows = new HashSet<FluidRow>();
    private final LinkedHashMap<FluidRow, Book> row2book     = new LinkedHashMap<FluidRow, Book>();

    private ClickHandler leftClick = null;
    private ClickHandler rightClick = null;

    public ListBooksViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));

        deleteDuplicatesBtn.setVisible(true); // hack for me
        updateDocBtn.setVisible(false); // hack for me

        pager.setVisible(false);
        pagerTop.setVisible(false);

        refreshBtn.setVisible(false); // TBD

        initBadges();
        initHeadersRow();

        pagerTop.getLeft().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {
                if (leftClick == null) {
                    return;
                }

                leftClick.onClick(event);
            }
        });
        pagerTop.getRight().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {
                if (rightClick == null) {
                    return;
                }

                rightClick.onClick(event);
            }
        });
    }

    private final AdminBooksServiceAsync      adminService      = GWT.create(AdminBooksService.class);

    @UiHandler("deleteDuplicatesBtn")
    public void clickDeleteDulicates(final ClickEvent e) {

        final HashSet<Book> duplicates = new HashSet<Book>();

        final Collection<Book> books = row2book.values();

        for (final Book book1 : books) {

            if (duplicates.contains(book1)) { // already tagged as a duplicate
                continue;
            }

            for (final Book book2 : books) {

                if (book2.equals(book1)) { // by id
                    continue;
                }

                if ( //
                        equal(book1.getAuthor(), book2.getAuthor()) //
                        &&  equal(book1.getCategory(), book2.getCategory()) //
                        &&  equal(book1.getComment(), book2.getComment()) //
                        &&  equal(book1.getEditor(), book2.getEditor()) //
                        &&  equal(book1.getTitle(), book2.getTitle()) //
                        &&  equal(book1.getYear(), book2.getYear()) //
                        //
                        ) { // is duplicate
                    duplicates.add(book2);
                    break;
                }

            }
        }

        presenter.deleteDuplicates(duplicates);
    }

    private final ClientUtils                    u           = new ClientUtils();

    private boolean equal(final Object a, final Object b) {
        return a == b || a != null && a.equals(b);
    }

    @UiHandler("updateDocBtn")
    public void clickRemove(final ClickEvent e) {
        adminService.updateDocFromIdx(new AsyncCallback<Void>() {

            @Override
            public void onFailure(final Throwable caught) {
                Window.alert("Fail");
            }

            @Override
            public void onSuccess(final Void result) {
                Window.alert("Success");
            }

        });
    }

    private void initHeadersRow() {

        titleCol = new SortHeader("Título", SortField.TITLE, 3);
        authorCol = new SortHeader("Autor", SortField.AUTHOR);
        editorCol = new SortHeader("Editor", SortField.EDITOR);
        categoryCol = new SortHeader("Categoría", SortField.CATEGORY);
        yearCol = new SortHeader("Año", SortField.YEAR, 1);

        final Column commentCol = new Column(2);
        commentCol.add(getColumnLabel("Comentario"));

        final FluidRow headersRow = new FluidRow();
        headersRow.addStyleName("my-show-grid-header");
        headersRow.addStyleName("row-header");
        headersRow.add(titleCol);
        headersRow.add(authorCol);
        headersRow.add(editorCol);
        headersRow.add(categoryCol);
        headersRow.add(yearCol);
        headersRow.add(commentCol);
        booksGrid.add(headersRow);
    }

    private void initBadges() {
        for (final Badge b : Arrays.asList( //
                results10, //
                results20, //
                results30, //
                results50 //
                )) {

            b.addClickHandler(new ClickHandler() {

                @Override
                public void onClick(final ClickEvent event) {
                    presenter.updateResultsPerPage(Integer.valueOf(b.getText()));
                }
            });
        }
    }

    private class SortHeader extends Column {
        private Button          upBtn;
        private Button          downBtn;
        private final SortField sortField;

        public SortHeader(final String text, final SortField sortField) {
            this(text, sortField, 2);
        }

        public SortHeader(final String text, final SortField sortField, final int size) {
            super(size);

            this.sortField = sortField;
            add(getColumnLabel(text));

            // addSortWidgets(sortField); disable it for now
        }

        private void addSortWidgets(final SortField sortField) {
            upBtn = new Button();
            upBtn.setSize(ButtonSize.MINI);
            upBtn.setIcon(IconType.SORT_UP);

            downBtn = new Button();
            downBtn.setSize(ButtonSize.MINI);
            downBtn.setIcon(IconType.SORT_DOWN);

            add(upBtn);
            add(downBtn);

            upBtn.addClickHandler(new ClickHandler() {

                @Override
                public void onClick(final ClickEvent event) {
                    presenter.updateSort(sortField, true);
                }
            });
            downBtn.addClickHandler(new ClickHandler() {

                @Override
                public void onClick(final ClickEvent event) {
                    presenter.updateSort(sortField, false);
                }
            });
        }

    }

    private InlineLabel getColumnLabel(final String text) {
        final InlineLabel label = new InlineLabel(text);
        label.getElement().getStyle().setMarginRight(7, Unit.PX);
        return label;
    }

    @Override
    public void setPresenter(final ListBooksPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setBooks(final ArrayList<Book> books, final boolean isEditable) {

        // do not remove the header's row
        for (int i = booksGrid.getWidgetCount() - 1; i > 0; i--) {
            booksGrid.remove(i);
        }

        row2book.clear();
        selectedRows.clear();

        int count = 0;
        for (final Book book : books) {

            final Column titleCol = new Column(3);
            final Column authorCol = new Column(2);
            final Column editorCol = new Column(2);
            final Column categoryCol = new Column(2);
            final Column yearCol = new Column(1);
            final Column commentCol = new Column(2);

            titleCol.getElement().setInnerText(book.getTitle());
            authorCol.getElement().setInnerText(book.getAuthor());
            editorCol.getElement().setInnerText(book.getEditor());
            categoryCol.getElement().setInnerText(book.getCategory());
            yearCol.getElement().setInnerText("" + (book.getYear() == 0 ? "" : book.getYear()));
            commentCol.getElement().setInnerText(book.getComment());

            final FluidRow row = new FluidRow();
            row.addStyleName("my-show-grid");
            row.addStyleName((count & 1) == 1 ? "row-odd" : "row-even");
            row.add(titleCol);
            row.add(authorCol);
            row.add(editorCol);
            row.add(categoryCol);
            row.add(yearCol);
            row.add(commentCol);

            if (isEditable) {
                row.addStyleName("row-editable");
                row2book.put(row, book);
                row.addDomHandler(new ClickHandler() {

                    @Override
                    public void onClick(final ClickEvent event) {

                        if (event.isControlKeyDown()) { // multi selection
                            selectWithControlKey(row);

                        } else if (event.isShiftKeyDown()) { // multi selection

                            if (selectedRows.size() != 1 //
                                    || selectedRows.contains(row)) {
                                return;
                            }

                            selectWithShiftKey(row);

                        } else { // single selection

                            final boolean wasSelected = selectedRows.contains(row) //
                                    && selectedRows.size() == 1;

                            for (final FluidRow selectedRow : selectedRows) {
                                selectedRow.removeStyleName("row-selected");
                            }
                            selectedRows.clear();

                            if (!wasSelected) {
                                selectRow(row);
                            }
                        }
                    }

                    private void selectWithControlKey(final FluidRow row) {
                        if (selectedRows.contains(row)) {
                            deselectRow(row);
                        } else {
                            selectRow(row);
                        }
                    }

                    private void selectWithShiftKey(final FluidRow row) {

                        final FluidRow borderRowA = selectedRows.iterator().next();
                        final FluidRow borderRowB = row;
                        final ArrayList<FluidRow> borderRows = new ArrayList<FluidRow>();
                        borderRows.add(borderRowA);
                        borderRows.add(borderRowB);

                        boolean isIn = false;
                        for (final FluidRow _row : row2book.keySet()) {
                            if (borderRows.contains(_row) //
                                    && !isIn) {

                                isIn = true;

                                if (!selectedRows.contains(_row)) {
                                    selectRow(_row);
                                }

                            } else if (borderRows.contains(_row) //
                                    && isIn) {

                                if (!selectedRows.contains(_row)) {
                                    selectRow(_row);
                                }
                                break;

                            } else {
                                if (isIn) {
                                    selectRow(_row);
                                }
                            }
                        }
                    }

                    private void deselectRow(final FluidRow row) {
                        selectedRows.remove(row);
                        row.removeStyleName("row-selected");
                    }

                    private void selectRow(final FluidRow _row) {
                        selectedRows.add(_row);
                        _row.addStyleName("row-selected");
                    }

                }, ClickEvent.getType());
            }

            booksGrid.add(row);
            count++;
        }

        scrollToTop();
    }

    private native void scrollToTop() /*-{
        $wnd.$("html, body").animate({ scrollTop: 0 }, 600);
    }-*/;

    @Override
    public HasClickAndVisibility getDeleteBooksWidget() {
        return new HasClickAndVisibility() {

            @Override
            public boolean isVisible() {
                return deleteBtn.isVisible();
            }

            @Override
            public void setVisible(final boolean visible) {
                //                updateDocBtn.setVisible(visible); // hack for me
                deleteDuplicatesBtn.setVisible(visible); // hack for me
                deleteBtn.setVisible(visible);
            }

            @Override
            public HandlerRegistration addClickHandler(final ClickHandler handler) {
                return deleteBtn.addClickHandler(handler);
            }

            @Override
            public void fireEvent(final GwtEvent<?> event) {
                deleteBtn.fireEvent(event);
            }

        };
    }

    @Override
    public HasClickAndVisibility getRefreshBooksWidget() {
        return new HasClickAndVisibility() {

            @Override
            public boolean isVisible() {
                return refreshBtn.isVisible();
            }

            @Override
            public void setVisible(final boolean visible) {
                refreshBtn.setVisible(visible);
            }

            @Override
            public HandlerRegistration addClickHandler(final ClickHandler handler) {
                return refreshBtn.addClickHandler(handler);
            }

            @Override
            public void fireEvent(final GwtEvent<?> event) {
                refreshBtn.fireEvent(event);
            }

        };
    }

    @Override
    public HasClickAndVisibility getEditBookWidget() {
        return new HasClickAndVisibility() {

            @Override
            public boolean isVisible() {
                return editBtn.isVisible();
            }

            @Override
            public void setVisible(final boolean visible) {
                editBtn.setVisible(visible);
            }

            @Override
            public HandlerRegistration addClickHandler(final ClickHandler handler) {
                return editBtn.addClickHandler(handler);
            }

            @Override
            public void fireEvent(final GwtEvent<?> event) {
                editBtn.fireEvent(event);
            }

        };
    }

    @Override
    public HasClickAndVisibility getCreateBookWidget() {
        return new HasClickAndVisibility() {

            @Override
            public void fireEvent(final GwtEvent<?> event) {
                addBtn.fireEvent(event);
            }

            @Override
            public HandlerRegistration addClickHandler(final ClickHandler handler) {
                return addBtn.addClickHandler(handler);
            }

            @Override
            public void setVisible(final boolean visible) {
                addBtn.setVisible(visible);
            }

            @Override
            public boolean isVisible() {
                return addBtn.isVisible();
            }
        };
    }

    @Override
    public Book getSelectedBook() {
        if (selectedRows.size() != 1) {
            return null;
        }

        return row2book.get(selectedRows.iterator().next());
    }

    @Override
    public HashSet<Book> getSelectedBooks() {
        final HashSet<Book> books = new HashSet<Book>();
        for (final FluidRow row : selectedRows) {
            books.add(row2book.get(row));
        }

        return books;
    }

    @Override
    public HasClickAndEnable getPreviousPageWidget() {
        return new HasClickAndEnable() {

            @Override
            public boolean isEnabled() {
                return !pager.getLeft().isDisabled();
            }

            @Override
            public void setEnabled(final boolean enabled) {
                pagerTop.getLeft().setDisabled(!enabled);
                pager.getLeft().setDisabled(!enabled);
            }

            @Override
            public HandlerRegistration addClickHandler(final ClickHandler handler) {
                leftClick = handler;
                return pager.getLeft().addClickHandler(handler);
            }

            @Override
            public void fireEvent(final GwtEvent<?> event) {
                pager.getLeft().fireEvent(event);
            }

        };
    }

    @Override
    public HasClickAndEnable getNextPageWidget() {
        return new HasClickAndEnable() {

            @Override
            public boolean isEnabled() {
                return !pager.getRight().isDisabled();
            }

            @Override
            public void setEnabled(final boolean enabled) {
                pagerTop.getRight().setDisabled(!enabled);
                pager.getRight().setDisabled(!enabled);
            }

            @Override
            public HandlerRegistration addClickHandler(final ClickHandler handler) {
                rightClick = handler;
                return pager.getRight().addClickHandler(handler);
            }

            @Override
            public void fireEvent(final GwtEvent<?> event) {
                pager.getRight().fireEvent(event);
            }

        };
    }

    @Override
    public void setResultsPerPage(final int length) {
        for (final Badge b : Arrays.asList( //
                results10, //
                results20, //
                results30, //
                results50 //
                )) {

            final boolean isSelected = length == Integer.valueOf(b.getText());
            if (isSelected) {
                b.addStyleName("badge-inverse");

            } else {
                b.removeStyleName("badge-inverse");

            }
        }
    }

    @Override
    public void setCurrentSort(final SortField sortField, final boolean isAscending) {
        for (final SortHeader h : Arrays.asList( //
                titleCol, //
                authorCol, //
                editorCol, //
                categoryCol, //
                yearCol //
                )) {

            if (h.sortField == sortField) {
                h.upBtn.setType(isAscending ? ButtonType.INVERSE : ButtonType.DEFAULT);
                h.downBtn.setType(isAscending ? ButtonType.DEFAULT : ButtonType.INVERSE);

            } else {
                h.upBtn.setType(ButtonType.DEFAULT);
                h.downBtn.setType(ButtonType.DEFAULT);
            }

        }
    }

    @Override
    public void updatePager(final boolean isFirstPage, final boolean hasNextPage, final int nbBooks) {
        pager.getLeft().setVisible(!isFirstPage);
        pager.getRight().setVisible(hasNextPage);

        pager.setVisible(nbBooks > 0);

        pagerTop.getLeft().setVisible(!isFirstPage);
        pagerTop.getRight().setVisible(hasNextPage);

        pagerTop.setVisible(nbBooks > 0);
    }

    @Override
    public HasClickAndVisibility getSearchPriceBooksWidget() {
        return new HasClickAndVisibility() {

            @Override
            public void fireEvent(final GwtEvent<?> event) {
                priceBtn.fireEvent(event);
            }

            @Override
            public HandlerRegistration addClickHandler(final ClickHandler handler) {
                return priceBtn.addClickHandler(handler);
            }

            @Override
            public void setVisible(final boolean visible) {
                priceBtn.setVisible(visible);
            }

            @Override
            public boolean isVisible() {
                return priceBtn.isVisible();
            }
        };
    }

    /**
     * no success with this one :-/
     */
    private native static void disableNativeSelectionTextWithShift(Element e, boolean disable)/*-{
		if (disable) {
			e.ondrag = function() {
				return false;
			};
			e.onselectstart = function() {
				return false;
			};

			e.style.KhtmlUserSelect = "none";
			e.style.MozUserSelect = "none";
			e.style.UserSelect = "none";

		} else {
			e.ondrag = null;
			e.onselectstart = null;

			e.style.KhtmlUserSelect = "text";
			e.style.MozUserSelect = "text";
			e.style.UserSelect = "text";
		}
    }-*/;

}
