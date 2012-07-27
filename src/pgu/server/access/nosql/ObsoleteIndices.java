package pgu.server.access.nosql;

import com.google.appengine.api.search.Index;
import com.google.appengine.api.search.IndexSpec;
import com.google.appengine.api.search.SearchServiceFactory;

public class ObsoleteIndices {

    private Index index;
    private Index archive_index;

    public Index idx() {
        if (index == null) {
            index = SearchServiceFactory.getSearchService().getIndex(IndexSpec.newBuilder().setName("shared_index"));
        }
        return index;
    }

    public Index archiveIdx() {
        if (archive_index == null) {
            archive_index = SearchServiceFactory.getSearchService().getIndex(
                    IndexSpec.newBuilder().setName("archive_shared_index"));
        }
        return archive_index;
    }
}
