package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import static com.company.DependencyType.SOFT;

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
        commands.forEach(c -> {
            System.out.println("Dependent commands of command [" + c.getId() + "]:");
            deps.getDependentCommands(c).forEach(cd -> System.out.println(" - [" + cd.getDependency().getId() + "] " + cd.getType() ));
        });

        System.out.println("\n\n*************************\n");
        System.out.println("Commands that have dependents:\n");
        deps.getCommandsThatHaveDependents().stream().forEach(c -> System.out.println(" - [" + c.getId() + "] "));


        System.out.println("\n\n*************************\n");
        System.out.println("Commands that have dependents of specific type, SOFT:\n");
        deps.getCommandsThatHaveDependents(SOFT).stream().forEach(c -> System.out.println(" - [" + c.getId() + "] "));

        System.out.println("\n\n*************************\n");

        ExecutionResolver er = new ExecutionResolver(deps);
        er.execute();

    }
}
