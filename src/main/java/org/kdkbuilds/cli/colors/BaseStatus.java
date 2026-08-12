package org.kdkbuilds.cli.colors;

public abstract class BaseStatus {

    // Colors
    protected String RED;
    protected String GREEN;
    protected String YELLOW;
    protected String BLUE;
    protected String CYAN;

    // Clear color for next log
    static String RESET = "\u001B[0m";

    // Default behaviours
    protected static void paint(String color, String text) {
        System.out.print(color + text + RESET);
    }
    abstract public void info(String text);
    abstract public void warning(String text);
    abstract public void success(String text);
    abstract public void accent(String text);
    abstract public void error(String text);
}
