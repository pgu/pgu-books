package pgu.server.access.nosql;

import pgu.server.app.AppLog;

import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.Field;

public class DocUtils {

    private final AppLog log = new AppLog();

    public String text(final String fieldName, final Document doc) {
        final Field field = field(fieldName, doc);
        return field == null ? null : field.getText();
    }

    public Long numLong(final String fieldName, final Document doc) {
        final Double n = num(fieldName, doc);
        return n == null ? null : Long.valueOf(Math.round(n));
    }

    public Integer numInt(final String fieldName, final Document doc) {
        final Double n = num(fieldName, doc);
        return n == null ? null : Integer.valueOf((int) Math.round(n));
    }

    public Double num(final String fieldName, final Document doc) {
        final Field field = field(fieldName, doc);
        return field == null ? null : field.getNumber();
    }

    private Field field(final String fieldName, final Document doc) {
        if (doc.getFieldCount(fieldName) == 1) {
            return doc.getOnlyField(fieldName);
        }

        log.warning(this, "The field %s is present %s times", fieldName, doc.getFieldCount(fieldName));
        return null;
    }

}
