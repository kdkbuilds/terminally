package org.kdkbuilds.persistence;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.kdkbuilds.config.AppConfig;
import org.kdkbuilds.exceptions.*;
import org.kdkbuilds.model.User;
import org.kdkbuilds.service.TaskService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

// Databse of the application
public final class JsonTaskStore {

    private final Path appDataPath;
    private final ObjectMapper mapper;
    private final DefaultIndenter indenter;
    private final DefaultPrettyPrinter printer;

    private String username;
    private final User user;
    private final Map<String, User> userMap;
    private final TaskService taskService;

    public JsonTaskStore(Path appDataPath, AppConfig config, TaskService taskService, User user) {
        this.taskService = taskService;
        this.mapper = new ObjectMapper();
        this.appDataPath = appDataPath;
        this.username = "";
        this.user = user;
        this.userMap = load();
        this.indenter = new DefaultIndenter("    ", "\n");
        this.printer = new DefaultPrettyPrinter();
        configurePrinter();
    }

    private void configurePrinter() {
        printer.indentObjectsWith(indenter);
        printer.indentArraysWith(indenter);
    }

    private Map<String, User> load() {
        if (Files.notExists(appDataPath)) {
            return new HashMap<>();
        }

        try {
            return mapper.readValue(appDataPath.toFile(), new TypeReference<Map<String, User>>() {});
        } catch (StreamReadException e) {
            throw new RuntimeException("Source JSON is corrupted");
        } catch (DatabindException e) {
            throw new RuntimeException("Fields from source JSON do not match with application design");
        } catch (IOException e) {
            throw new RuntimeException("System error occurred while parsing source JSON");
        }
    }

    public void save() throws UserNotInitializedException {
        if (username.isEmpty()) {
            throw new UserNotInitializedException("User is not initialized for current session");
        }
        userMap.get(username).setTasks(taskService.getAllTasks());
        try {
            mapper.writer(printer).writeValue(appDataPath.toFile(), userMap);
        } catch (IOException e) {
            throw new RuntimeException("Error while deserializng to JSON");
        }
    }

    public boolean userExists(String username) {
        return userMap.containsKey(username);
    }

    public boolean validatePassword(String username, String password) throws UserNotFoundException {
        if (userExists(username)) {
            return userMap.get(username).getPassword().equals(password);
        } else {
            throw new UserNotFoundException("User does not exist!");
        }
    }

    public void registerUser(String username, String password) throws UserAlreadyExists, AccessDeniedException, UserNotInitializedException {
        if (userExists(username)) {
            throw new UserAlreadyExists("Sorry love, this username is taken... time to get creative!");
        }
        // can only be called for the entire application session
        user.setPassword(password);

        // registered
        this.username = username;
        userMap.put(username, user);

        // persist the user to disk as well
        save();
    }

    public void loginUser(String username, String password) throws UserNotFoundException, InvalidPasswordException {
        if (!userMap.containsKey(username)) {
            throw new UserNotFoundException("User is not registered");
        }

        // load all the fields for current user session
        if (userMap.get(username).getPassword().equals(password)) {
            this.username = username;
            taskService.setTasks(userMap.get(username).getTasks());
        } else {
            throw new InvalidPasswordException("Incorrect password provided");
        }
    }
}
