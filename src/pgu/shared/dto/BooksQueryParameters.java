package pgu.shared.dto;

import java.util.ArrayList;

import pgu.shared.utils.SortField;

import com.google.gwt.user.client.rpc.IsSerializable;

public class BooksQueryParameters implements IsSerializable {

    private String            searchText;
    private ArrayList<String> selectedAuthors;
    private ArrayList<String> selectedEditors;
    private ArrayList<String> selectedCategories;
    private SortField         sortField;
    private boolean           isAscending = true;

    public BooksQueryParameters authors(final ArrayList<String> selectedAuthors) {
        checkNotNull(selectedAuthors);

        this.selectedAuthors = selectedAuthors;
        return this;
    }

    public BooksQueryParameters editors(final ArrayList<String> selectedEditors) {
        checkNotNull(selectedEditors);

        this.selectedEditors = selectedEditors;
        return this;
    }

    public BooksQueryParameters categories(final ArrayList<String> selectedCategories) {
        checkNotNull(selectedCategories);

        this.selectedCategories = selectedCategories;
        return this;
    }

    private static void checkNotNull(final ArrayList<String> selecteds) {
        if (null == selecteds) {
            throw new IllegalArgumentException();
        }
    }

    public ArrayList<String> getSelectedAuthors() {
        if (selectedAuthors == null) {
            selectedAuthors = new ArrayList<String>();
        }
        return selectedAuthors;
    }

    public ArrayList<String> getSelectedEditors() {
        if (selectedEditors == null) {
            selectedEditors = new ArrayList<String>();
        }
        return selectedEditors;
    }

    public ArrayList<String> getSelectedCategories() {
        if (selectedCategories == null) {
            selectedCategories = new ArrayList<String>();
        }
        return selectedCategories;
    }

    public String getSearchText() {
        if (searchText == null) {
            return "";
        }
        return searchText;
    }

    public void setSearchText(final String searchText) {
        this.searchText = searchText;
    }

    public SortField getSortField() {
        if (sortField == null) {
            return SortField.TITLE;
        }
        return sortField;
    }

    public boolean isAscending() {
        return isAscending;
    }

    public void setAscending(final boolean isAscending) {
        this.isAscending = isAscending;
    }

    public void setSortField(final SortField sortField) {
        this.sortField = sortField;
    }

    @Override
    public String toString() {
        return "BooksQueryParameters [searchText=" + searchText + ", selectedAuthors=" + selectedAuthors
                + ", selectedEditors=" + selectedEditors + ", selectedCategories=" + selectedCategories
                + ", sortField=" + sortField + ", isAscending=" + isAscending + "]";
    }

}
