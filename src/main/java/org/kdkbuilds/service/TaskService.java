package org.kdkbuilds.service;

import org.kdkbuilds.exceptions.IdNotFoundException;
import org.kdkbuilds.model.Task;

import java.util.ArrayList;
import java.util.List;

public final class TaskService {

    private final List<Task> tasks;
    private int maxId;

    public TaskService(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
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

    private int getTaskIndex(int id) {
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

    public Task add(String description) {
        Task newTask = new Task(maxId + 1, description);
        tasks.add(newTask);
        maxId++;
        return newTask;
    }

    public boolean delete(int id) {
        return tasks.removeIf(task -> task.getId() == id);
    }

    public void update(int id, String updatedDescription) throws IdNotFoundException{
        int index = getTaskIndex(id);
        if (index == -1) {
            throw new IdNotFoundException("Provided ID does not match any existing task");
        }
        tasks.set(index, new Task(id, updatedDescription));
    }
}
