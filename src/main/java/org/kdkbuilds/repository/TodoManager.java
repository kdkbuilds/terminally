package org.kdkbuilds.repository;

import org.kdkbuilds.model.Todo;

import java.util.ArrayList;

public class TodoManager {
    private ArrayList<Todo> todos = new ArrayList<>();

    public void add(String task) {
        int id = (int) (Math.random() * 100);
        Todo todo = new Todo(task, id);
        todos.add(todo);
    }

    public void update(String task, int id) {
        for (int i = 0; i < todos.size(); i++) {
            Todo todo = todos.get(i);
            if (todo.getId() == id) {
                todo.setTask(task);
            }
        }
    }

    public void delete(int id) {
        todos.removeIf(todo -> todo.getId() == id);
    }

    public void display() {
        for (int i = 0; i < todos.size(); i++) {
            Todo todo = todos.get(i);
            System.out.println(todo.toString());
        }
    }

    public void display(int id) {
        for (int i = 0; i < todos.size(); i++) {
            Todo todo = todos.get(i);
            if (todo.getId() == id) {
                System.out.println("Task : " + todo.getTask());
            }
        }
    }
}
