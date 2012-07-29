package pgu.server.utils;

import javax.servlet.http.HttpServletRequest;

import pgu.server.app.AppLog;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

public class QueueUtils {

    private static final String FV_QUEUE = "fieldValuesQueue";

    private final AppUtils      u        = new AppUtils();
    private final MailUtils     mailU    = new MailUtils();

    public void addTaskToCreateFieldValues(final String bookId) {

        final TaskOptions task = TaskOptions.Builder.withUrl("/task/createFieldValues");
        task.param("bookId", bookId);

        final Queue queue = QueueFactory.getQueue(FV_QUEUE);
        queue.add(task);
    }

    public boolean isTaskLauncherAuthorized(final HttpServletRequest req, final AppLog log) {

        if (u.isEnvProd()) {

            final String qNameHeader = req.getHeader("X-AppEngine-QueueName");
            if (!FV_QUEUE.equals(qNameHeader)) {

                final String message = "The servlet has been called from something else than the queue";
                mailU.sendMail(message, log);
                log.error(this, new UnsupportedOperationException(message));
                return false;
            }
        }

        return true;
    }

}
