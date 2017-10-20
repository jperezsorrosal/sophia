package com.company;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import com.company.StringColor.*;

import static com.company.DependencyType.HARD;
import static com.company.StringColor.*;


public class ExecutionResolver {

    private final CommandDependencyResolver dependencyResolver;
    private final Map<Command, TaskWithBarrier> taskRegister = new HashMap<>();


    ExecutionResolver(CommandDependencyResolver dependencyResolver) {
        this.dependencyResolver = dependencyResolver;

        // initialize task register with Tasks for each command
        dependencyResolver.getCommands().forEach(c -> {
            taskRegister.put(c, new TaskWithBarrier(c));
        });


        // create barriers for each command with hard dependency and set them up in corresponding Tasks

        dependencyResolver.getCommandsWithHardDependencies().stream().forEach(c -> {
            List<CommandDependency> dependencies = dependencyResolver.getDependencies(c, HARD);

            CyclicBarrier cb = new CyclicBarrier(dependencies.size(), taskRegister.get(c));

            dependencies.stream().forEach(d -> {
                // add barrier to the task that corresponds to the dependency command
                taskRegister.get(d.getDependency()).addBarrier(cb);
            });
        });
    }

    abstract class Task implements Runnable {
        Command command;

        public Task(Command command) {
            this.command = command;
        }


        @Override
        public void run() {

        }
    }

    private class IndependentTask extends Task {

        public IndependentTask(Command command) {
            super(command);
        }

        @Override
        public void run() {
            try {
                Process p = Runtime.getRuntime().exec(command.getCommand());
                System.out.println("Command [" + command.getId() + "] has been launched...");
                p.waitFor();
                System.out.println("Command [" + command.getId() + "] has finished...");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private class TaskWithBarrier extends Task {

        private List<CyclicBarrier> barrierList = new ArrayList<>();

        public TaskWithBarrier(Command command) {
            super(command);
        }

        void addBarrier(CyclicBarrier barrier) {
            barrierList.add(barrier);
        }

        @Override
        public void run() {

            System.out.println(StringColor.colour("Task for command [" + command.getId() + "] has been launched.", GREEN));

            try {
                Thread.sleep((new Random()).nextInt(2000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(StringColor.colour("Task for command [" + command.getId() + "] has been finished.", BLUE));

            barrierList.stream().forEach(b -> {
                    try {
                        System.out.println(StringColor.colour("Task [" + command.getId() + "]" + " is waiting on barrier", RED));
                        b.await();
                        System.out.println(StringColor.colour("Command [" + command.getId() + "]" + " has crossed the barrier", YELLOW));
                    } catch (InterruptedException ex) {
                        System.err.println(ex.toString());
                    } catch (BrokenBarrierException ex) {
                        System.err.println(ex.toString());
                    }
            });

        }
    }


    public void execute() {

        List<Command> noDepsCommands = dependencyResolver.getCommandsWithNoDependencies();
        List<Command> softDepsCommands = dependencyResolver.getCommandsWithSoftDependencies();

        List<Command> hardDepsCommands = dependencyResolver.getCommandsWithHardDependencies();

        noDepsCommands.stream().forEach(c -> (new Thread(taskRegister.get(c))).start() );
        softDepsCommands.stream().forEach(c -> (new Thread(taskRegister.get(c))).start() );

    }
}
