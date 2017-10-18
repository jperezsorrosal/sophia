package com.company;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.*;

public class ExecutionResolver {

    private final CommandDependencyResolver dependencyResolver;

    ExecutionResolver(CommandDependencyResolver dependencyResolver) {
        this.dependencyResolver = dependencyResolver;
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

        private CyclicBarrier barrier;

        public TaskWithBarrier(Command command, CyclicBarrier barrier) {
            super(command);
            this.barrier = barrier;
        }

        @Override
        public void run() {
            try {
                System.out.println(Thread.currentThread().getName() + " is waiting on barrier");
                barrier.await();
                System.out.println(Thread.currentThread().getName() + " has crossed the barrier");
            } catch (InterruptedException ex) {
                System.err.println(ex.toString());
            } catch (BrokenBarrierException ex) {
                System.err.println(ex.toString());
            }
        }
    }


    public void execute() {

        List<Command> noDepsCommands = dependencyResolver.getCommandsWithNoDependencies();
        List<Command> softDepsCommands = dependencyResolver.getCommandsWithSoftDependencies();

        List<Command> hardDepsCommands = dependencyResolver.getCommandsWithHardDependencies();

        HashMap<Command, Task> taskRegister = new HashMap<>();

        noDepsCommands.stream().forEach(c -> taskRegister.put(c, new IndependentTask(c)));
        softDepsCommands.stream().forEach(c -> taskRegister.put(c, new IndependentTask(c)));

//        int executorServiceSize = noDepsCommands.size() + softDepsCommands.size();
//
//        if (executorServiceSize > 0) {
//
//            ExecutorService executor = Executors.newFixedThreadPool(executorServiceSize);
//            List<Future<?>> futures = new ArrayList<Future<?>>();
//
//            noDepsCommands.stream().forEach(c -> futures.add(executor.submit(new IndependentTask(c))) );
//            softDepsCommands.stream().forEach(c -> futures.add( executor.submit(new IndependentTask(c)) ));
//
//            for(Future<?> future : futures)
//                try {
//                    future.get();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                } catch (ExecutionException e) {
//                    e.printStackTrace();
//                }
//            executor.shutdown();
//        }
//
//        hardDepsCommands.stream().forEach( c -> {
//            List<CommandDependency> dependencies = dependencyResolver.getDependencies(c);
//            dependencies.stream().forEach(cd -> {
//                cd.getDependency()
//            });
//            final CyclicBarrier cb = new CyclicBarrier(dependencies.size(), )
//        });

    }
}
