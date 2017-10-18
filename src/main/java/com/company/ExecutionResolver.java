package com.company;

public class ExecutionResolver {

    private final CommandDependencyResolver dependencyResolver;

    ExecutionResolver(CommandDependencyResolver dependencyResolver) {
        this.dependencyResolver = dependencyResolver;
    }

    public void execute() {
    }
}
