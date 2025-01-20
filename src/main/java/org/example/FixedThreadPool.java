package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class FixedThreadPool implements ThreadPool {
    private final int amountOfThreads;
    private BlockingQueue<Runnable> blockingQueue = new LinkedBlockingQueue<>();
    private final List<Thread> threads = new CopyOnWriteArrayList<>();

    public FixedThreadPool(int amountOfThreads) {
        this.amountOfThreads = amountOfThreads;
    }

    @Override
    public void start() {
        for (int i = 0; i < amountOfThreads; i++) {
            Thread newThread = new Thread(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        Runnable task = blockingQueue.take();
                        task.run();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            });
            threads.add(newThread);
            newThread.start();
        }
    }

    @Override
    public void execute(Runnable runnable) throws InterruptedException {
        if (runnable == null) {
            throw new IllegalArgumentException("Задача не может быть null!");
        }
        boolean added = blockingQueue.offer(runnable, 1, TimeUnit.SECONDS);
        if (!added) {
            throw new RejectedExecutionException("Очередь переполнена, задача не добавлена!");
        }
    }

    public void shutdown() {
        for (Thread thread : threads) {
            thread.interrupt();
        }
    }
}

