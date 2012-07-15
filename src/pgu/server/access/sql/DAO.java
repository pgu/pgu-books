package pgu.server.access.sql;

import pgu.shared.domain.Book;
import pgu.shared.domain.ImportResult;

import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.util.DAOBase;

public class DAO extends DAOBase {

    static {
        ObjectifyService.register(Book.class);
        ObjectifyService.register(ImportResult.class);
    }

}
