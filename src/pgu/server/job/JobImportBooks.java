package pgu.server.job;

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
import pgu.server.service.AdminBooksServiceImpl;
import pgu.shared.domain.ImportResult;

@SuppressWarnings("serial")
public class JobImportBooks extends HttpServlet {

    private final AppLog                log     = new AppLog();
    private final DAO                   dao     = new DAO();
    private final AdminBooksServiceImpl service = new AdminBooksServiceImpl();

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException,
            IOException {
        log.info(this, "jobImportBooks GET..." + new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()));
        doPost(req, resp);
    }

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException,
            IOException {

        // ...has header X-AppEngine-Cron? ...
        final Enumeration headerNames = req.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            final Object headerName = headerNames.nextElement();
            log.info(this, "header: %s: %s", headerName, req.getHeader((String) headerName));
        }

        final ImportResult importResult = dao.ofy().query(ImportResult.class).filter("lastImport", true).get();
        if (importResult == null) {
            log.warning(this, "No previous import has been found");
            return;
        }

        if (!importResult.isDone()) {
            log.warning(this, "Something went wrong with the last import. Fix it before using the automatic import");
            return;
        }

        service.setExternalServletContext(getServletContext());
        service.importBooks(importResult.getLastLineNb(), 50);
    }

}
