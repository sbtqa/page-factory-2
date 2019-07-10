package ru.sbtqa.tag.pagefactory.web.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.pagefactory.exceptions.WaitException;
import ru.sbtqa.tag.pagefactory.tasks.Task;
import ru.sbtqa.tag.pagefactory.utils.Alert;

public class KillAlertTask implements Task {

    private static final Logger LOG = LoggerFactory.getLogger(KillAlertTask.class);

    @Override
    public void handle() {
        try {
            new Alert(1).dismiss();
        } catch (WaitException | NullPointerException e) {
            LOG.debug("No alert opened", e);
        }
    }
}
