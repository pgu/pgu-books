package pgu.server.utils;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import pgu.server.app.AppLog;

public class ServletUtils {

    private final AppLog log = new AppLog();

    public void listRequestHeaders(final HttpServletRequest req) {

        final Enumeration headerNames = req.getHeaderNames();
        while (headerNames.hasMoreElements()) {

            final String headerName = headerNames.nextElement().toString();
            log.info(this, "header: %s [%s]", headerName, req.getHeader(headerName));
        }
    }

}
