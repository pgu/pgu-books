package pgu.server.job;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pgu.client.service.AdminBooksService;
import pgu.server.access.sql.DAO;
import pgu.server.app.AppLog;
import pgu.server.service.AdminBooksServiceImpl;
import pgu.shared.domain.ImportResult;

@SuppressWarnings("serial")
public class JobImportBooks extends HttpServlet {

    private final AppLog            log     = new AppLog();
    private final DAO               dao     = new DAO();
    private final AdminBooksService service = new AdminBooksServiceImpl();

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException,
            IOException {
        log.info(this, "jobImportBooks GET..." + new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()));
        doPost(req, resp);
    }

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException,
            IOException {

        final ImportResult importResult = dao.ofy().query(ImportResult.class).filter("lastImport", true).get();
        if (importResult == null) {
            log.warning(this, "No previous import has been found");
            return;
        }

        if (!importResult.isDone()) {
            log.warning(this, "Something went wrong with the last import. Fix it before using the automatic import");
            return;
        }

        service.importBooks(importResult.getLastLineNb(), 50);
    }

}
