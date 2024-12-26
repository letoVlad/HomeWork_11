package org.example;

import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ScalableThreadPool implements ThreadPool {
    private final int minAmountOfThreads;
    private final int maxAmountOfThreads;
    private MyThread[] threads;
    private BlockingQueue<Runnable> blockingQueue = new LinkedBlockingQueue<>();


    public ScalableThreadPool(int minAmountOfThreads, int maxAmountOfThreads) {
        this.minAmountOfThreads = minAmountOfThreads;
        this.maxAmountOfThreads = maxAmountOfThreads;
        this.threads = new MyThread[minAmountOfThreads];
        this.blockingQueue = new LinkedBlockingQueue<>();
    }


    @Override
    public void start() {
        for (int i = 0; i < minAmountOfThreads; i++) {
            threads[i] = new MyThread(() -> {
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

        if (Arrays.stream(threads).allMatch(MyThread::isFlag)) {
            if (getActiveThreadCount() < maxAmountOfThreads) {
                MyThread[] newThreads = Arrays.copyOf(threads, threads.length + 1);
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
                newThreads[threads.length] = newThread;
                threads = newThreads;
                newThread.start();
            }
        }
    }

    public long getActiveThreadCount() {
        return Arrays.stream(threads).filter(Thread::isAlive).count();
    }
}
