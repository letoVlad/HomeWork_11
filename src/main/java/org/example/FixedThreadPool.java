package org.example;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class FixedThreadPool implements ThreadPool {
    private final int amountOfThreads;
    private BlockingQueue<Runnable> blockingQueue = new LinkedBlockingQueue<>();
    private final Thread[] threads;

    public FixedThreadPool(int amountOfThreads) {
        this.amountOfThreads = amountOfThreads;
        this.threads = new Thread[amountOfThreads];
    }

    @Override
    public void start() throws InterruptedException {
        for (int i = 0; i < amountOfThreads; i++) {
            threads[i] = new Thread(() -> {
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
            threads[i].start();
        }
    }

    @Override
    public void execute(Runnable runnable) throws InterruptedException {
        blockingQueue.put(runnable);
    }
}

