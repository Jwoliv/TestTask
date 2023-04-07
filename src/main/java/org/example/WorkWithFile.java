package org.example;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WorkWithFile {
    private final String NAME_READ_FILE = "input.txt";
    private final String NAME_WRITE_FILE = "output.txt";

    public List<List<String>> readFileByName() {
        List<List<String>> strings = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(NAME_READ_FILE));
            for (String line : lines) {
                strings.add(Arrays.asList(line.split(",")));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return strings;
    }
    public void writeFile(String string) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(NAME_WRITE_FILE, true))) {
            bufferedWriter.append(string);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void cleanFile() {
        try (FileWriter fileWriter = new FileWriter(NAME_WRITE_FILE)) {
            fileWriter.write("");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
