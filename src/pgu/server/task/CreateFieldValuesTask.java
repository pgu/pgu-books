package pgu.server.task;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pgu.server.access.sql.DAO;
import pgu.server.app.AppLog;
import pgu.server.domain.nosql.BookDoc;
import pgu.server.utils.AppUtils;
import pgu.server.utils.FieldValueUtils;
import pgu.server.utils.QueueUtils;
import pgu.shared.domain.Book;

@SuppressWarnings("serial")
public class CreateFieldValuesTask extends HttpServlet {

    private final AppLog          log    = new AppLog();
    private final DAO             dao    = new DAO();
    private final AppUtils        u      = new AppUtils();
    private final FieldValueUtils fvU    = new FieldValueUtils();
    private final QueueUtils      queueU = new QueueUtils();

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException,
            IOException {
        log.info(this, "GET..." + u.now());
        doPost(req, resp);
    }

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException,
            IOException {

        if (!queueU.isTaskLauncherAuthorized(req, log)) {
            return;
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
