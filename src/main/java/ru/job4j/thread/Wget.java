package ru.job4j.thread;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.regex.Pattern;

public class Wget implements Runnable {

    private final String url;
    private final int speed;

    public Wget(String url, int speed) {
        this.url = url;
        this.speed = speed;
    }

    @Override
    public void run() {
        try (BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
             FileOutputStream out = new FileOutputStream("tmp.txt")) {
            byte[] dataBuffer = new byte[speed];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, speed)) != -1) {
                out.write(dataBuffer, 0, bytesRead);
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        if (args.length != 2) {
            throw new IllegalArgumentException("Count of params must be 2.");
        }
        validateUrl(args[0]);
        validateNum(args[1]);
        String url = args[0];
        int speed = Integer.parseInt(args[1]);
        Thread wget = new Thread(new Wget(url, speed));
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
}
