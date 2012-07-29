package pgu.server.utils;

import pgu.server.access.nosql.AppDoc;
import pgu.server.access.nosql.FieldValueIndex;
import pgu.server.access.sql.DAO;
import pgu.server.domain.nosql.FieldValueDoc;
import pgu.server.domain.sql.FieldValue;

public class FieldValueUtils {

    private final DAO             dao   = new DAO();
    private final FieldValueIndex fvIdx = new FieldValueIndex();

    public void saveFieldValue(final String field, final String value) {

        final FieldValue fv = dao.ofy().query(FieldValue.class) //
                .filter(FieldValueDoc.FIELD._(), field) //
                .filter(FieldValueDoc.VALUE._(), value) //
                .get();

        if (fv == null) {

            // create a sql fv
            final FieldValue newFv = new FieldValue();
            newFv.setField(field);
            newFv.setValue(value);
            dao.ofy().put(newFv);

            // create a doc fv
            final AppDoc fvDoc = new AppDoc() //
                    .text(FieldValueDoc.FIELD._(), field) //
                    .text(FieldValueDoc.VALUE._(), value) //
                    .num(FieldValueDoc.FV_ID._(), newFv.getId()) //
            ;
            fvIdx.idx().addAsync(fvDoc.build());

        } else {
            // update the counter of the sql fv
            fv.setCounter(fv.getCounter() + 1);
            dao.ofy().async().put(fv);
        }
    }

}
