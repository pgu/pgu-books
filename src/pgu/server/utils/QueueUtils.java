package pgu.server.utils;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

public class QueueUtils {

    public void addTaskToCreateFieldValues(final String bookId) {

        final TaskOptions task = TaskOptions.Builder.withUrl("/task/createFieldValues");
        task.param("bookId", bookId);

        final Queue queue = QueueFactory.getQueue("fieldValuesQueue");
        queue.add(task);
    }

}
