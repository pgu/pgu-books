package pgu.server.access.nosql;

import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.Field;
import com.google.appengine.api.search.ScoredDocument;

public class AppDoc {

    private final DocUtils         docU       = new DocUtils();

    private final Document.Builder docBuilder = Document.newBuilder();

    public AppDoc text(final String field, final String value) {
        docBuilder.addField(Field.newBuilder().setName(field).setText(value));
        return this;
    }

    public AppDoc num(final String field, final Integer value) {
        docBuilder.addField(Field.newBuilder().setName(field).setNumber(value));
        return this;
    }

    public AppDoc num(final String field, final Long value) {
        docBuilder.addField(Field.newBuilder().setName(field).setNumber(value));
        return this;
    }

    public Document build() {
        return docBuilder.build();
    }

    public AppDoc copyNumLong(final String field, final ScoredDocument originalDoc) {
        return num(field, docU.numLong(field, originalDoc));
    }

    public AppDoc copyText(final String field, final ScoredDocument originalDoc) {
        return text(field, docU.text(field, originalDoc));
    }

    public AppDoc copyNumInt(final String field, final ScoredDocument originalDoc) {
        return num(field, docU.numInt(field, originalDoc));
    }

    public AppDoc setId(final Long id) {
        docBuilder.setId("" + id);
        return this;
    }
}
