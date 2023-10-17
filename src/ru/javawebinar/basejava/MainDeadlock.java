package ru.javawebinar.basejava;

public class MainDeadlock {
    public static void main(String[] args) throws InterruptedException {

        Object LOCK1 = new Object();
        Object LOCK2 = new Object();

        new Thread() {
            @Override
            public void run() {

                synchronized (LOCK1) {
                    System.out.println("lock LOCK1");
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    synchronized (LOCK2) {
                        System.out.println("in lock LOCK2");
                    }
                }
            }
        }.start();

        new Thread() {
            @Override
            public void run() {
                synchronized (LOCK2) {
                    System.out.println("lock LOCK2");
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    synchronized (LOCK1) {
                        System.out.println("in lock LOCK1");
                    }
                }
            }
        }.start();
    }
}