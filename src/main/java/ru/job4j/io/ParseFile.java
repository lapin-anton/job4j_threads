package ru.job4j.io;

import java.io.*;
import java.util.function.Predicate;

public final class ParseFile {

    private final File file;

    public ParseFile(final File file) {
        this.file = file;
    }

    public synchronized File getFile() {
        return file;
    }

    private String content(Predicate<Integer> condition) throws IOException {
        String output = "";
        try (BufferedInputStream i = new BufferedInputStream(new FileInputStream(file))) {
            int data;
            while ((data = i.read()) > 0) {
                if (condition.test(data)) {
                    output += (char) data;
                }
            }
        }
        return output;
    }

    public String getContent() throws IOException {
        return content(data -> true);
    }

    public String getContentWithoutUnicode() throws IOException {
        return content(data -> data < 0x80);
    }

}
