package pgu.server.job;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pgu.server.access.sql.DAO;
import pgu.server.app.AppLog;
import pgu.server.utils.AppUtils;
import pgu.server.utils.CronUtils;
import pgu.shared.domain.Book;
import pgu.shared.domain.BooksCount;

@SuppressWarnings("serial")
public class JobCountBooks extends HttpServlet {

    private final AppLog    log   = new AppLog();
    private final AppUtils  u     = new AppUtils();
    private final CronUtils cronU = new CronUtils();
    private final DAO       dao   = new DAO();

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException,
            IOException {
        log.info(this, "GET..." + u.now());
        doPost(req, resp);
    }

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException,
            IOException {

        if (!cronU.isJobLauncherAuthorized(req, log)) {
            return;
        }

        final int count = dao.ofy().query(Book.class).count();

        final BooksCount lastBooksCount = dao.ofy().query(BooksCount.class).filter("isLast", true).get();
        if (lastBooksCount != null) {
            lastBooksCount.setLast(false);
            dao.ofy().async().put(lastBooksCount);
        }

        final BooksCount booksCount = new BooksCount();
        booksCount.setCount(count);
        booksCount.setCountDate(u.now());
        booksCount.setLast(true);

        dao.ofy().async().put(booksCount);

    }

}
