package org.kdkbuilds.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.kdkbuilds.exceptions.AccessDeniedException;

import java.util.List;

public final class User {

    private String password;
    private List<Task> tasks;
    private boolean isRegistered;

    @JsonCreator
    public User(@JsonProperty("password") String password, @JsonProperty("tasks") List<Task> tasks) {
        this.password = password;
        this.tasks = tasks;
    }

    public void setPassword(String password) throws AccessDeniedException {
        if (!isRegistered) {
            this.password = password;
            isRegistered = true;
        } else {
            throw new AccessDeniedException("Password can not be set more than once per session");
        }
    }

    public String getPassword() {
        isRegistered = true;
        return password;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
}
