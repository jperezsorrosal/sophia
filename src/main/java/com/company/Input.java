package com.company;

public class Input {

    private String resourceName;
    private ResourceType type;

    Input(String name, ResourceType type) {
        this.resourceName = name;
        this.type = type;
    }

    public String getResourceName() {
        return resourceName;
    }

    public ResourceType getType() {
        return type;
    }
}
