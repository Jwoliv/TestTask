package org.example;

import java.io.File;
import java.util.Scanner;

import static org.example.WorkWithFile.NAME_READ_FILE;

public class Main {
    public static void main(String[] args) {
        OrderBook orderBook = new OrderBook();
        try (Scanner scanner = new Scanner(new File(NAME_READ_FILE))) {
            while (scanner.hasNextLine()) {
                String[] element = scanner.nextLine().split(",");
                char identify = element[0].charAt(0);
                switch (identify) {
                    case 'u' -> orderBook.update(element);
                    case 'q' -> orderBook.query(element);
                    case 'o' -> orderBook.marketOrder(element);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        orderBook.writeResultToFile();
    }
}