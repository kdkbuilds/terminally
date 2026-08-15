package org.kdkbuilds.cli.colors;

public class RgbConsoleRenderer extends ConsoleRenderer {

    private final String TEXT;
    private final String MUTED;
    private final String PURPLE;
    private final String PEACH;

    public RgbConsoleRenderer() {
        // parents
        RED     = rgb(243, 139, 168);
        BLUE    = rgb(137, 180, 250);
        CYAN    = rgb(116, 199, 236);
        GREEN   = rgb(166, 227, 161);
        YELLOW  = rgb(249, 226, 175);

        // additional
        TEXT    = rgb(205, 214, 244);
        MUTED   = rgb(108, 112, 134);
        PURPLE  = rgb(203, 166, 247);
        PEACH   = rgb(250, 179, 135);
    }

    private static String rgb(int r, int g, int b) {
        return "\u001B[38;2;" + r + ";" + g + ";" + b + "m";
    }

    @Override
    public String developer(String text) {
        return render(CYAN, text);
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
        return render(BLUE, text);
    }

    @Override
    public String error(String text) {
        return render(RED, text);
    }
}
