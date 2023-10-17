package ru.javawebinar.basejava;

public class MainDeadlock {
    public static void main(String[] args) throws InterruptedException {

        final Object LOCK1 = new Object();
        final Object LOCK2 = new Object();
        getThread(LOCK1, LOCK2).start();
        getThread(LOCK2, LOCK1).start();
    }

    private static Thread getThread(Object o1, Object o2) {
        return new Thread(() -> {
            synchronized (o1) {
                System.out.println("lock " + o1);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                synchronized (o2) {
                    System.out.println("in lock " + o2);
                }
            }
        });
    }
}