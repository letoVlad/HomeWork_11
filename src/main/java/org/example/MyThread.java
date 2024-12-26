package org.example;

public class MyThread extends Thread {
    private volatile boolean flag = false;
    private Runnable runnable;

    public MyThread(Runnable runnable) {
        this.runnable = runnable;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    @Override
    public void run() {
        while (true) {
            if (runnable != null) {
                setFlag(true);
                try {
                    runnable.run();
                } finally {
                    setFlag(false);
                }
            }
        }
    }
}
