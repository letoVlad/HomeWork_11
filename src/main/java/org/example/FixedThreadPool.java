package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class FixedThreadPool implements ThreadPool {
    private final int amountOfThreads;
    private final BlockingQueue<Runnable> blockingQueue = new LinkedBlockingQueue<>();
    private final List<Thread> threads = new CopyOnWriteArrayList<>();
    private volatile boolean isShutdown = false;
    private final AtomicInteger activeThreads = new AtomicInteger(0);


    public FixedThreadPool(int amountOfThreads) {
        this.amountOfThreads = amountOfThreads;
    }

    @Override
    public void start() {
        for (int i = 0; i < amountOfThreads; i++) {
            addNewThread();
        }
    }

    private void addNewThread() {
        Thread newThread = new Thread(() -> {
            while (true) {
                try {
                    Runnable task = blockingQueue.poll(1, TimeUnit.SECONDS);
                    if (task != null) {
                        activeThreads.incrementAndGet();
                        try {
                            task.run();
                        } finally {
                            activeThreads.decrementAndGet();
                            synchronized (this) {
                                notifyAll();
                            }
                        }
                    } else if (isShutdown && blockingQueue.isEmpty()) {
                        synchronized (this) {
                            notifyAll();
                        }
                        break;
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        threads.add(newThread);
        newThread.start();
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
        isShutdown = true;

        synchronized (this) {
            while (!blockingQueue.isEmpty() || activeThreads.get() > 0) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }

        threads.forEach(Thread::interrupt);
        threads.clear();
    }
}

