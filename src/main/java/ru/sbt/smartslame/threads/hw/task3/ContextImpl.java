package ru.sbt.smartslame.threads.hw.task3;

import ru.sbt.smartslame.threads.hw.task1.ThreadPool;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ContextImpl implements Context {
    private final ThreadPool threadPool;
    private final Runnable callback;
    private final List<Runnable> tasks;
    private final AtomicInteger completedTasksCount;
    private final AtomicInteger failedTasksCount;
    private final AtomicInteger interruptedTasksCount;
    private volatile boolean interrupted;
    private boolean finished;

    public ContextImpl(ThreadPool threadPool, Runnable callback, List<Runnable> tasks) {
        this.threadPool = threadPool;
        this.callback = callback;
        this.tasks = tasks;
        this.completedTasksCount = new AtomicInteger(0);
        this.failedTasksCount = new AtomicInteger(0);
        this.interruptedTasksCount = new AtomicInteger(0);
        this.interrupted = false;
        this.finished = false;

        new Thread(this::run).start();
    }

    @Override
    public int getCompletedTaskCount() {
        return completedTasksCount.get();
    }

    @Override
    public int getFailedTaskCount() {
        return failedTasksCount.get();
    }

    @Override
    public int getInterruptedTaskCount() {
        return interruptedTasksCount.get();
    }

    @Override
    public void interrupt() {
        this.interrupted = true;
    }

    @Override
    public boolean isFinished() {
        return finished;
    }

    private void run() {
        runAllTasks();
        waitTasks();
        finished = true;
        callback.run();
    }

    private void runAllTasks() {
        for (Runnable task : tasks) {
            threadPool.execute(() -> runOneTask(task));
        }
    }

    private void runOneTask(Runnable task) {
        if (!interrupted) {
            try {
                task.run();
                completedTasksCount.getAndIncrement();
            } catch (Exception e) {
                failedTasksCount.getAndIncrement();
            }
        } else {
            interruptedTasksCount.getAndIncrement();
        }

        synchronized (this) {
            if (isAllTaskHandled()) {
                this.notify();
            }
        }
    }

    private void waitTasks() {
        synchronized (this) {
            while (!isAllTaskHandled()) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean isAllTaskHandled() {
        return completedTasksCount.get() + failedTasksCount.get() + interruptedTasksCount.get() == tasks.size();
    }

}
