package ru.sbtqa.tag.pagefactory.tasks;

import java.util.ArrayList;
import java.util.List;

public class TaskHandler {

    private static List<Task> tasks = new ArrayList<>();

    private TaskHandler() {}

    public static void addTask(Task task) {
        tasks.add(task);
    }

    public static void handleTasks() {
        for (Task task : tasks) {
            task.handle();
        }
        tasks.clear();
    }
}
