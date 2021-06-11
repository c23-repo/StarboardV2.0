package com.starboard.util;

public enum ConsoleColors {
    //color reset
    RESET("\033[0m"),

    // colors and corresponding code
    RED("\033[0;91m"),
    GREEN("\033[0;92m"),
    YELLOW("\033[0;93m"),
    BLUE("\033[0;94m"),
    CYAN("\033[0;96m"),
    WHITE("\033[0;37m"),
    BRIGHTWHITE("\033[0;97m");

    private final String code;

    ConsoleColors(String code) {
        this.code = code;
    }

    public static void changeTo(ConsoleColors color) {
        System.out.print(color);
    }

    public static void reset() {
        System.out.print(ConsoleColors.RESET);
    }

    @Override
    public String toString() {
        return code;
    }
}