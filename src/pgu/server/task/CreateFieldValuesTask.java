package pgu.server.task;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pgu.server.access.sql.DAO;
import pgu.server.app.AppLog;
import pgu.server.domain.nosql.BookDoc;
import pgu.server.utils.AppUtils;
import pgu.server.utils.FieldValueUtils;
import pgu.shared.domain.Book;

@SuppressWarnings("serial")
public class CreateFieldValuesTask extends HttpServlet {

    private final AppLog          log = new AppLog();
    private final DAO             dao = new DAO();
    private final AppUtils        u   = new AppUtils();
    private final FieldValueUtils fvU = new FieldValueUtils();

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException,
            IOException {
        log.info(this, "GET..." + new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()));
        doPost(req, resp);
    }

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException,
            IOException {

        final Enumeration headerNames = req.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            log.info(this, "header: %s", headerNames.nextElement());
        }

        final String bookId = req.getParameter("bookId");

        if (u.isVoid(bookId)) {

            log.warning(this, "The task %s has been called with an empty bookId", getServletName());
            return;
        }

        final Long _bookId = Long.valueOf(bookId);
        final Book book = dao.ofy().get(Book.class, _bookId);

        // for each fields, saves a doc
        fvU.saveFieldValue(BookDoc.AUTHOR._(), book.getAuthor());
        fvU.saveFieldValue(BookDoc.CATEGORY._(), book.getCategory());
        fvU.saveFieldValue(BookDoc.COMMENT._(), book.getComment());
        fvU.saveFieldValue(BookDoc.EDITOR._(), book.getEditor());
        fvU.saveFieldValue(BookDoc.TITLE._(), book.getTitle());
        fvU.saveFieldValue(BookDoc.STR_YEAR._(), book.getStrYear());

    }

}
