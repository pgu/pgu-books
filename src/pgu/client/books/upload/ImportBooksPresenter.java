package pgu.client.books.upload;

import pgu.client.app.utils.Level;

public interface ImportBooksPresenter {

    void importBooks(int start, int length);

    void display(Level level, String text);

    void deleteAll();

}
