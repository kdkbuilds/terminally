package org.kdkbuilds.cli;

import org.jline.reader.LineReader;
import org.jline.terminal.Terminal;
import org.kdkbuilds.cli.colors.ConsoleRenderer;
import org.kdkbuilds.config.AppConfig;
import org.kdkbuilds.exceptions.*;
import org.kdkbuilds.model.Task;
import org.kdkbuilds.persistence.JsonTaskStore;
import org.kdkbuilds.service.TaskService;

import java.util.List;

public final class TerminalCli {

    // external dependencies
    private final Terminal terminal;
    private final LineReader reader;
    private final ConsoleRenderer renderer;
    private final TaskService taskService;
    private final JsonTaskStore jsonTaskStore;

    // local variables
    private final String appVersion;
    private final String devName;
    private String username;

    public TerminalCli(Terminal terminal, LineReader reader, ConsoleRenderer renderer,
                       AppConfig config, TaskService taskService, JsonTaskStore jsonTaskStore) {
        this.terminal = terminal;
        this.reader = reader;
        this.renderer = renderer;
        this.taskService = taskService;
        this.jsonTaskStore = jsonTaskStore;

        this.devName = config.getAdminCredentials();
        this.appVersion = config.getAppVersion();
    }

    public void configureWorkspace() {
        terminal.writer().println(renderer.success("--------------------------- \u001B[3m\u001B[1mTERMINALLY %s\u001B[22m\u001B[23m ---------------------------\n".formatted(appVersion)));

        terminal.writer().println(renderer.developer("%s: Hi there! Welcome to terminally!").formatted(devName));

        terminal.writer().println(renderer.accent("\n1. Login    2. Register    3.Exit\n"));

        boolean workspaceLoaded = false;
        while (!workspaceLoaded) {
            String userChoice = reader.readLine(renderer.developer("choice : ")).strip().toLowerCase();

            switch (userChoice) {
                case "1", "login" -> {
                    terminal.writer().println("\n→ Login\n");

                    String username = validateUserName();
                    String password = validatePassword();

                    try {
                        jsonTaskStore.loginUser(username, password);
                        this.username = username;

                        terminal.writer().println("\n\n✓ Login complete");
                        welcomeUserBack();
                        workspaceLoaded = true;
                    } catch (UserNotFoundException e) {
                        terminal.writer().println(renderer.developer("\n%s: I was unable to locate you %s. Please consider registering instead :)\n".formatted(devName, username)));
                        terminal.writer().println(renderer.accent("\n1. Login    2. Register    3.Exit\n"));
                    } catch (InvalidPasswordException e) {
                        terminal.writer().println(renderer.error("\n%s: Hm... Something didn't quite add up there. Credentials you provided did not match".formatted(devName)));
                        terminal.writer().println(renderer.accent("\n1. Login    2. Register    3.Exit\n"));
                    }

                }
                case "2", "r", "register" -> {
                    terminal.writer().println("\n→ Register\n");

                    boolean registrationIsComplete = false;
                    String username = "";
                    String password = "";
                    while (!registrationIsComplete) {
                        username = validateUserName();
                        password = validatePassword();

                        try {
                            jsonTaskStore.registerUser(username, password);
                            registrationIsComplete = true;
                        } catch (UserAlreadyExists e) {
                            terminal.writer().println(renderer.developer("\n%s: Sorry love, this username is taken... time to get creative!\n").formatted(devName));
                        } catch (AccessDeniedException e) {
                            terminal.writer().println(renderer.error("\n%s: Our systems have detected sus activity... cash me outside hackers how about dah?\n").formatted(devName));
                        } catch (UserNotInitializedException e) {
                            terminal.writer().println(renderer.developer("\n%s: terminally is off da zoinky's right now 😵‍💫 Shutting down before any weird stuff takes effect".formatted(devName)));
                            System.exit(0);
                        }
                    }

                    this.username = username;
                    terminal.writer().println("\n\n✓ Registration complete");
                    greetNewUser();
                    workspaceLoaded = true;
                }
                case "3", "q", "exit" -> {
                    exit();
                    System.exit(0);
                }
                default -> {
                    try {
                        int choice = Integer.parseInt(userChoice);
                        if (choice < 1 || choice > 3) {
                            terminal.writer().println(renderer.error("\n%s: You might want to adjust your input according to what we currently offer 👀\n".formatted(devName)));
                        }
                    } catch (NumberFormatException e) {
                        terminal.writer().println(renderer.error("\nkdk: Hm... I didn't quite get that. Can you please try again?\n"));
                    }
                }
            }
        }
    }

    private String validateUserName() {
        String username = "";
        boolean usernameIsInvalid = true;

        while (usernameIsInvalid) {
            username = reader.readLine("username: ").strip();
            if (username.isEmpty()) {
                terminal.writer().println(renderer.error("\n%s: You literally gave an empty username 🙆🏻‍♂️\n".formatted(devName)));
            }  else {
                usernameIsInvalid = false;
            }
        }

        return username;
    }

    private String validatePassword() {
        String password = "";
        boolean passwordIsInvalid = true;

        while (passwordIsInvalid) {
            password = reader.readLine("password: ", '*');
            if (password.isEmpty()) {
                terminal.writer().println(renderer.error("\n%s: You literally gave an empty password 🙆🏻‍♂️\n".formatted(devName)));
            } else {
                passwordIsInvalid = false;
            }
        }

        return password;
    }

    private void welcomeUserBack() {
        terminal.writer().println(renderer.success("\n--------------------------- \u001B[3m\u001B[1m%s's WORKSPACE\u001B[22m\u001B[23m ---------------------------\n").formatted(username.toUpperCase()));
        terminal.writer().println(renderer.developer("\n%s: Welcome back, " + username + ". What shall we do today :)\n").formatted(devName));
    }

    private void greetNewUser() {
        terminal.writer().println(renderer.success("\n--------------------------- \u001B[3m\u001B[1m%s's WORKSPACE\u001B[22m\u001B[23m ---------------------------\n").formatted(username.toUpperCase()));

        terminal.writer().println(renderer.developer("\n%s: Welcome to terminally, " + username + ". I have a feeling we'll get along really well :)").formatted(devName));
        terminal.writer().println(renderer.developer("%s: So what terminally does is, it helps you deal with dangers of the day by mitigating your daily tasks.\n").formatted(devName));
        terminal.writer().println(renderer.developer("%s: Ready when you are!\n".formatted(devName)));
    }

    private void exit() {
        terminal.writer().println(renderer.developer("\n%s: Catch you later, then ✌️").formatted(devName));
    }

    public void beginSession() {
        boolean keepSessionActive = true;
        displayMenu();
        while (keepSessionActive) {
            String userChoice = reader.readLine(renderer.user("\n%s: ").formatted(username)).strip().toLowerCase();

            switch (userChoice) {
                case "1", "a", "add" -> {
                    try {
                        keepSessionActive = add();
                    } catch (UserNotInitializedException e) {
                        forceApplicationShutDown();
                    }
                }
                case "2", "l", "list", "list all" -> listAll();
                case "3", "u", "update" -> {
                    try {
                        keepSessionActive = update();
                    } catch (UserNotInitializedException e) {
                        forceApplicationShutDown();
                    }
                }
                case "4", "d", "delete", "remove", "get ridda", "to the streets" -> {
                    try {
                        keepSessionActive = delete();
                    } catch (UserNotInitializedException e) {
                        forceApplicationShutDown();
                    }
                }
                case "5", "c", "clear", "clear all", "faah" -> {
                    try {
                        clearAll();
                    } catch (Exception e) {
                        forceApplicationShutDown();
                    }
                }
                case "6", "exit", "q", "quit", "bye", "see ya" -> {
                    keepSessionActive = false;
                }
                case "m", "menu", "7" -> {
                    displayMenu();
                }
                default -> {
                    try {
                        int choice = Integer.parseInt(userChoice);
                        if (choice < 0 || choice > 6) {
                            terminal.writer().println(renderer.developer("\n%s: Slow your roll chief 🙆🏻‍♂️ ! We do not have a feature matching to your input right now.".formatted(devName)));
                            terminal.writer().println(renderer.developer("%s: Perhaps for now, you could stick with whatever choices we have 👉🏼 👈🏼\n".formatted(devName)));
                        }
                    } catch (NumberFormatException e) {
                        terminal.writer().println(renderer.developer("\n%s: I hear you %s, I do... But fam I can't help you with that right now :(".formatted(devName, username)));
                    }
                }
            }
        }

        sayGoodBye();
    }

    private boolean add() throws UserNotInitializedException {
        String description = "";
        while (true) {
            description = reader.readLine(renderer.user("\ntask description? ")).strip();
            if (description.isEmpty()) {
                terminal.writer().println(renderer.developer("\n%s: An empty description? %s I can't add that").formatted(devName, username));
            } else if (userWantsToGoBack(description)) {
                displayMenu();
                return true;
            } else if (userWantsToExit(description)) {
                return false;
            } else {
                break;
            }
        }
        taskService.add(description);
        jsonTaskStore.save();
        terminal.writer().println(renderer.developer("\n%s: Task is added to the ship... Toot Toot!".formatted(devName)));

        return true;
    }

    private void listAll() {
        List<Task> allTasks = taskService.getAllTasks();
        terminal.writer().println("\n{");
        for (Task task: allTasks) {
            terminal.writer().println("  " + task);
        }
        terminal.writer().println("}");
        if (allTasks.isEmpty()) {
            terminal.writer().println(renderer.developer("\n%s: Such empty...".formatted(devName)));
        }
    }

    private boolean update() throws UserNotInitializedException {
        while (true) {
            String userId = reader.readLine(renderer.user("\ntask id? ")).strip();
            if (userId.isEmpty()) {
                terminal.writer().println(renderer.developer("\n%s: An empty id? %s I can't update that!").formatted(devName, username));
                continue;
            } else if (userWantsToGoBack(userId)) {
                displayMenu();
                return true;
            } else if (userWantsToExit(userId)) {
                return false;
            }

            int id = -1;
            try {
                id = Integer.parseInt(userId);
            } catch (NumberFormatException e) {
                terminal.writer().println(renderer.developer("\n%s: I'm looking for a numberic ID little dove, can you provide it please?".formatted(devName)));
                continue;
            }

            int taskIndex = taskService.getTaskIndex(id);
            if (taskIndex == -1) {
                terminal.writer().println(renderer.developer("\n%s: No task matched with that ID %s. Could you please double check?".formatted(devName, username)));
                continue;
            }

            String description = "";
            while (true) {
                description = reader.readLine(renderer.user("updated description? ")).strip();
                if (description.isEmpty()) {
                    terminal.writer().println(renderer.developer("\n%s: You gave an empty description %s -_-\n").formatted(devName, username));
                } else {
                    break;
                }
            }

            taskService.update(id, taskIndex, description);
            jsonTaskStore.save();
            terminal.writer().println(renderer.developer("\n%s: Nice touch %s... description is now updated!".formatted(devName, username)));

            break;
        }
        return true;
    }

    private boolean delete() throws UserNotInitializedException {
        while (true) {
            String userInput = reader.readLine(renderer.user("\ntask id? ")).strip();
            if (userInput.isEmpty()) {
                terminal.writer().println(renderer.developer("\n%s: An empty id? %s I can't delete that!").formatted(devName, username));
                continue;
            } else if (userWantsToGoBack(userInput)) {
                displayMenu();
                return true;
            } else if (userWantsToExit(userInput)) {
                return false;
            }

            int id = -1;
            try {
                id = Integer.parseInt(userInput);
            } catch (NumberFormatException e) {
                terminal.writer().println(renderer.developer("\n%s: I'm looking for a numberic ID little dove, can you provide it please?".formatted(devName)));
                continue;
            }

            if(taskService.delete(id)) {
                jsonTaskStore.save();
                terminal.writer().println(renderer.developer("\n%s: Scratched that one right off did ya? Nice work! The task lives no more...".formatted(devName)));
                break;
            } else {
                terminal.writer().println(renderer.developer("\n%s: No task matched with that ID %s. Could you please double check?".formatted(devName, username)));
            }
        }
        return true;
    }

    private void clearAll() throws UserNotFoundException, UserNotInitializedException {
        terminal.writer().println(renderer.error("\n%s: This is dangerous waters %s. This will erase all tasks. Please confirm your password!\n".formatted(devName, username)));
        String password = validatePassword();
        if(jsonTaskStore.validatePassword(username, password)) {
            taskService.clear();
            jsonTaskStore.save();
            terminal.writer().println(renderer.success("\n%s: You now have a nice clean slate %s! Your tasks have been wiped cleeaaan :)".formatted(devName, username)));
        } else {
            terminal.writer().println(renderer.error("\n%s: Incorrect Password!").formatted(devName));
            displayMenu();
        }
    }

    private void displayMenu() {
        terminal.writer().println(renderer.accent("\n1. Add    2. List All    3. Update    4. Delete    5. Clear All    6. Exit    7. Menu"));
    }

    private void sayGoodBye() {
        terminal.writer().println(renderer.developer("\n%s: See you soon %s ✌️\n").formatted(devName, username));
    }

    private boolean userWantsToGoBack(String userInput) {
        return userInput.equals("m") || userInput.equals("7") || userInput.equals("menu");
    }

    private boolean userWantsToExit(String description) {
        return description.equals("exit") || description.equals("quit") || description.equals("q");
    }

    private void forceApplicationShutDown() {
        terminal.writer().println(renderer.error("\n%s: Terminally is off da zoinky's right now 😵‍💫 Shutting down before any weird stuff takes effect".formatted(devName)));
        System.exit(-1);
    }
}
