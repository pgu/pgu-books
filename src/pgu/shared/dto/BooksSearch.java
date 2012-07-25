package pgu.shared.dto;

import java.util.HashMap;

import pgu.shared.utils.SortField;

import com.google.gwt.user.client.rpc.IsSerializable;

public class BooksSearch implements IsSerializable {

    private int                            length;
    private SortField                      sortField       = SortField.TITLE;
    private boolean                        isAscending     = true;

    private String                         title;
    private String                         author;
    private String                         editor;
    private String                         category;
    private String                         year;
    private String                         comment;

    private Integer                        pageDestination = 0;
    private final HashMap<Integer, String> pageNb2cursor   = new HashMap<Integer, String>();

    // TODO PGU Jul 25, 2012 review copy
    public BooksSearch copy() {
        final BooksSearch copy = new BooksSearch();
        copy.setAscending(isAscending);
        copy.setAuthor(author);
        copy.setCategory(category);
        copy.setComment(comment);
        copy.setEditor(editor);
        copy.setLength(length);
        copy.setSortField(sortField);
        copy.setTitle(title);
        copy.setYear(year);
        return copy;
    }

    // TODO PGU Jul 25, 2012 set equals/hashcode

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(final String author) {
        this.author = author;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(final String editor) {
        this.editor = editor;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(final String category) {
        this.category = category;
    }

    public String getYear() {
        return year;
    }

    public void setYear(final String year) {
        this.year = year;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(final String comment) {
        this.comment = comment;
    }

    public int getLength() {
        return length;
    }

    public void setLength(final int length) {
        this.length = length;
    }

    public SortField getSortField() {
        return sortField;
    }

    public void setSortField(final SortField sortField) {
        this.sortField = sortField;
    }

    public boolean isAscending() {
        return isAscending;
    }

    public void setAscending(final boolean isAscending) {
        this.isAscending = isAscending;
    }

    public HashMap<Integer, String> getPageNb2cursor() {
        return pageNb2cursor;
    }

    public Integer getPageDestination() {
        return pageDestination;
    }

    public void setPageDestination(final Integer pageDestination) {
        this.pageDestination = pageDestination;
    }

}
