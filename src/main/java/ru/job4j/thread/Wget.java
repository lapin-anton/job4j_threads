package ru.job4j.thread;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.regex.Pattern;

public class Wget implements Runnable {

    private final String url;
    private final int speed;
    private final String output;

    public Wget(String url, int speed, String output) {
        this.url = url;
        this.speed = speed;
        this.output = output;
    }

    @Override
    public void run() {
        try (BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
             FileOutputStream out = new FileOutputStream(output)) {
            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            long bytesWrited = 0;
            long finish;
            long diffTime;
            long start = System.currentTimeMillis();
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                out.write(dataBuffer, 0, bytesRead);
                bytesWrited += bytesRead;
                if (bytesWrited >= speed) {
                    finish = System.currentTimeMillis();
                    diffTime = finish - start;
                    if (diffTime < 1000) {
                        Thread.sleep(1000 - diffTime);
                    }
                    start = System.currentTimeMillis();
                    bytesWrited = 0;
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        if (args.length != 3) {
            throw new IllegalArgumentException("Count of params must be 3.");
        }
        validateUrl(args[0]);
        validateNum(args[1]);
        validateFilePath(args[2]);
        String url = args[0];
        int speed = Integer.parseInt(args[1]);
        String output = args[2];
        Thread wget = new Thread(new Wget(url, speed, output));
        wget.start();
        wget.join();
    }

    private static void validateUrl(String arg) {
        String pattern = "^[(http(s)?):\\/\\/(www\\.)?a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)$";
        if (!Pattern.matches(pattern, arg)) {
            throw new IllegalArgumentException("Param 'url' has incorrect format.");
        }
    }

    private static void validateNum(String arg) {
        String pattern = "^[\\d]+$";
        if (!Pattern.matches(pattern, arg) || Integer.parseInt(arg) <= 0) {
            throw new IllegalArgumentException("Param 'speed' has incorrect format or value.");
        }
    }

    private static void validateFilePath(String arg) {
        String pattern = "([a-zA-Z]:)?(\\\\[a-zA-Z0-9_.-]+)*\\\\?(\\w+\\.\\w+)";
        if (!Pattern.matches(pattern, arg)) {
            throw new IllegalArgumentException("Param 'out' has incorrect file path format.");
        }
    }
}
