package pgu.server.utils;

import javax.servlet.http.HttpServletRequest;

import pgu.server.app.AppLog;

public class CronUtils {

    private final AppUtils  u     = new AppUtils();
    private final MailUtils mailU = new MailUtils();

    public boolean isJobLauncherAuthorized(final HttpServletRequest req, final AppLog log) {
        if (u.isEnvProd()) {

            final String value = req.getHeader("X-AppEngine-Cron");
            if (!"true".equalsIgnoreCase(value)) {
                final String message = "The servlet has been called from something else than the cron job";
                mailU.sendMail(message, log);
                log.error(this, new UnsupportedOperationException(message));
                return false;
            }
        }

        return true;
    }

}
