package ru.sbt.smartslame.threads.hw.task3;

public interface ExecutionManager {
    Context execute(Runnable callback, Runnable... tasks);
}
