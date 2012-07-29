package pgu.server.utils;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import pgu.server.app.AppLog;

public class MailUtils {

    private final AppUtils u = new AppUtils();

    public void sendMail(final String message, final AppLog log) {
        log.info(this, "Send an email about [%s]", message);

        if (!u.isEnvProd()) {
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
