package pgu.server.domain.nosql;

public enum BookDoc {

    DOC_TYPE, BOOK_ID, //
    AUTHOR, TITLE, //
    EDITOR, YEAR, //
    COMMENT, CATEGORY, //
    ARCHIVE_DATE;

    public String _() {
        return toString();
    }

}
