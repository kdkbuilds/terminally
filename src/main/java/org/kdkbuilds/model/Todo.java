package org.kdkbuilds.model;

public class Todo {
    private String task;
    private long id;

    // no-args constructor for jackson library
    public Todo() {

    }

    public Todo(String task, int id) {
        this.task = task;
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    @Override
    public String toString() {
        return id + " : " + task;
    }
}
