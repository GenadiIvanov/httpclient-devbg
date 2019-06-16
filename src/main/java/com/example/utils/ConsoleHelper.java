package com.example.utils;

public class ConsoleHelper {

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
}
