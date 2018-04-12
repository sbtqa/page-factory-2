package ru.sbtqa.tag.pagefactory.events;

import java.util.ArrayList;
import java.util.List;

public class TaskHandler {

    private static List<Task> tasks = new ArrayList<>();

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
