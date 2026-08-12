package org.kdkbuilds.cli.colors;

public final class Status extends BaseStatus {

    public Status() {
        RED     = "\u001B[31m";
        GREEN   = "\u001B[32m";
        YELLOW  = "\u001B[33m";
        BLUE    = "\u001B[34m";
        CYAN    = "\u001B[36m";
    }

    @Override
    public void info(String text) {
        paint(BLUE, text);
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
        paint(CYAN, text);
    }

    @Override
    public void error(String text) {
        paint(RED, text);
    }

}
