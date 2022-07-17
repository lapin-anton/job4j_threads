package ru.job4j.concurrent;

public class ConsoleProgress implements Runnable {

    @Override
    public void run() {
        try {
            char[] process = {'-', '\\', '|', '/'};
            int idx = 0;
            while (!Thread.currentThread().isInterrupted()) {
                System.out.print("\rLoading... " + process[idx % process.length]);
                idx++;
                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            Thread progress = new Thread(new ConsoleProgress());
            progress.start();
            Thread.sleep(1000);
            progress.interrupt();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
