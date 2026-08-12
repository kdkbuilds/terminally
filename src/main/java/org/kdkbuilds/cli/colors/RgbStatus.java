package org.kdkbuilds.cli.colors;

public class RgbStatus extends BaseStatus{

    private final String TEXT;
    private final String MUTED;
    private final String PURPLE;
    private final String PEACH;

    public RgbStatus() {
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
    public void info(String text) {
        paint(CYAN, text);
    }

    @Override
    public void warning(String text) {
        paint(YELLOW, text);
    }

    @Override
    public void success(String text) {
        paint(GREEN, text);
    }

    @Override
    public void accent(String text) {
        paint(BLUE, text);
    }

    @Override
    public void error(String text) {
        paint(RED, text);
    }
}
