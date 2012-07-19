package pgu.server.job;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pgu.server.access.sql.DAO;
import pgu.server.app.AppLog;
import pgu.server.service.AdminBooksServiceImpl;
import pgu.shared.AppUtils;
import pgu.shared.domain.ImportResult;

import com.googlecode.objectify.Key;

@SuppressWarnings("serial")
public class JobImportBooks extends HttpServlet {

    private final AppLog                log     = new AppLog();
    private final DAO                   dao     = new DAO();
    private final AdminBooksServiceImpl service = new AdminBooksServiceImpl();
    private final AppUtils              u       = new AppUtils();

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException,
            IOException {
        log.info(this, "jobImportBooks GET..." + new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()));
        doPost(req, resp);
    }

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException,
            IOException {

        if (u.isEnvProd()) {

            final String value = req.getHeader("X-AppEngine-Cron");
            if (!"true".equalsIgnoreCase(value)) {
                final String message = "The servlet has been called from something else than the cron job";
                sendMail(message);
                log.error(this, new UnsupportedOperationException(message));
                return;
            }
        }

        final ImportResult importResult = dao.ofy().query(ImportResult.class).filter("lastImport", true).get();
        if (importResult == null) {
            final String message = "No previous import has been found";
            if (u.isEnvProd()) {
                sendMail(message);
            }
            log.warning(this, message);
            return;
        }

        if (!importResult.isDone()) {
            final String message = "Something went wrong with the last import. Fix it before using the automatic import";
            if (u.isEnvProd()) {
                sendMail(message);
            }
            log.warning(this, message);
            return;
        }

        service.setExternalServletContext(getServletContext());
        service.importBooks(importResult.getLastLineNb(), 50);

        cleanOldImportResults();
    }

    private void cleanOldImportResults() {
        final List<Key<ImportResult>> keyOldImportResults = dao.ofy().query(ImportResult.class) //
                .filter("lastImport", false) //
                .filter("done", true) //
                .filter("countImported", 0) //
                .listKeys();

        if (keyOldImportResults.size() > 48) {
            dao.ofy().delete(keyOldImportResults);
        }
    }

    private void sendMail(final String message) {
        log.info(this, "Send an email about [%s]", message);

        final Properties props = new Properties();
        final Session session = Session.getDefaultInstance(props, null);

        final Message msg = new MimeMessage(session);
        try {
            msg.setFrom(new InternetAddress("guilcher.pascal.dev@gmail.com", "pgu-books"));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress("guilcher.pascal@gmail.com", "owner"));
            msg.setSubject("Books import");
            msg.setText(message);

            Transport.send(msg);

        } catch (final UnsupportedEncodingException e) {
            log.error(this, e);
            throw new RuntimeException(e);

        } catch (final MessagingException e) {
            log.error(this, e);
            throw new RuntimeException(e);
        }
    }

}
