package org.example;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws InterruptedException {
        ScalableThreadPool threadPool = new ScalableThreadPool(4, 6);
        threadPool.start();

        for (int i = 1; i <= 10; i++) {
            int taskNumber = i;
            threadPool.execute(() -> {
                System.out.println("задача - " + taskNumber + " выполняется потоком -  " + Thread.currentThread().getName());
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println("задача - " + taskNumber + " завершена");
            });
        }
    }
}