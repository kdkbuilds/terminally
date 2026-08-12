package org.kdkbuilds.cli;

import org.kdkbuilds.cli.colors.BaseStatus;
import org.kdkbuilds.cli.colors.RgbStatus;
import org.kdkbuilds.cli.colors.Status;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Scanner;

public class TerminalCli {

    private static final Properties PROPERTIES = new Properties();
    private static final BaseStatus LOG;
    private static final Scanner SCANNER = new Scanner(System.in);

    // static block to load properties file
    static {
        try (InputStream inputStream =
                TerminalCli.class.getClassLoader()
                        .getResourceAsStream("terminally.properties")) {
            if (inputStream == null) {
                throw new IllegalStateException("Properties file not found");
            }

            PROPERTIES.load(inputStream);
            LOG = initStatus();

        } catch (IOException ioe) {
            throw new ExceptionInInitializerError(ioe);
        }
    }

    private static BaseStatus initStatus() {
        String status = get("color.profile");

        if (status.equals("rgb")) {
            return new RgbStatus();
        }

        return new Status();
    }

    public static String get(String key) {
        return PROPERTIES.getProperty(key);
    }

    // overloaded get method for future, and safe default fallback
    public static String get(String key, String defaultValue) {
        return PROPERTIES.getProperty(key, defaultValue);
    }


    private static void greet() {
        LOG.success("----------------------TERMINALLY----------------------\n\n");

//        Thread.sleep(1000);
        LOG.info("kdk: Hi there, welcome to terminally v1.0.0\n");

//        Thread.sleep(3000);
        LOG.info("kdk: Before we begin, what shall I address you as?\n\n");

//        Thread.sleep(1000);
        LOG.warning("session name: ");
        String username = SCANNER.nextLine().strip();

//        log.info("\nkdk: What's going on " + username + "! ");
        LOG.info("\nkdk: What's going on ");
        LOG.warning(username); LOG.info("! ");

//        Thread.sleep(1700);
        LOG.info("I have a feeling we'll get along really well :)\n");

//        Thread.sleep(2000);
        LOG.info("kdk: So what terminally does is, it helps you deal with dangers of the day by mitigating your daily tasks.\n\n");

//        Thread.sleep(7000);
        LOG.info("kdk: Ready when you are!\n\n");

//        Thread.sleep(800);
        LOG.accent("1. Add   2. List All   3. Update   4. Delete   5. Exit\n");
        LOG.warning(username + ": ");

        String choice = SCANNER.next().strip();

        LOG.info("\nkdk: Excellent choice ");
        LOG.warning(username);
        LOG.info("... What would you like to do today?\n");

        LOG.warning(username + ": ");
    }

    public static void main(String[] args) {
        greet();
    }

}
