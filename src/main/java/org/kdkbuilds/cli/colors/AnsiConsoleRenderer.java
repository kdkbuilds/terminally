package org.kdkbuilds.cli.colors;

public final class AnsiConsoleRenderer extends ConsoleRenderer {

    public AnsiConsoleRenderer() {
        RED     = "\u001B[31m";
        GREEN   = "\u001B[32m";
        YELLOW  = "\u001B[33m";
        BLUE    = "\u001B[34m";
        CYAN    = "\u001B[36m";
    }

    @Override
    public String developer(String text) {
        return render(BLUE, text);
    }

    @Override
    public String user(String text) {
        return render(YELLOW, text);
    }

    @Override
    public String success(String text) {
        return render(GREEN, text);
    }

    @Override
    public String accent(String text) {
        return render(CYAN, text);
    }

    @Override
    public String error(String text) {
        return render(RED, text);
    }
}
