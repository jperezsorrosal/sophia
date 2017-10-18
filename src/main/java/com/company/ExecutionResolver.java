package com.company;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class ExecutionResolver {

    private final CommandDependencyResolver dependencyResolver;

    ExecutionResolver(CommandDependencyResolver dependencyResolver) {
        this.dependencyResolver = dependencyResolver;
    }

    private static class TaskWithBarrier implements Runnable {

        private CyclicBarrier barrier;

        public TaskWithBarrier(CyclicBarrier barrier) {
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

        dependencyResolver.getCommandsWithSoftDependencies();
    }
}
