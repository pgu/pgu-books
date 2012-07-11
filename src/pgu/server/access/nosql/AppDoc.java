package pgu.server.access.nosql;

import pgu.server.domain.nosql.BookDoc;

import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.Field;
import com.google.appengine.api.search.ScoredDocument;

public class AppDoc {

    private final DocUtils         docU       = new DocUtils();

    private final Document.Builder docBuilder = Document.newBuilder();

    public AppDoc text(final BookDoc bookDoc, final String value) {
        docBuilder.addField(Field.newBuilder().setName(bookDoc._()).setText(value));
        return this;
    }

    public AppDoc num(final BookDoc bookDoc, final Integer value) {
        docBuilder.addField(Field.newBuilder().setName(bookDoc._()).setNumber(value));
        return this;
    }

    public AppDoc num(final BookDoc bookDoc, final Long value) {
        docBuilder.addField(Field.newBuilder().setName(bookDoc._()).setNumber(value));
        return this;
    }

    public Document build() {
        return docBuilder.build();
    }

    public AppDoc copyNumLong(final BookDoc bookDoc, final ScoredDocument originalDoc) {
        return num(bookDoc, docU.numLong(bookDoc, originalDoc));
    }

    public AppDoc copyText(final BookDoc bookDoc, final ScoredDocument originalDoc) {
        return text(bookDoc, docU.text(bookDoc, originalDoc));
    }

    public AppDoc copyNumInt(final BookDoc bookDoc, final ScoredDocument originalDoc) {
        return num(bookDoc, docU.numInt(bookDoc, originalDoc));
    }
}
