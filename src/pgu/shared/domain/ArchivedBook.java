package pgu.shared.domain;

import javax.persistence.Id;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ArchivedBook implements IsSerializable {

    @Id
    private Long    id;
    private Long    bookId;
    private String  author;
    private String  editor;
    private Integer year;
    private String  title;
    private String  comment;
    private String  category;
    private String  archiveDate;

    public ArchivedBook() {
    }

    public ArchivedBook(final Book book, final String archiveDate) {
        bookId = book.getId();
        author = book.getAuthor();
        editor = book.getEditor();
        year = book.getYear();
        title = book.getTitle();
        comment = book.getComment();
        category = book.getCategory();
        this.archiveDate = archiveDate;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (id == null ? 0 : id.hashCode());
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
        final ArchivedBook other = (ArchivedBook) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ArchivedBook [id=" + id + ", bookId=" + bookId + ", author=" + author + ", editor=" + editor
                + ", year=" + year + ", title=" + title + ", comment=" + comment + ", category=" + category
                + ", archiveDate=" + archiveDate + "]";
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(final Long bookId) {
        this.bookId = bookId;
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

    public Integer getYear() {
        return year;
    }

    public void setYear(final Integer year) {
        this.year = year;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(final String comment) {
        this.comment = comment;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(final String category) {
        this.category = category;
    }

    public String getArchiveDate() {
        return archiveDate;
    }

    public void setArchiveDate(final String archiveDate) {
        this.archiveDate = archiveDate;
    }

}
