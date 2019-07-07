package org.academiadecodigo.bootcamp;

public enum UserColors {
    BLACK("\033[0;30m"),
    RED("\033[0;31m"),
    GREEN("\033[0;32m"),
    YELLOW("\033[0;33m"),
    BLUE("\033[0;34m"),
    PURPLE("\033[0;35m"),
    CYAN("\033[0;36m"),
    WHITE("\033[0;37m");

    private String color;

    UserColors(String color) {
        this.color = color;
    }

    public static String getColorFromString(String color) {
        switch (color.toUpperCase()) {
            case "BLACK":
                return "" + BLACK.getColor();
            case "RED":
                return "" + RED.getColor();
            case "GREEN":
                return "" + GREEN.getColor();
            case "YELLOW":
                return "" + YELLOW.getColor();
            case "BLUE":
                return "" + BLUE.getColor();
            case "PURPLE":
                return "" + PURPLE.getColor();
            case "CYAN":
                return "" + CYAN.getColor();
            default:
                return "" + WHITE.getColor();
        }
    }

    public String getColor() {
        return color;
    }
}
