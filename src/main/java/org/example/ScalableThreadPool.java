package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ScalableThreadPool implements ThreadPool {
    private final int minAmountOfThreads;
    private final int maxAmountOfThreads;
    private final List<MyThread> threads = new CopyOnWriteArrayList<>();
    private final BlockingQueue<Runnable> blockingQueue = new LinkedBlockingQueue<>();
    private final AtomicInteger activeThreads = new AtomicInteger(0);
    private volatile boolean isShutdown = false;


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
        if (runnable == null) {
            throw new IllegalArgumentException("Задача не может быть null!");
        }
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

    /*
    Завершает пул потоков и дожидается выполнение всех задач.
     */
    public void shutdownAndWait() {
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
