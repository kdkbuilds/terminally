package org.kdkbuilds;

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.kdkbuilds.cli.TerminalCli;
import org.kdkbuilds.cli.colors.AnsiConsoleRenderer;
import org.kdkbuilds.cli.colors.ConsoleRenderer;
import org.kdkbuilds.cli.colors.RgbConsoleRenderer;
import org.kdkbuilds.config.AppConfig;
import org.kdkbuilds.config.AppDataDirectory;
import org.kdkbuilds.model.User;
import org.kdkbuilds.persistence.JsonTaskStore;
import org.kdkbuilds.service.TaskService;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

public class Main {

    private static final Path APP_DATA_DIRECTORY;
    private static final Path APP_DATA_FILE_PATH;
    private static final String APP_DATA_FILE_NAME;
    private static final User USER;
    private static final JsonTaskStore JSON_TASK_STORE;
    private static final TaskService TASK_SERVICE;

    // for terminal
    private final static Terminal TERMINAL;
    private final static LineReader READER;
    private static final ConsoleRenderer CONSOLE_RENDERER;
    private static final TerminalCli TERMINAL_CLI;

    private static final AppConfig APP_CONFIG;

    static {
        // paramount to the application
        try {
            APP_DATA_DIRECTORY = AppDataDirectory.create();
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize data store for the application. You most probably can't run this app :(");
        }
        APP_CONFIG = new AppConfig();
        APP_DATA_FILE_NAME = APP_CONFIG.getFileName();
        APP_DATA_FILE_PATH = APP_DATA_DIRECTORY.resolve(APP_DATA_FILE_NAME);

        USER = new User("", new ArrayList<>());
        TASK_SERVICE = new TaskService();
        JSON_TASK_STORE = new JsonTaskStore(APP_DATA_FILE_PATH, APP_CONFIG, TASK_SERVICE, USER);
    }

    // for terminal ops
    static {
        try {
            TERMINAL = TerminalBuilder.builder().system(true).build();
            READER = LineReaderBuilder.builder().terminal(TERMINAL).build();
        } catch (IOException e) {
            throw new RuntimeException("Failed to build terminal");
        }
        CONSOLE_RENDERER = initConsoleRenderer();
        TERMINAL_CLI = new TerminalCli(TERMINAL, READER, CONSOLE_RENDERER, APP_CONFIG, TASK_SERVICE, JSON_TASK_STORE);
    }

    private static ConsoleRenderer initConsoleRenderer() {
        if (APP_CONFIG.getColorProfile().equals("rgb")) {
            return new RgbConsoleRenderer();
        }

        return new AnsiConsoleRenderer();
    }

    private static void run() {
        TERMINAL_CLI.configureWorkspace();
        TERMINAL_CLI.beginSession();
    }

    public static void main(String[] args) {
        run();
    }
}
