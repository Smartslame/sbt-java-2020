package ru.sbt.smartslame.threads.hw.task1;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

public class ScalableThreadPool implements ThreadPool {
    private final int minThreadsCount;
    private final int maxThreadsCount;
    private final Queue<Runnable> tasks;
    private final List<Worker> threads;
    private volatile AtomicInteger freeWorkersCount;

    public ScalableThreadPool(int minThreadsCount, int maxThreadsCount) {
        this.minThreadsCount = minThreadsCount;
        this.maxThreadsCount = maxThreadsCount;
        this.tasks = new ArrayDeque<>();
        this.threads = new ArrayList<>(maxThreadsCount);
        freeWorkersCount = new AtomicInteger(minThreadsCount);

        for (int i = 0; i < minThreadsCount; i++) {
            this.threads.add(new Worker());
        }
    }

    private class Worker extends Thread {
        @Override
        public void run() {
            while (true) {
                Runnable task = null;
                synchronized (tasks) {
                    if (tasks.isEmpty() && threads.size() > minThreadsCount) {
                        threads.remove(Thread.currentThread());
                        freeWorkersCount.getAndDecrement();
                        tasks.notifyAll();
                        return;
                    }

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
                    freeWorkersCount.getAndDecrement();
                    task.run();
                    freeWorkersCount.getAndIncrement();
                }
            }
        }
    }

    @Override
    public void start() {
        for (Worker w : threads) {
            w.start();
        }
    }

    @Override
    public void execute(Runnable runnable) {
        synchronized (tasks) {
            tasks.add(runnable);

            if (freeWorkersCount.get() < tasks.size() && threads.size() < maxThreadsCount) {
                Worker w = new Worker();
                w.start();
                threads.add(w);
                freeWorkersCount.getAndIncrement();
            }

            tasks.notify();

        }
    }

    public static void main(String[] args) {
        final ScalableThreadPool threadPool = new ScalableThreadPool(2, 5);
        threadPool.start();
        for (int i = 0; i < 5; i++) {
            threadPool.execute(() -> {
                System.out.println(Thread.currentThread().getName());
                try {
                    Thread.currentThread().sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        for (int i = 0; i < 20; i++) {
            threadPool.execute(() -> {
                System.out.println(Thread.currentThread().getName());
            });
        }

    }
}
