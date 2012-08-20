package pgu.server.ext;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.Normalizer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pgu.server.utils.AppUtils;

@SuppressWarnings("serial")
public class BooksEngineServlet extends HttpServlet {

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException,
            IOException {

        final String urlQuery = req.getParameter("urlQuery");
        String q = Normalizer.normalize(urlQuery, Normalizer.Form.NFD) //
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        try {
            q = URLEncoder.encode(q, AppUtils.UTF8);

        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        final PrintWriter w = resp.getWriter();
        w.write("<html>");
        w.write("<head>");
        w.write("<script>");

        w.write("  function loadIberLibros() {");
        w.write("    window.open(\"http://www.iberlibro.com/servlet/SearchResults" + q + "\", \"iberLibro\", \"\");");
        w.write("  }");

        w.write("</script>");
        w.write("</head>");
        w.write("<body onload=\"loadIberLibros();\"></body>");
        w.write("</html>");

    }

}
