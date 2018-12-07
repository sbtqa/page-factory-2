package ru.sbtqa.tag.pagefactory.tasks;

import java.util.ArrayList;
import java.util.List;

public class TaskHandler {

    private static ThreadLocal<List<Task>> tasks = ThreadLocal.withInitial(ArrayList::new);

    private TaskHandler() {
    }

    public static void addTask(Task task) {
        tasks.get().add(task);
    }

    public static void handleTasks() {
        for (Task task : tasks.get()) {
            task.handle();
        }
        tasks.get().clear();
    }
}
