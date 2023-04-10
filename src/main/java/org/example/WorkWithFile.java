package org.example;

import java.io.BufferedWriter;
import java.io.FileWriter;

public class WorkWithFile {
    public static final String NAME_READ_FILE = "input.txt";
    private static final String NAME_WRITE_FILE = "output.txt";

    public void writeFile(String string) {
        try (
                FileWriter fileWriter = new FileWriter(NAME_WRITE_FILE, true);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)
        ) {
            bufferedWriter.write(string);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}