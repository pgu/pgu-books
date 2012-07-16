package pgu.shared.dto;

import pgu.shared.utils.SortField;

import com.google.gwt.user.client.rpc.IsSerializable;

public class BooksSearch implements IsSerializable {

    private int       start;
    private int       length;
    private SortField sortField   = SortField.TITLE;
    private boolean   isAscending = true;

    private String    title;
    private String    author;
    private String    editor;
    private String    category;
    private String    year;
    private String    comment;
    private String    searchText;

    public BooksSearch copy() {
        final BooksSearch copy = new BooksSearch();
        copy.setAscending(isAscending);
        copy.setAuthor(author);
        copy.setCategory(category);
        copy.setComment(comment);
        copy.setEditor(editor);
        copy.setLength(length);
        copy.setSearchText(searchText);
        copy.setSortField(sortField);
        copy.setStart(start);
        copy.setTitle(title);
        copy.setYear(year);
        return copy;
    }

    @Override
    public String toString() {
        return "BooksSearch [start=" + start + ", length=" + length + ", sortField=" + sortField + ", isAscending="
                + isAscending + ", title=" + title + ", author=" + author + ", editor=" + editor + ", category="
                + category + ", year=" + year + ", comment=" + comment + ", searchText=" + searchText + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (author == null ? 0 : author.hashCode());
        result = prime * result + (category == null ? 0 : category.hashCode());
        result = prime * result + (comment == null ? 0 : comment.hashCode());
        result = prime * result + (editor == null ? 0 : editor.hashCode());
        result = prime * result + (isAscending ? 1231 : 1237);
        result = prime * result + length;
        result = prime * result + (searchText == null ? 0 : searchText.hashCode());
        result = prime * result + (sortField == null ? 0 : sortField.hashCode());
        result = prime * result + start;
        result = prime * result + (title == null ? 0 : title.hashCode());
        result = prime * result + (year == null ? 0 : year.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final BooksSearch other = (BooksSearch) obj;
        if (author == null) {
            if (other.author != null) {
                return false;
            }
        } else if (!author.equals(other.author)) {
            return false;
        }
        if (category == null) {
            if (other.category != null) {
                return false;
            }
        } else if (!category.equals(other.category)) {
            return false;
        }
        if (comment == null) {
            if (other.comment != null) {
                return false;
            }
        } else if (!comment.equals(other.comment)) {
            return false;
        }
        if (editor == null) {
            if (other.editor != null) {
                return false;
            }
        } else if (!editor.equals(other.editor)) {
            return false;
        }
        if (isAscending != other.isAscending) {
            return false;
        }
        if (length != other.length) {
            return false;
        }
        if (searchText == null) {
            if (other.searchText != null) {
                return false;
            }
        } else if (!searchText.equals(other.searchText)) {
            return false;
        }
        if (sortField != other.sortField) {
            return false;
        }
        if (start != other.start) {
            return false;
        }
        if (title == null) {
            if (other.title != null) {
                return false;
            }
        } else if (!title.equals(other.title)) {
            return false;
        }
        if (year == null) {
            if (other.year != null) {
                return false;
            }
        } else if (!year.equals(other.year)) {
            return false;
        }
        return true;
    }

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

    public int getStart() {
        return start;
    }

    public void setStart(final int start) {
        this.start = start;
    }

    public void setLength(final int length) {
        this.length = length;
    }

    public void setSearchText(final String text) {
        searchText = text;
    }

    public String getSearchText() {
        return searchText;
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

}
