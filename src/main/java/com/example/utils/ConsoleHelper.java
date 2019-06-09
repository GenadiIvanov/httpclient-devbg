package com.example.utils;

public class ConsoleHelper {

    public static final int BLACK = 1;
    public static final int RED = 2;
    public static final int GREEN = 3;
    public static final int YELLOW = 4;
    public static final int BLUE = 5;
    public static final int PURPLE = 6;
    public static final int CYAN = 7;
    public static final int WHITE = 8;

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static String green(String message) {
        return ANSI_GREEN + message + ANSI_RESET;
    }

    public static String red(String message) {
        return ANSI_RED + message + ANSI_RESET;
    }

    public static String yellow(String message) {
        return ANSI_YELLOW + message + ANSI_RESET;
    }

    public static String blue(String message) {
        return ANSI_BLUE + message + ANSI_RESET;
    }

    public static String purple(String message) {
        return ANSI_PURPLE + message + ANSI_RESET;
    }

    public static String cyan(String message) {
        return ANSI_CYAN + message + ANSI_RESET;
    }

    public static String white(String message) {
        return ANSI_WHITE + message + ANSI_RESET;
    }

    public static String thread(String message) {
        return thread(message, WHITE);
    }

    public static String thread(String message, int color) {
        String ANSI_COLOR;
        switch (color) {
            case BLACK:
                ANSI_COLOR = ANSI_BLACK;
                break;
            case RED:
                ANSI_COLOR = ANSI_RED;
                break;
            case GREEN:
                ANSI_COLOR = ANSI_GREEN;
                break;
            case YELLOW:
                ANSI_COLOR = ANSI_YELLOW;
                break;
            case BLUE:
                ANSI_COLOR = ANSI_BLUE;
                break;
            case PURPLE:
                ANSI_COLOR = ANSI_PURPLE;
                break;
            case CYAN:
                ANSI_COLOR = ANSI_CYAN;
                break;
            default:
                ANSI_COLOR = ANSI_WHITE;
        }
        return "[" + Thread.currentThread().getName() + "] " + ANSI_COLOR + message + ANSI_RESET;
    }
}
