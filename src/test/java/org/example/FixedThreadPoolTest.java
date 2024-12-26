package org.example;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FixedThreadPoolTest {


    @Test
    void checksThatTasksHaveBeenCompleted() throws InterruptedException {
        FixedThreadPool threadPool = new FixedThreadPool(3);
        AtomicReference<Double> result = new AtomicReference<>();

        threadPool.start();

        threadPool.execute(() -> {
            result.set(Math.cos(6));
        });

        Thread.sleep(100);

        assertEquals(0.960170286650366, result.get());
    }

    @Test
    void taskExecutionOrder() throws InterruptedException {
        FixedThreadPool threadPool = new FixedThreadPool(2);
        AtomicInteger counter = new AtomicInteger(0);

        threadPool.start();

        threadPool.execute(() -> counter.addAndGet(1));
        threadPool.execute(() -> counter.addAndGet(1));
        threadPool.execute(() -> counter.addAndGet(1));

        Thread.sleep(100);

        assertEquals(3, counter.get());
    }

}