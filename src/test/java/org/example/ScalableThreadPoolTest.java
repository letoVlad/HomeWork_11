package org.example;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class ScalableThreadPoolTest {

    @Test
    void minimumNumberOfThreadsIsCreated() throws InterruptedException {
        ScalableThreadPool threadPool = new ScalableThreadPool(2, 5);
        threadPool.start();

        Thread.sleep(100);
        assertEquals(2, threadPool.getActiveThreadCount());
    }


    @Test
    void numberOfThreadsScales() throws InterruptedException {
        ScalableThreadPool threadPool = new ScalableThreadPool(2, 5);
        AtomicInteger counter = new AtomicInteger(0);
        threadPool.start();

        for (int i = 0; i < 10; i++) {
            threadPool.execute(() -> {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                counter.incrementAndGet();
            });
        }

        Thread.sleep(300);

        assertTrue(threadPool.getActiveThreadCount() > 2);
        assertTrue(threadPool.getActiveThreadCount() <= 5);

        Thread.sleep(3000);
        assertEquals(10, counter.get());
    }

}