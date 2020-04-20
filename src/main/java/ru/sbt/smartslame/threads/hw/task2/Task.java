package ru.sbt.smartslame.threads.hw.task2;

import java.util.concurrent.Callable;

public class Task<T> {
    private final Callable<? extends T> callable;
    private volatile boolean finished;
    private T result;
    private RuntimeException exception;

    public Task(Callable<? extends T> callable) {
        this.callable = callable;
    }

    public T get() {
        if (!finished) {
            synchronized (this) {
                if (!finished) {
                    try {
                        this.result = callable.call();
                    } catch (RuntimeException e) {
                        this.exception = e;
                    } catch (Exception e) {
                        this.exception = new RuntimeException(e);
                    } finally {
                        finished = true;
                    }
                }
            }
        }

        if (exception != null) {
            throw exception;
        }

        return result;
    }
}
