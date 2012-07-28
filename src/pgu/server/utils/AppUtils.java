package pgu.server.utils;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import pgu.server.app.AppLog;

import com.google.appengine.api.utils.SystemProperty;

public class AppUtils {

    public boolean eq(final Object a, final Object b) {
        return a == b || a != null && a.equals(b);
    }

    public boolean isVoid(final String str) {
        return null == str || str.trim().isEmpty();
    }

    public boolean isEnvProd() {
        return SystemProperty.environment.value() == SystemProperty.Environment.Value.Production;
    }

    public boolean isJobLauncherAuthorized(final HttpServletRequest req, final AppLog log) {
        if (isEnvProd()) {

            final String value = req.getHeader("X-AppEngine-Cron");
            if (!"true".equalsIgnoreCase(value)) {
                final String message = "The servlet has been called from something else than the cron job";
                sendMail(message, log);
                log.error(this, new UnsupportedOperationException(message));
                return false;
            }
        }

        return true;
    }

    public void sendMail(final String message, final AppLog log) {
        log.info(this, "Send an email about [%s]", message);

        if (!isEnvProd()) {
            return;
        }

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
