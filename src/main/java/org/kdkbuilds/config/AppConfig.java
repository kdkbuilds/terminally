package org.kdkbuilds.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class AppConfig {

    // app metadata
    private final String APP_VERSION;
    private final String FILE_NAME;
    private final String COLOR_PROFILE;
    private final String DEVELOPER_CREDENTIALS;

    public AppConfig() {
        try (InputStream inputStream = AppConfig.class.getClassLoader()
                .getResourceAsStream("terminally.properties")) {

            final Properties PROPERTIES = new Properties();
            PROPERTIES.load(inputStream);

            // init configs
            APP_VERSION = PROPERTIES.getProperty("app.version", "v1.0.0");
            FILE_NAME = PROPERTIES.getProperty("file.name", "users.json");
            COLOR_PROFILE = PROPERTIES.getProperty("color.profile", "rgb");
            DEVELOPER_CREDENTIALS = PROPERTIES.getProperty("developer.credentials", "kdk");

        } catch (IOException ioe) {
            throw new RuntimeException("Error parsing properties file");
        }
    }

    public String getAppVersion() {
        return APP_VERSION;
    }

    public String getFileName() {
        return FILE_NAME;
    }

    public String getColorProfile() {
        return COLOR_PROFILE;
    }

    public String getAdminCredentials() {
        return DEVELOPER_CREDENTIALS;
    }
}
