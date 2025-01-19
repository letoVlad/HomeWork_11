package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;


public class ScalableThreadPool implements ThreadPool {
    private final int minAmountOfThreads;
    private final int maxAmountOfThreads;
    private final List<MyThread> threads = new ArrayList<>();
    private final BlockingQueue<Runnable> blockingQueue = new LinkedBlockingQueue<>();

    public ScalableThreadPool(int minAmountOfThreads, int maxAmountOfThreads) {
        this.minAmountOfThreads = minAmountOfThreads;
        this.maxAmountOfThreads = maxAmountOfThreads;
    }

    @Override
    public void start() {
        for (int i = 0; i < minAmountOfThreads; i++) {
            addNewThread();
        }
    }

    @Override
    public void execute(Runnable runnable) throws InterruptedException {
        boolean added = blockingQueue.offer(runnable, 1, TimeUnit.SECONDS);
        if (!added) {
            throw new RejectedExecutionException("Очередь переполнена, задача не добавлена!");
        }
        maybeAddThread();
    }


    private void maybeAddThread() {
        long activeThreads = threads.stream()
                .filter(t -> t.getState() != Thread.State.WAITING)
                .count();

        if (activeThreads == threads.size() && threads.size() < maxAmountOfThreads) {
            addNewThread();
        }
    }

    private void addNewThread() {
        MyThread newThread = new MyThread(() -> {
            while (true) {
                try {
                    Runnable task = blockingQueue.take();
                    task.run();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        threads.add(newThread);
        newThread.start();
    }

}
