package pgu.server.app;

import java.util.logging.Level;
import java.util.logging.Logger;

import pgu.shared.AppUtils;

public class AppLog {

    private final AppUtils u = new AppUtils();

    public void error(final Object o, final Throwable t) {
        logger(o).log(Level.SEVERE, t.getMessage(), t);
    }

    public void warning(final Object o, final String format, final Object... args) {
        final String msg = String.format(format, args);
        if (!u.isEnvProd()) {
            System.out.println(msg);
        }
        logger(o).log(Level.WARNING, msg);
    }

    public void info(final Object o, final String format, final Object... args) {
        final String msg = String.format(format, args);
        if (!u.isEnvProd()) {
            System.out.println(msg);
        }
        logger(o).log(Level.INFO, msg);
    }

    private Logger logger(final Object o) {
        return Logger.getLogger(o.getClass().getSimpleName());
    }

}
