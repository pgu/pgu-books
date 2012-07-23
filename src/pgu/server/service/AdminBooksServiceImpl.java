package pgu.server.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.ServletContext;

import pgu.client.service.AdminBooksService;
import pgu.server.access.nosql.AppDoc;
import pgu.server.access.nosql.Search;
import pgu.server.access.sql.DAO;
import pgu.server.app.AppLog;
import pgu.server.domain.nosql.BookDoc;
import pgu.server.domain.nosql.DocType;
import pgu.server.domain.nosql.FieldValueDoc;
import pgu.server.domain.sql.FieldValue;
import pgu.server.utils.AppUtils;
import pgu.shared.domain.ArchivedBook;
import pgu.shared.domain.Book;
import pgu.shared.domain.ImportResult;

import com.google.appengine.api.datastore.QueryResultIterator;
import com.google.appengine.api.search.Query;
import com.google.appengine.api.search.Results;
import com.google.appengine.api.search.ScoredDocument;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class AdminBooksServiceImpl extends RemoteServiceServlet implements AdminBooksService {

    private final AppLog   log = new AppLog();
    private final Search   s   = new Search();
    private final DAO      dao = new DAO();
    private final AppUtils u   = new AppUtils();

    private ServletContext externalServletContext;

    public void setExternalServletContext(final ServletContext servletContext) {
        externalServletContext = servletContext;
    }

    @Override
    public ImportResult importBooks(final int start, final int length) {

        final ImportResult importResult = new ImportResult();
        importResult.setImportDate(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()));
        importResult.setLength(length);
        importResult.setStart(start);
        dao.ofy().put(importResult);

        final long startTime = System.currentTimeMillis();
        final int stop = start + length;
        log.info(this, "Importing books from %s to %s", start, stop);

        final ArrayList<String> misseds = new ArrayList<String>();
        Book lastBook = null;

        int counter = 0;
        int countImported = 0;

        String line = null;

        try {
            ServletContext servletContext = null;
            try {
                servletContext = getServletContext();

            } catch (final NullPointerException npe) {
                servletContext = externalServletContext;
            }

            final InputStream is = servletContext.getResourceAsStream("/WEB-INF/books/import/books.txt");
            final BufferedReader br = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            try {
                while ((line = br.readLine()) != null) {

                    if (counter < start) {
                        counter++;
                        continue;
                    }

                    if (counter >= stop) {
                        break;
                    }

                    counter++;
                    final String[] tokens = line.split("\", \"");
                    if (tokens.length == 6) {
                        countImported++;

                        final String authorRaw = tokens[0].trim();
                        final String author = authorRaw.isEmpty() ? "" : authorRaw.substring(1); // removes first "

                        final String categoryRaw = tokens[5].trim();
                        final String category = categoryRaw.isEmpty() ? "" : categoryRaw.substring(0,
                                categoryRaw.length() - 1); // removes last "

                        final String title = tokens[1].trim();
                        final String editor = tokens[2].trim();
                        final String _year = tokens[3].trim();
                        final Integer year = u.isVoid(_year) ? null : Integer.valueOf(_year);
                        final String comment = tokens[4].trim();

                        final Book book = new Book(author, title, editor, year, comment, category);
                        saveBook(book);
                        lastBook = book;

                    } else {
                        misseds.add(line);
                        log.warning(this, "The book %s has not been imported: %s", counter, line);
                    }
                }

                log.info(this, "Imported books: %s/%s in %s ms", countImported, length, System.currentTimeMillis()
                        - startTime);

                final ImportResult previousImportResult = dao.ofy().query(ImportResult.class)
                        .filter("lastImport", true).get();
                if (previousImportResult != null) {
                    previousImportResult.setLastImport(false);
                    dao.ofy().put(previousImportResult);
                }

                importResult.setCountImported(countImported);
                importResult.setLastLineNb(counter);
                importResult.setLastBook(lastBook == null ? "" : lastBook.toString());
                importResult.setMisseds(misseds);
                importResult.setDone(misseds.isEmpty());
                importResult.setLastImport(true);
                dao.ofy().put(importResult);

                return importResult;

            } catch (final IOException e) {
                log.error(this, e);

                importResult.setLastLineNb(counter);
                importResult.setLastBook(lastBook == null ? "" : lastBook.toString());
                importResult.setMisseds(misseds);
                importResult.setDone(false);
                importResult.setLastImport(true);
                dao.ofy().put(importResult);

                throw new RuntimeException(e);
            }
        } catch (final Throwable t) {
            log.error(this, t);

            importResult.setLastLineNb(counter);
            importResult.setLastBook(lastBook == null ? "" : lastBook.toString());
            importResult.setMisseds(misseds);
            importResult.setDone(false);
            importResult.setLastImport(true);
            dao.ofy().put(importResult);

            throw new RuntimeException(t);
        }
    }

    // https://developers.google.com/appengine/docs/java/search/overview
    @Override
    public Book saveBook(final Book book) {

        final String author = book.getAuthor();
        final String category = book.getCategory();
        final String comment = book.getComment();
        final String editor = book.getEditor();
        final String title = book.getTitle();
        final String str_year = book.getStrYear();

        final Long bookId = book.getId();
        if (null == bookId) { // creation

            // for each fields, save a doc
            saveFieldValue(BookDoc.AUTHOR._(), author);
            saveFieldValue(BookDoc.CATEGORY._(), category);
            saveFieldValue(BookDoc.COMMENT._(), comment);
            saveFieldValue(BookDoc.EDITOR._(), editor);
            saveFieldValue(BookDoc.TITLE._(), title);
            saveFieldValue(BookDoc.STR_YEAR._(), str_year);

            // generate id for the book
            dao.ofy().put(book);

        } else { // update

            final Book bookUI = book;
            final Book bookDB = dao.ofy().get(Book.class, bookId);

            updateBookField(BookDoc.AUTHOR._(), bookDB.getAuthor(), bookUI.getAuthor());
            updateBookField(BookDoc.CATEGORY._(), bookDB.getCategory(), bookUI.getCategory());
            updateBookField(BookDoc.COMMENT._(), bookDB.getComment(), bookUI.getComment());
            updateBookField(BookDoc.EDITOR._(), bookDB.getEditor(), bookUI.getEditor());
            updateBookField(BookDoc.TITLE._(), bookDB.getTitle(), bookUI.getTitle());
            updateBookField(BookDoc.STR_YEAR._(), bookDB.getStrYear(), bookUI.getStrYear());

            // update book
            dao.ofy().put(bookUI);
        }

        return book;
    }

    private void updateBookField(final String field, final String valueDB, final String valueUI) {
        if (!u.eq(valueUI, valueDB)) {

            updateFieldValue(field, valueDB);

            saveFieldValue(field, valueUI);
        }
    }

    private void saveFieldValue(final String field, final String value) {

        final FieldValue fv = dao.ofy().query(FieldValue.class) //
                .filter(FieldValueDoc.FIELD._(), field) //
                .filter(FieldValueDoc.VALUE._(), value) //
                .get();

        if (fv == null) {

            // create a sql fv
            final FieldValue newFv = new FieldValue();
            newFv.setField(field);
            newFv.setValue(value);
            dao.ofy().put(newFv);

            // create a doc fv
            final AppDoc valueDoc = new AppDoc() //
                    .text(FieldValueDoc.DOC_TYPE._(), DocType.FIELD_VALUE._()) //
                    .text(FieldValueDoc.FIELD._(), field) //
                    .text(FieldValueDoc.VALUE._(), value) //
                    .num(FieldValueDoc.FV_ID._(), newFv.getId()) //
            ;
            s.idx().add(valueDoc.build());

        } else {
            // update the counter of the sql fv
            fv.setCounter(fv.getCounter() + 1);
            dao.ofy().put(fv);
        }
    }

    private ScoredDocument fetchDocByBook(final Book book) {
        final Results<ScoredDocument> docs = s.idx().search(Query.newBuilder().build("" + //
                BookDoc.DOC_TYPE._() + ":" + DocType.BOOK._() + " " + //
                BookDoc.BOOK_ID._() + ":" + book.getId()) //
                );

        if (docs.getNumberReturned() == 1) {
            return docs.iterator().next();

        } else {
            final IllegalArgumentException e = new IllegalArgumentException(String.format(
                    "%s results have been found for the book id %s", docs.getNumberReturned(), book.getId()));
            log.error(this, e);
            throw e;
        }
    }

    @Override
    public void deleteAll() {

        final QueryResultIterator<FieldValue> fvItr = dao.ofy().query(FieldValue.class).iterator();
        while (fvItr.hasNext()) {
            dao.ofy().delete(fvItr.next());
        }

        final QueryResultIterator<Book> bookItr = dao.ofy().query(Book.class).iterator();
        while (bookItr.hasNext()) {
            dao.ofy().delete(bookItr.next());
        }

        final Iterator<ScoredDocument> fvDocItr = s.idx().search(Query.newBuilder().build("" + //
                FieldValueDoc.DOC_TYPE._() + ":" + DocType.FIELD_VALUE._())).iterator();
        while (fvDocItr.hasNext()) {
            s.idx().remove(fvDocItr.next().getId());
        }

        final Iterator<ScoredDocument> bookDocItr = s.idx().search(Query.newBuilder().build("" + //
                BookDoc.DOC_TYPE._() + ":" + DocType.BOOK._())).iterator();
        while (bookDocItr.hasNext()) {
            s.idx().remove(bookDocItr.next().getId());
        }

        final Iterator<ScoredDocument> archiveDocItr = s.archiveIdx().search(Query.newBuilder().build("" + //
                BookDoc.DOC_TYPE._() + ":" + DocType.ARCHIVE_BOOK._())).iterator();
        while (archiveDocItr.hasNext()) {
            s.archiveIdx().remove(archiveDocItr.next().getId());
        }
    }

    @Override
    public void deleteBooks(final ArrayList<Book> selectedBooks) {

        final String now = new SimpleDateFormat("yyyy/MM/dd HH:mm ss").format(new Date());
        final ArrayList<ArchivedBook> archivedBooks = new ArrayList<ArchivedBook>(selectedBooks.size());

        for (final Book book : selectedBooks) {

            updateFieldValue(BookDoc.AUTHOR._(), book.getAuthor());
            updateFieldValue(BookDoc.CATEGORY._(), book.getCategory());
            updateFieldValue(BookDoc.COMMENT._(), book.getComment());
            updateFieldValue(BookDoc.EDITOR._(), book.getEditor());
            updateFieldValue(BookDoc.TITLE._(), book.getTitle());
            updateFieldValue(BookDoc.STR_YEAR._(), book.getStrYear());

            archivedBooks.add(new ArchivedBook(book, now));
        }

        dao.ofy().put(archivedBooks);
        dao.ofy().delete(selectedBooks);
    }

    private void updateFieldValue(final String field, final String value) {

        final FieldValue fv = dao.ofy().query(FieldValue.class) //
                .filter(FieldValueDoc.FIELD._(), field) //
                .filter(FieldValueDoc.VALUE._(), value) //
                .get();

        if (fv == null) {
            final UnsupportedOperationException e = new UnsupportedOperationException( //
                    String.format( //
                            "We should have one entity for the field [%s] and the value [%s]" //
                            , field //
                            , value //
                    ));
            log.error(this, e);
            throw e;
        }

        fv.setCounter(fv.getCounter() - 1);

        if (fv.getCounter() == 0) {
            final Results<ScoredDocument> docs = s.idx().search(Query.newBuilder().build("" + //
                    FieldValueDoc.DOC_TYPE._() + ":" + DocType.FIELD_VALUE._() + " " + //
                    FieldValueDoc.FV_ID._() + "=" + fv.getId()) //
                    );

            if (docs.getResults().size() != 1) {
                final UnsupportedOperationException e = new UnsupportedOperationException( //
                        String.format( //
                                "We should have one doc for the fieldValue [%s]" //
                                , fv.getId() //
                        ));
                log.error(this, e);
                throw e;
            }

            final ScoredDocument fvDoc = docs.iterator().next();
            s.idx().remove(fvDoc.getId());

            dao.ofy().delete(fv); // delete
        } else {
            dao.ofy().put(fv); // update
        }
    }

}
