package org.kdkbuilds.cli.colors;

public abstract class ConsoleRenderer {

    // Colors
    protected String RED;
    protected String GREEN;
    protected String YELLOW;
    protected String BLUE;
    protected String CYAN;

    // Clear color for next log
    static String RESET = "\u001B[0m";

    abstract public String developer(String text);
    abstract public String user(String text);
    abstract public String success(String text);
    abstract public String accent(String text);
    abstract public String error(String text);

    // Default behaviours
    protected static String render(String color, String text) {
        return (color + text + RESET);
    }
}
