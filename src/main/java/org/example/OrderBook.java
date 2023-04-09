package org.example;

import java.util.*;

public class OrderBook {
    private final WorkWithFile workWithFile = new WorkWithFile();
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
                if (bids.isEmpty()) {
                    System.out.println("Bids empty");
                } else {
                    int price = bids.keySet().iterator().next();
                    int size = bids.get(price);
                    workWithFile.writeFile(price + "," + size + "\n");
                }
            }
            case "best_ask" -> {
                if (asks.isEmpty()) {
                    System.out.println("Asks empty");
                } else {
                    int price = asks.keySet().iterator().next();
                    int size = asks.get(price);
                    workWithFile.writeFile(price + "," + size + "\n");
                }
            }
            case "size" -> {
                int price = Integer.parseInt(input[2]);
                if (bids.containsKey(price)) {
                    workWithFile.writeFile(bids.get(price) + "\n");
                } else {
                    workWithFile.writeFile(asks.getOrDefault(price, 0) + "\n");
                }
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
        int size = Integer.parseInt(input[2]);
        if (input[1].equals("buy")) {
            processOrder(size, asks);
        } else if (input[1].equals("sell")) {
            processOrder(size, bids);
        }
    }
}
