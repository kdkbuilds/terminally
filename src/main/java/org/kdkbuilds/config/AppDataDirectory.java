package org.kdkbuilds.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

// utility class
public final class AppDataDirectory {

    private static final String APP_DIRECTORY_NAME = "Terminally";

    // prevent initialisation
    private AppDataDirectory () {}

    public static Path create() throws IOException {
        return Files.createDirectories(resolve());
    }

    private static Path resolve() {
        String userOs = System.getProperty("os.name").toLowerCase(Locale.ROOT);
        Path userHome = getUserHome();

        if (userOs.contains("windows")) {
            Path localAppData = getAbsolutePath("LOCALAPPDATA");

            if (localAppData == null) {
                localAppData = userHome
                                .resolve("AppData")
                                .resolve("Local");
            }

            return localAppData.resolve(APP_DIRECTORY_NAME);
        }

        if (userOs.contains("mac") || userOs.contains("darwin")) {
            return userHome
                    .resolve("Library")
                    .resolve("Application Support")
                    .resolve(APP_DIRECTORY_NAME);
        }

        if (userOs.contains("linux")
                || userOs.contains("nix")
                || userOs.contains("nux")
                || userOs.contains("aix")) {

            Path xdgDataHome = getAbsolutePath("XDG_DATA_HOME");

            if (xdgDataHome == null) {
                xdgDataHome =  userHome
                            .resolve(".local")
                            .resolve("share");
            }

            return xdgDataHome.resolve(APP_DIRECTORY_NAME);
        }

        return userHome.resolve("." + APP_DIRECTORY_NAME.toLowerCase());
    }

    private static Path getUserHome() {
        String userHome = System.getProperty("user.home");

        // extremmely improbable, but if yes, shut down application as data can not be persisted
        if (userHome == null) {
            throw new IllegalStateException("Unable to fetch user's home directory");
        }

        return Path.of(userHome);
    }

    private static Path getAbsolutePath(String variable) {
        String value = System.getenv(variable);

        if (value == null || value.isBlank()) {
            return null;
        }

        return Path.of(value);
    }
}
