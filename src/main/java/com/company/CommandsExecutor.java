package com.company;

import java.util.List;

public class CommandsExecutor {
    public static void main(String[] args) {

        XMLShellCommandsParser parser = new XMLShellCommandsParser();

        List<Command> commands = parser.parseShellCommandsFromXML("src/main/resources/commands2.xml");

        System.out.println("*** Parsed Commands:\n");
        commands.forEach(c -> System.out.println(c));

        CommandDependencyResolver deps = new CommandDependencyResolver(commands);

        deps.printDependency(commands.get(2));

        System.out.println("\n\n*************************\n");
        System.out.println("Commands that can be lauched for execution in this order:\n");

        deps.getNoDependentAndSoftDependentInExecutionOrder().stream().forEach(c -> System.out.println(c.getId()));

        ExecutionResolver er = new ExecutionResolver(deps);
        er.execute();

    }
}
