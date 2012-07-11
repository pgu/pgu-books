package pgu.server.access.nosql;

import pgu.server.app.AppLog;
import pgu.server.domain.nosql.BookDoc;

import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.Field;

public class DocUtils {

    private final AppLog log = new AppLog();

    public String text(final BookDoc fieldName, final Document doc) {
        final Field field = field(fieldName, doc);
        return field == null ? null : field.getText();
    }

    public Long numLong(final BookDoc fieldName, final Document doc) {
        final String text = text(fieldName, doc);
        return text == null ? null : Long.getLong(text);
    }

    public Integer numInt(final BookDoc fieldName, final Document doc) {
        final String text = text(fieldName, doc);
        return text == null ? null : Integer.getInteger(text);
    }

    public Double num(final BookDoc fieldName, final Document doc) {
        final Field field = field(fieldName, doc);
        return field == null ? null : field.getNumber();
    }

    private Field field(final BookDoc fieldName, final Document doc) {
        final String name = fieldName._();
        if (doc.getFieldCount(name) == 1) {
            return doc.getOnlyField(name);
        }

        log.warning(this, "The field %s is present %s times", name, doc.getFieldCount(name));
        return null;
    }

}
