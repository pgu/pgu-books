package pgu.server.access.nosql;

import com.google.appengine.api.search.Index;
import com.google.appengine.api.search.IndexSpec;
import com.google.appengine.api.search.SearchServiceFactory;

public class FieldValueIndex {

    private Index idx;

    public Index idx() {
        if (idx == null) {
            idx = SearchServiceFactory.getSearchService().getIndex(IndexSpec.newBuilder().setName("fv_index"));
        }
        return idx;
    }

}
