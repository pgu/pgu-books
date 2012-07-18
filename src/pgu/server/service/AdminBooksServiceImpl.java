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
import java.util.Map;

import javax.servlet.ServletContext;

import pgu.client.service.AdminBooksService;
import pgu.server.access.nosql.AppDoc;
import pgu.server.access.nosql.Search;
import pgu.server.access.sql.DAO;
import pgu.server.app.AppLog;
import pgu.server.domain.nosql.BookDoc;
import pgu.server.domain.nosql.DocType;
import pgu.shared.AppUtils;
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
    public void saveBook(final Book book) {

        if (null == book.getId()) { // creation

            // generate id for the book
            dao.ofy().put(book);

            // create a doc for the book
            final AppDoc doc = new AppDoc() //
                    .setId(book.getId()) //
                    .text(BookDoc.DOC_TYPE, DocType.BOOK._()) //
                    .num(BookDoc.BOOK_ID, book.getId()) //
                    .text(BookDoc.AUTHOR, book.getAuthor()) //
                    .text(BookDoc.TITLE, book.getTitle()) //
                    .text(BookDoc.EDITOR, book.getEditor()) //
                    .num(BookDoc.YEAR, book.getYear()) //
                    .text(BookDoc.COMMENT, book.getComment()) //
                    .text(BookDoc.CATEGORY, book.getCategory()) //
            ;
            s.idx().add(doc.build());

        } else { // update

            // retrieves the doc with the book id
            final ScoredDocument currentDoc = fetchDocByBook(book);

            s.idx().remove(currentDoc.getId());

            // creates a new one with the same book id
            final AppDoc newDoc = new AppDoc() //
                    .setId(book.getId()) //
                    .text(BookDoc.DOC_TYPE, DocType.BOOK._()) //
                    .num(BookDoc.BOOK_ID, book.getId()) //
                    .text(BookDoc.AUTHOR, book.getAuthor()) //
                    .text(BookDoc.TITLE, book.getTitle()) //
                    .text(BookDoc.EDITOR, book.getEditor()) //
                    .num(BookDoc.YEAR, book.getYear()) //
                    .text(BookDoc.COMMENT, book.getComment()) //
                    .text(BookDoc.CATEGORY, book.getCategory()) //
            ;
            s.idx().add(newDoc.build());

            // update book
            dao.ofy().put(book);
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

        final QueryResultIterator<Book> bookItr = dao.ofy().query(Book.class).iterator();
        while (bookItr.hasNext()) {
            dao.ofy().delete(bookItr.next());
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

        final String now = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        final ArrayList<Long> bookIds = new ArrayList<Long>(selectedBooks.size());

        for (final Book book : selectedBooks) {
            final ScoredDocument doc = fetchDocByBook(book);

            final AppDoc archiveDoc = new AppDoc() //
                    .text(BookDoc.DOC_TYPE, DocType.ARCHIVE_BOOK._()) //
                    .copyNumLong(BookDoc.BOOK_ID, doc) //
                    .copyText(BookDoc.AUTHOR, doc) //
                    .copyText(BookDoc.TITLE, doc) //
                    .copyText(BookDoc.EDITOR, doc) //
                    .copyNumInt(BookDoc.YEAR, doc) //
                    .copyText(BookDoc.COMMENT, doc) //
                    .copyText(BookDoc.CATEGORY, doc) //
                    .text(BookDoc.ARCHIVE_DATE, now) //
            ;

            s.idx().remove(doc.getId()); // removes it from the index
            s.archiveIdx().add(archiveDoc.build()); // adds the archive

            bookIds.add(book.getId());
        }

        // clean books
        final Map<Long, Book> id2bookId = dao.ofy().get(Book.class, bookIds);
        dao.ofy().delete(id2bookId.values());
    }

}
