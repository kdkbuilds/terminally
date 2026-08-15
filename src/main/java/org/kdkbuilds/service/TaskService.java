package org.kdkbuilds.service;

import org.kdkbuilds.model.Task;

import java.util.ArrayList;
import java.util.List;

public final class TaskService {

    private List<Task> tasks;
    private int maxId;

    public TaskService() {
        this.tasks = new ArrayList<>();
        setMaxID();
    }

    private void setMaxID() {
        if (tasks.isEmpty()) {
            maxId = 0;
            return;
        }

        int max = -1;
        for (Task task: tasks) {
            max = Math.max(max, task.getId());
        }
        maxId = max;
    }

    public int getTaskIndex(int id) {
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getId() == id) {
                return i;
            }
        }
        return -1;
    }

    public List<Task> getAllTasks() {
        return List.copyOf(tasks);
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
        setMaxID();
    }

    public Task add(String description) {
        Task newTask = new Task(maxId + 1, description);
        tasks.add(newTask);
        maxId++;
        return newTask;
    }

    public boolean delete(int id) {
        return tasks.removeIf(task -> task.getId() == id);
    }

    public void update(int id, int taskIndex, String updatedDescription) {
        tasks.set(taskIndex, new Task(id, updatedDescription));
    }

    public void clear() {
        tasks.clear();
    }
}
