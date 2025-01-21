package org.example;

import java.util.concurrent.RejectedExecutionException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws InterruptedException {
//        ScalableThreadPool threadPool = new ScalableThreadPool(6, 8);
//        threadPool.start();
//
//        for (int i = 1; i <= 10; i++) {
//            int taskNumber = i;
//            threadPool.execute(() -> {
//                System.out.println("задача - " + taskNumber + " выполняется потоком -  " + Thread.currentThread().getName());
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    Thread.currentThread().interrupt();
//                }
//                System.out.println("задача - " + taskNumber + " завершена");
//            });
//        }
//        threadPool.shutdownAndWait();
//        System.out.println("Конец");
//
//    }

        FixedThreadPool threadPool = new FixedThreadPool(5);


            threadPool.start();

            for (int i = 0; i < 10; i++) {
                final int taskId = i;
                threadPool.execute(() -> {
                    System.out.println(Thread.currentThread().getName() + " выполняет задачу " + taskId);
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
            }

            threadPool.shutdown();
            System.out.println("Конец");
        }



}