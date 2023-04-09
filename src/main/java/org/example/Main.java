package org.example;

import java.util.List;

public class Main {
    private static final WorkWithFile workWithFile = new WorkWithFile();
    public static void main(String[] args) {
        OrderBook orderBook = new OrderBook();
        List<String[]> listsOfStrings = workWithFile.readFileByName();
        for (String[] element: listsOfStrings) {
           char identify = element[0].charAt(0);
           switch (identify) {
               case 'u' -> orderBook.update(element);
               case 'q' -> orderBook.query(element);
               case 'o' -> orderBook.marketOrder(element);
           }
        }
    }
}