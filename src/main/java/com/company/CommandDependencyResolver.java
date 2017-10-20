package com.company;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static com.company.DependencyType.HARD;
import static com.company.DependencyType.SOFT;

public class CommandDependencyResolver {

    private List<Command> commands;
    private HashMap<Command, List<CommandDependency>> dependencyRegister = new HashMap<>();
    private HashMap<Command, List<CommandDependency>> dependentCommandRegister = new HashMap<>();

    HashMap<String, Command> outputProducer = new HashMap<>();

    CommandDependencyResolver(List<Command> shellCommands) {
        this.commands = shellCommands;

        // register which Shell Command produces which resource
        shellCommands.forEach(c -> c.getOutputs().forEach(o -> outputProducer.put(o.getResourceName(), c)));

        // initialize the dependency register
        shellCommands.forEach(c -> dependencyRegister.put(c, new ArrayList<>()));
        shellCommands.forEach(c -> addDependencyToRegister(c));

        // initialize dependent command register
        initializeDependentCommandRegister(shellCommands);
    }

    private void initializeDependentCommandRegister(List<Command> shellCommands) {
        shellCommands.forEach(c -> dependentCommandRegister.put(c, new ArrayList<>()));
        dependencyRegister.entrySet().stream().forEach(entry -> {
            Command command = entry.getKey();
            List<CommandDependency> dependencies = entry.getValue();

            if (!dependencies.isEmpty()) {
                dependencies.stream().forEach(cd -> {
                    CommandDependency dependent = new CommandDependency(command, cd.getType());
                    dependentCommandRegister.get(cd.getDependency()).add(dependent);
                });
            }
        });
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

    public List<Command> getCommandsWithNoDependencies(){
        List<Command> commandsWithNoDependencies = new ArrayList<>();

        dependencyRegister.entrySet().stream().forEach(es -> {
            Command command = es.getKey();
            List<CommandDependency> commandDeps = es.getValue();

            if (commandDeps.isEmpty()) commandsWithNoDependencies.add(command);
        });

        return commandsWithNoDependencies;
    }

    private void findSoftDeps(Command command, List<CommandDependency> dependencies, List<Command> currentDepList) {
        List<Command> foundDeps = new ArrayList<>();

        if (!dependencies.isEmpty() && !currentDepList.contains(command)) {

            dependencies.forEach(cd -> {
                if (cd.getType() == SOFT) {

                    Command dep = cd.getDependency();
                    foundDeps.add(dep);
                    findSoftDeps(dep, dependencyRegister.get(dep), currentDepList);
                }
            });

            if (!foundDeps.isEmpty()) currentDepList.add(command);
        }

    }

    public List<Command> getCommandsWithSoftDependencies() {
        List<Command> softDepends = new ArrayList<Command>();

        dependencyRegister.entrySet().stream().forEach(es -> {
            Command command = es.getKey();
            List<CommandDependency> commandDeps = es.getValue();

            findSoftDeps(command, commandDeps, softDepends);
        });

        return softDepends;
    }

    private void findHardDeps(Command command, List<CommandDependency> dependencies, List<Command> currentDepList) {
        List<Command> foundDeps = new ArrayList<>();

        if (!dependencies.isEmpty() && !currentDepList.contains(command)) {

            dependencies.forEach(cd -> {
                if (cd.getType() == HARD) {
                    Command dep = cd.getDependency();
                    foundDeps.add(dep);
                    findHardDeps(dep, dependencyRegister.get(dep), currentDepList);
                }
            });

            if (!foundDeps.isEmpty()) currentDepList.add(command);
        }

    }

    public List<Command> getCommandsWithHardDependencies() {
        List<Command> hardDepends = new ArrayList<Command>();

        dependencyRegister.entrySet().stream().forEach(es -> {
            Command command = es.getKey();
            List<CommandDependency> commandDeps = es.getValue();

            findHardDeps(command, commandDeps, hardDepends);
        });

        return hardDepends;
    }

    public List<CommandDependency> getDependencies(Command c) {
        return dependencyRegister.get(c);
    }

    public List<CommandDependency> getDependentCommands(Command c) {
        return dependentCommandRegister.get(c);
    }

    public List<Command> getCommandsThatHaveDependents() {
        return dependentCommandRegister.entrySet().stream()
                .filter(es -> ! es.getValue().isEmpty())
                .map(es -> es.getKey())
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public List<Command> getCommandsThatHaveDependents(DependencyType type) {
        return dependentCommandRegister.entrySet().stream()
                .filter(es -> ! es.getValue().isEmpty())
                .filter(es -> es.getValue().stream().anyMatch(v -> v.getType() == type))
                .map(es -> es.getKey())
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public List<Command> getCommands() {
        return commands;
    }
}
