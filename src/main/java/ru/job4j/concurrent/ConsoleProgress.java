package ru.job4j.concurrent;

public class ConsoleProgress implements Runnable {

    @Override
    public void run() {
        char[] process = {'-', '\\', '|', '/'};
        int idx = 0;
        while (!Thread.currentThread().isInterrupted()) {
            try {
                System.out.print("\rLoading... " + process[idx % process.length]);
                idx++;
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) {
        try {
            Thread progress = new Thread(new ConsoleProgress());
            progress.start();
            Thread.sleep(5000);
            progress.interrupt();
            progress.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
