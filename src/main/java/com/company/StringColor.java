package com.company;

public enum StringColor {

    BLACK((char) 27 + "[30m"),
    RED((char) 27 + "[31m"),
    GREEN((char) 27 + "[32m"),
    YELLOW((char) 27 + "[33m"),
    BLUE((char) 27 + "[34m"),
    MAGENTA((char) 27 + "[35m"),
    CYAN((char) 27 + "[36m"),
    WHITE((char) 27 + "[37m"),
    CLEAR_FORMAT((char) 27 + "[0m");

    private String colorSequence;

    StringColor(String colorSequence) {
        this.colorSequence = colorSequence;
    }

    final static String colour(String s, StringColor color) {
        return color.colorSequence + s + CLEAR_FORMAT.colorSequence;
    }
}
