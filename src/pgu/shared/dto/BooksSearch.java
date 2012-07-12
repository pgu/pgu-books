package pgu.shared.dto;

import com.google.gwt.user.client.rpc.IsSerializable;

public class BooksSearch implements IsSerializable {

    private String title;
    private String author;
    private String editor;
    private String category;
    private String year;
    private String comentario;

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

    public String getComentario() {
        return comentario;
    }

    public void setComentario(final String comentario) {
        this.comentario = comentario;
    }

}
