package com.company;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        XMLShellCommandsParser parser = new XMLShellCommandsParser();

        List<Command> commands = parser.parseShellCommandsFromXML("src/main/resources/commands.xml");

        System.out.println("*** Parsed Commands:\n");
        commands.forEach(c -> System.out.println(c));

        CommandDependencyResolver deps = new CommandDependencyResolver(commands);

        System.out.println("\n\n*************************\n");
        deps.printDependency(commands.get(2));

        System.out.println("\n\n*************************\n");
        System.out.println("Commands with no dependencies can be launched in parallel:\n");
        deps.getCommandsWithNoDependencies().stream().forEach(c -> System.out.println(c.getId()));

        System.out.println("\n\n*************************\n");
        System.out.println("Commands that have soft dependency that can be lauched for execution in this order after the ones with no dependencies:\n");

        deps.getCommandsWithSoftDependencies().stream().forEach(c -> System.out.println(c.getId()));

        System.out.println("\n\n*************************\n");
        System.out.println("Commands that have hard dependencies, launch must be blocked until task wich depend have finished:\n");
        deps.getCommandsWithHardDependencies().stream().forEach(c -> System.out.println(c.getId()));

        System.out.println("\n\n*************************\n");

        ExecutionResolver er = new ExecutionResolver(deps);
        er.execute();

    }
}
