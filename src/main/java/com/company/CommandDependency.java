package com.company;

public class CommandDependency {

    private Command dependency;
    private DependencyType type;

    CommandDependency(Command dependency, DependencyType type) {

        this.dependency = dependency;
        this.type = type;
    }

    public Command getDependency() {
        return dependency;
    }

    public DependencyType getType() {
        return type;
    }

    public boolean hasDepency() {
        return this.dependency == null;
    }
}
