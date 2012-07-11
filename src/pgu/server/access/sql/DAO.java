package pgu.server.access.sql;

import pgu.server.domain.sql.BookId;

import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.util.DAOBase;

public class DAO extends DAOBase {

    static {
        ObjectifyService.register(BookId.class);
    }

}
