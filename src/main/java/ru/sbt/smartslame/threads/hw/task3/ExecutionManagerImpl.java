package ru.sbt.smartslame.threads.hw.task3;

import ru.sbt.smartslame.threads.hw.task1.ThreadPool;

import java.util.Arrays;

public class ExecutionManagerImpl implements ExecutionManager {
    private final ThreadPool threadPool;

    public ExecutionManagerImpl(ThreadPool threadPool) {
        this.threadPool = threadPool;
        this.threadPool.start();
    }


    @Override
    public Context execute(Runnable callback, Runnable... tasks) {
        return new ContextImpl(threadPool, callback, Arrays.asList(tasks));
    }
}
