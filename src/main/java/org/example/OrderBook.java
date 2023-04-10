package org.example;

import java.util.*;

public class OrderBook {
    private final WorkWithFile workWithFile = new WorkWithFile();
    private final StringBuilder stringBuilder = new StringBuilder();
    private Map<Integer, Integer> bids;
    private Map<Integer, Integer> asks;

    public OrderBook() {
        bids = new TreeMap<>(Collections.reverseOrder());
        asks = new TreeMap<>();
    }
    public void update(String[] input) {
        int price = Integer.parseInt(input[1]);
        int size = Integer.parseInt(input[2]);
        if (input[3].equals("bid")) {
            if (size == 0) {
                bids.remove(price);
            } else {
                bids.put(price, size);
            }
        } else if (input[3].equals("ask")) {
            if (size == 0) {
                asks.remove(price);
            } else {
                asks.put(price, size);
            }
        }
    }
    public void query(String[] input) {
        switch (input[1]) {
            case "best_bid" -> {
                int price = bids.keySet().stream().findFirst().orElse(0);
                int size = bids.getOrDefault(price, 0);
                stringBuilder.append(String.format("%s,%s", price, size));
            }
            case "best_ask" -> {
                int price = asks.keySet().stream().findFirst().orElse(0);
                int size = asks.getOrDefault(price, 0);
                stringBuilder.append(String.format("%s,%s\n", price, size));
            }
            case "size" -> {
                int price = Integer.parseInt(input[2]);
                int size = bids.getOrDefault(price, asks.getOrDefault(price, 0));
                stringBuilder.append(String.format("%s\n", size));
            }
        }
    }
    private void processOrder(int size, Map<Integer, Integer> collections) {
        Iterator<Integer> it = collections.keySet().iterator();
        while (it.hasNext() && size > 0) {
            int price = it.next();
            int availableSize = collections.get(price);
            if (availableSize <= size) {
                it.remove();
                size -= availableSize;
            } else {
                collections.put(price, availableSize - size);
                size = 0;
            }
        }
    }
    public void marketOrder(String[] input) {
        if (input[1].equals("buy")) {
            processOrder(Integer.parseInt(input[2]), asks);
        } else if (input[1].equals("sell")) {
            processOrder(Integer.parseInt(input[2]), bids);
        }
    }
    public void writeResultToFile() {
        workWithFile.writeFile(stringBuilder.toString());
    }
}
