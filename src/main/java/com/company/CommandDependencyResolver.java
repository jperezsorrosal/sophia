package com.company;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.company.DependencyType.HARD;
import static com.company.DependencyType.SOFT;

public class CommandDependencyResolver {

    private HashMap<Command, List<CommandDependency>> dependencyRegister = new HashMap<>();

    HashMap<String, Command> outputProducer = new HashMap<>();

    CommandDependencyResolver(List<Command> shellCommands) {
        // register which Shell Command produces which resource
        shellCommands.forEach(c -> c.getOutputs().forEach(o -> outputProducer.put(o.getResourceName(), c)));

        // initialize the dependency register
        shellCommands.forEach(c -> dependencyRegister.put(c, new ArrayList<>()));
        shellCommands.forEach(c -> addDependencyToRegister(c));
    }

    private void addDependencyToRegister(Command command) {

        if (command.hasInputs()) {
            command.getInputs().forEach(i -> {
                DependencyType type = ((i.getType() ==  ResourceType.STREAMABLE_INPUT) ? SOFT : HARD);
                dependencyRegister.get(command).add(new CommandDependency(outputProducer.get(i.getResourceName()), type));
            });
        }
    }

    void printDependency(Command command) {
        List<CommandDependency> depList = dependencyRegister.get(command);

        if (depList.isEmpty()) System.out.println("Command [" + command.getId() + "] has no dependencies.");
        else {
            depList.forEach(dep -> {
                System.out.println("Command [" + command.getId() + "] depends on [" + dep.getDependency().getId() + "] with " +dep.getType().toString() + " dependency type.");
                printDependency(dep.getDependency());
            });
        }
    }

    private void findSoftDeps(Command command, List<CommandDependency> dependencies, List<Command> currentDepList) {
        dependencyRegister.entrySet().stream().forEach(es -> {

            if (dependencies.isEmpty() && !currentDepList.contains(command)) {
                currentDepList.add(command);

            } else if (!dependencies.isEmpty() && !currentDepList.contains(command)) {

                dependencies.forEach(cd -> {
                    if (cd.getType() == SOFT) {
                        Command dep = cd.getDependency();
                        findSoftDeps(dep, dependencyRegister.get(dep), currentDepList);
                    }
                });

                currentDepList.add(command);
            }
        });
    }

    public List<Command> getNoDependentAndSoftDependentInExecutionOrder() {
        List<Command> softDepends = new ArrayList<Command>();

        dependencyRegister.entrySet().stream().forEach(es -> {
            Command command = es.getKey();
            List<CommandDependency> commandDeps = es.getValue();

            findSoftDeps(command, commandDeps, softDepends);
        });

        return softDepends;
    }
}
