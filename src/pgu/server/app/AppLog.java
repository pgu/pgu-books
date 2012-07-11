package pgu.server.app;

import java.util.logging.Level;
import java.util.logging.Logger;

public class AppLog {

    public void error(final Object o, final Throwable t) {
        logger(o).log(Level.SEVERE, t.getMessage(), t);
    }

    public void warning(final Object o, final String format, final Object... args) {
        logger(o).log(Level.WARNING, String.format(format, args));
    }

    private Logger logger(final Object o) {
        return Logger.getLogger(o.getClass().getSimpleName());
    }

    public void info(final Object o, final String format, final Object... args) {
        logger(o).log(Level.INFO, String.format(format, args));
    }

}
