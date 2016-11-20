package ua.nure.lab2;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ReadersWriters {
    private static final int COUNT_OF_ITERATORS = 5;
    private static final int COUNT_OF_READERS = 5;
    private static final StringBuilder BUFF = new StringBuilder();
    private static final int BUFFER_SIZE = 10;
    private static final int SLEEP_TIME = 5;
    private static final Object WRITER_MONITOR = new Object();
    private static final Object READER_MONITOR = new Object();

    private static boolean stop;

    private static class Reader implements Runnable{
        private static int counter = 0;

        public void run() {
            while (!stop) {
                try {
                    synchronized (READER_MONITOR) {
                        if (!stop) {
                            READER_MONITOR.wait();
                        }
                        counter++;
                        if (!stop) {
                            read(Thread.currentThread().getName());
                        }
                        synchronized (WRITER_MONITOR) {
                            if (counter % COUNT_OF_READERS == 0) {
                                WRITER_MONITOR.notify();
                            }
                        }
                    }
                } catch (InterruptedException e) {
                    System.out.println("Error in Reader ");
                }
            }
        }

        private void read(String threadName) throws InterruptedException {
            System.out.print("Reader " + threadName + ": ");
            for (int j = 0; j < BUFFER_SIZE; j++) {
                Thread.sleep(SLEEP_TIME);
                System.out.print(BUFF.charAt(j));
            }
            System.out.println();
            Thread.sleep(5);
        }
    }

    private static class Writer implements Runnable {
        public void run() {
            int interval = 0;
            while (!stop) {
                try {
                    synchronized (WRITER_MONITOR) {
                        write();
                        synchronized (READER_MONITOR) {
                            if (!stop) {
                                READER_MONITOR.notifyAll();
                            }
                        }
                        WRITER_MONITOR.wait();
                    }

                } catch (InterruptedException e) {
                    System.out.println("Error in Writer");
                } finally {
                    synchronized (READER_MONITOR) {
                        if (++interval == COUNT_OF_ITERATORS) {
                            stop = true;
                            READER_MONITOR.notifyAll();
                        }
                    }
                }
            }
        }

        private void write() throws InterruptedException {
            BUFF.setLength(0);
            System.out.println("Writer is writing:");
            Random rand = new Random();
            for (int i = 0; i < BUFFER_SIZE; i++) {
                Thread.sleep(SLEEP_TIME);
                char letter = (char) ('a' + rand.nextInt(26));
                System.err.print(letter);
                BUFF.append(letter);
            }
            System.out.println();
            Thread.sleep(5);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread writer = new Thread(new Writer());
        writer.start();
        List<Thread> threadList = new ArrayList<>();
        for (int j = 0; j < COUNT_OF_ITERATORS; j++) {
            threadList.add(new Thread(new Reader()));
        }
        Thread.sleep(20);
        for (Thread reader : threadList) {
            reader.start();
        }
        Thread.sleep(20);
        for (Thread reader : threadList) {
            reader.join();
        }
    }
}
