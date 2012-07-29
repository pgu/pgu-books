package pgu.server.job;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pgu.server.access.sql.DAO;
import pgu.server.app.AppLog;
import pgu.server.service.AdminBooksServiceImpl;
import pgu.server.utils.CronUtils;
import pgu.server.utils.MailUtils;
import pgu.shared.domain.ImportResult;

import com.googlecode.objectify.Key;

@SuppressWarnings("serial")
public class JobImportBooks extends HttpServlet {

    private final AppLog                log     = new AppLog();
    private final DAO                   dao     = new DAO();
    private final AdminBooksServiceImpl service = new AdminBooksServiceImpl();
    private final CronUtils             cronU   = new CronUtils();
    private final MailUtils             mailU   = new MailUtils();

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException,
            IOException {
        log.info(this, "GET..." + new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()));
        doPost(req, resp);
    }

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException,
            IOException {

        if (!cronU.isJobLauncherAuthorized(req, log)) {
            return;
        }

        final ImportResult importResult = dao.ofy().query(ImportResult.class).filter("lastImport", true).get();
        if (importResult == null) {
            final String message = "No previous import has been found";
            mailU.sendMail(message, log);
            log.warning(this, message);
            return;
        }

        if (!importResult.isDone()) {
            final String message = "Something went wrong with the last import. Fix it before using the automatic import";
            mailU.sendMail(message, log);
            log.warning(this, message);
            return;
        }

        service.setExternalServletContext(getServletContext());
        service.importBooks(importResult.getLastLineNb(), 5);

        cleanOldImportResults();
    }

    private void cleanOldImportResults() {
        final List<Key<ImportResult>> keyOldImportResults = dao.ofy().query(ImportResult.class) //
                .filter("lastImport", false) //
                .filter("done", true) //
                .filter("countImported", 0) //
                .listKeys();

        if (keyOldImportResults.size() > 48) {
            dao.ofy().async().delete(keyOldImportResults);
        }
    }

}
