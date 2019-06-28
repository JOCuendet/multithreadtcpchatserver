package org.academiadecodigo.bootcamp;

public class UserColorMapper {
    private static final String colors [] = {
            "\033[0;30m", // BLACK
            "\033[0;31m", // RED
            "\033[0;32m", // GREEN
            "\033[0;33m", // YELLOW
            "\033[0;34m", // BLUE
            "\033[0;35m", // PURPLE
            "\033[0;36m", // CYAN
            "\033[0;37m"  // WHITE
    };

    public static String getColor(String color){
        color.toUpperCase();
        switch (color){
            case "BLACK":
                return colors[0];
            case "RED":
                return colors[1];
            case "GREEN":
                return colors[2];
            case "YELLOW":
                return colors[3];
            case "BLUE":
                return colors[4];
            case "PURPLE":
                return colors[5];
            case "CYAN":
                return colors[6];
            default:
                return colors[7];
        }
    }
}
