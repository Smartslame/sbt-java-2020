package ru.sbt.smartslame.threads.hw.task1;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class FixedThreadPool implements ThreadPool {
    private final int threadsCount;
    private final Queue<Runnable> tasks;
    private final List<Thread> threads;

    public FixedThreadPool(int threadsCount) {
        this.threadsCount = threadsCount;
        this.tasks = new ArrayDeque<>();
        this.threads = new ArrayList<>(threadsCount);

        for (int i = 0; i < threadsCount; i++) {
            this.threads.add(new Worker());
        }
    }

    private class Worker extends Thread {
        @Override
        public void run() {
            while (true) {
                Runnable task = null;
                synchronized (tasks) {
                    while (tasks.isEmpty()) {
                        try {
                            tasks.wait();
                        } catch (InterruptedException ignored) {
                        }
                    }
                }

                synchronized (tasks) {
                    if (!tasks.isEmpty()) {
                        task = tasks.poll();
                    }
                }

                if (task != null) {
                    task.run();
                }
            }
        }
    }

    @Override
    public void start() {
        for (Thread thread : threads) {
            thread.start();
        }
    }

    @Override
    public synchronized void execute(Runnable runnable) {
        synchronized (tasks) {
            tasks.add(runnable);
            tasks.notify();
        }

    }

    public static void main(String[] args) {
        final FixedThreadPool threadPool = new FixedThreadPool(5);
        threadPool.start();
        for (int i = 0; i < 20; i++) {
            threadPool.execute(() -> {
                System.out.println(Thread.currentThread().getName());
                try {
                    Thread.currentThread().sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
