package com.company;

public class Output {

    private final String resourceName;
    private final ResourceType type;

    Output(String name, ResourceType type) {
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
