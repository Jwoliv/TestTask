package org.example;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class WorkWithFile {
    private final String NAME_READ_FILE = "input.txt";
    private final String NAME_WRITE_FILE = "output.txt";

    public List<List<String>> readFileByName() {
        List<List<String>> strings = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(NAME_READ_FILE))) {
            while (scanner.hasNextLine()) {
                String string = scanner.nextLine();
                strings.add(List.of(string.split(",")));
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return strings;
    }
    public void writeFile(String string) {
        try (
                FileWriter fileWriter = new FileWriter(NAME_WRITE_FILE, true);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)
        ) {
            bufferedWriter.append(string);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void cleanFile() {
        try (FileWriter fileWriter = new FileWriter(NAME_WRITE_FILE)) {
            fileWriter.write("");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}