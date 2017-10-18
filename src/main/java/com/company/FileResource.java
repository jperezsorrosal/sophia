package com.company;

public abstract class FileResource {

    private final String resourceName;
    private final ResourceType type;

    FileResource(String resourceName, ResourceType type) {
        this.resourceName = resourceName;
        this.type = type;
    }

    public String getResourceName() {
        return resourceName;
    }

    public ResourceType getType() {
        return type;
    }
}
