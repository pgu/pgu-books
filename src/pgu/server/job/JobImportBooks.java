package pgu.server.job;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pgu.server.app.AppLog;

@SuppressWarnings("serial")
public class JobImportBooks extends HttpServlet {

    private final AppLog log = new AppLog();

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException,
            IOException {
        log.info(this, "jobImportBooks GET..." + new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()));
        doPost(req, resp);
    }

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException,
            IOException {

    }

}
