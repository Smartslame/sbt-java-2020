package ru.sbt.smartslame.threads.hw.task2;


import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class TaskTest {
    @Test
    public void test() {
        AtomicInteger counter = new AtomicInteger();
        Task<Integer> task = new Task<>(counter::getAndIncrement);
        System.out.println(task.get());
        assertEquals(0, task.get());
        assertEquals(0, task.get());
        assertEquals(0, task.get());
        assertEquals(0, task.get());
    }

    @Test
    void testRuntimeException() {
        Task<Integer> task = new Task<>(() -> {
            throw new NullPointerException();
        });
        assertThrows(NullPointerException.class, task::get);
    }

    @Test
    void testRuntimeExceptionsAreTheSame() {
        testExceptionsAreTheSame(new NullPointerException());
    }

    @Test
    void testCheckedException() {
        Task<Integer> task = new Task<>(() -> {
            throw new IOException();
        });
        assertThrows(RuntimeException.class, task::get);
    }

    @Test
    void testCheckedExceptionsAreTheSame() {
        testExceptionsAreTheSame(new IOException());
    }

    void testExceptionsAreTheSame(Exception incomingException) {
        Task<Integer> task = new Task<>(() -> {
            throw incomingException;
        });
        RuntimeException exception = new RuntimeException();
        try {
            task.get();
        } catch (RuntimeException e) {
            exception = e;
        }
        try {
            task.get();
        } catch (RuntimeException e) {
            assertEquals(exception, e);
        }
    }
}