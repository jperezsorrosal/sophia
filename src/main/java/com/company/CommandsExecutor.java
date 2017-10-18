package com.company;

import java.util.List;

public class CommandsExecutor {
    public static void main(String[] args) {

        XMLShellCommandsParser parser = new XMLShellCommandsParser();

        List<Command> commands = parser.parseShellCommandsFromXML("/Users/jperezsl/IdeaProjects/sophia/src/main/resources/commands.xml");

        System.out.println("*** Parsed Commands:\n");
        commands.forEach(c -> {
            System.out.println("Command [" + c.getId() + "]: " + c.getCommand());
            System.out.println("Input Resources:");
            c.getInputs().forEach(i -> System.out.println("Resource: " + i.getResourceName() + " Type: " + i.getType().name()));
            System.out.println("Output Resources:");
            c.getOutputs().forEach(o -> System.out.println("Resource: " + o.getResourceName() + " Type: " + o.getType().name()));
            System.out.println("\n");
        });

        CommandDependencyResolver deps = new CommandDependencyResolver(commands);

        deps.printDependency(commands.get(2));

        ExecutionResolver er = new ExecutionResolver(deps);
        er.execute();

    }
}
