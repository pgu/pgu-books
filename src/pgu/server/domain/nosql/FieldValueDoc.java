package pgu.server.domain.nosql;

public enum FieldValueDoc {
    DOC_TYPE //
    , FIELD // field name
    , VALUE // value
    , FV_ID // FieldValue sql id
    ;

    public String _() {
        return toString().toLowerCase();
    }

}
