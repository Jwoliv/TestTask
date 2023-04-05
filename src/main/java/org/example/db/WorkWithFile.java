package org.example.db;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class WorkWithFile {
    private final String NAME_READ_FILE = "D:\\programming\\Projects\\TestTask\\src\\main\\java\\org\\example\\input.txt";
    private final String NAME_WRITE_FILE = "D:\\programming\\Projects\\TestTask\\src\\main\\java\\org\\example\\output.txt";

    public List<List<String>> readFileByName() {
        File file = new File(NAME_READ_FILE);
        List<List<String>> strings = new ArrayList<>();
        try (Scanner scanner = new Scanner(file)) {
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
        File file = new File(NAME_WRITE_FILE);
        try (
                FileWriter fileWriter = new FileWriter(file, true);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)
        ) {
            bufferedWriter.append(string);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void cleanFile() {
        File file = new File(NAME_WRITE_FILE);
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write("");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
