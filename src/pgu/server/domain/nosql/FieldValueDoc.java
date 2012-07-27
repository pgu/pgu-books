package pgu.server.domain.nosql;

public enum FieldValueDoc {
    FIELD // field name
    , VALUE // value
    , FV_ID // FieldValue sql id
    ;

    public String _() {
        return toString().toLowerCase();
    }

}
