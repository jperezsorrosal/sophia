package com.company;


public enum ResourceType {
        INPUT("i"), STREAMABLE_INPUT("si"), OUTPUT("o"), STREAMABLE_OUTPUT("so");

    public String getTag() {
        return tag;
    }

    private String tag;

        ResourceType(String tag) {
            this.tag = tag;
        }
}

