package org.example;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static ArrayList<Integer> bidPrice = new ArrayList<>();
    public static ArrayList<Integer> askPrice = new ArrayList<>();
    public static ArrayList<Integer> bidSize = new ArrayList<>();
    public static ArrayList<Integer> askSize = new ArrayList<>();
    public static WorkWithFile workWithFile = new WorkWithFile();

    public static void main(String[] args) {
        workWithFile.cleanFile();
        List<List<String>> stringList = workWithFile.readFileByName();
        for (List<String> strings : stringList) {
            switch (strings.get(0)) {
                case "u" -> {
                    if (strings.get(3).equals("bid")) {
                        addUpdateToBid(strings);
                    } else if (strings.get(3).equals("ask")) {
                        addUpdateToAsk(strings);
                    }
                }
                case "o" -> {
                    int orderSize = Integer.parseInt(strings.get(2));
                    if (strings.get(1).equals("sell")) {
                        int bestBidPrice = bidPrice.stream()
                                .max(Integer::compare)
                                .orElse(0);

                        if (bestBidPrice >= Integer.parseInt(strings.get(2))) {
                            processOrder(orderSize, false);
                        }
                    }
                    else if (strings.get(1).equals("buy")) {
                        int bestAskPrice = askPrice.stream()
                                .min(Integer::compare)
                                .orElse(Integer.MAX_VALUE);

                        if (bestAskPrice <= Integer.parseInt(strings.get(2))) {
                            processOrder(orderSize, true);
                        }
                    }
                }
                case "q" -> {
                    if (strings.size() == 2) {
                        if (strings.get(1).equals("best_bid")) {
                            int maxPrice = bidPrice.stream()
                                    .max(Integer::compare)
                                    .orElse(0);

                            findQuery(maxPrice, bidPrice, bidSize);
                        } else if (strings.get(0).equals("best_ask")) {
                            int minPrice = askPrice.stream()
                                    .min(Integer::compare)
                                    .orElse(0);

                            findQuery(minPrice, askPrice, askSize);
                        }
                    }
                    else if (strings.size() == 3) {
                        int price = Integer.parseInt(strings.get(2));
                        int totalSize = findTotalSizeAtPrice(price);
                        workWithFile.writeFile(totalSize + "\n");
                    }
                }
            }
        }
    }
    public static int findTotalSizeAtPrice(int price) {
        int sumOfSizes = askPrice.stream()
                .filter(p -> p == price)
                .mapToInt(p -> askSize.get(askPrice.indexOf(p)))
                .sum();

        sumOfSizes += bidPrice.stream()
                .filter(p -> p == price)
                .mapToInt(p -> bidSize.get(bidPrice.indexOf(p)))
                .sum();

        return sumOfSizes;
    }
    public static void processOfAddFields(int index, int price, int size, List<Integer> sizes, List<Integer> prices) {
        if (prices == null || sizes == null) return;

        if (price == prices.get(index)) {
            sizes.set(index, size);
        }
        else {
            prices.add(index, price);
            sizes.add(index, size);
        }
        if (index == 0 && size == 0) {
            prices.remove(0);
            sizes.remove(0);
        }
    }
    public static void addUpdateToBid(List<String> strings) {
        if (strings == null) return;

        int price = Integer.parseInt(strings.get(1));
        int size = Integer.parseInt(strings.get(2));
        int index = bidPrice.indexOf(price);
        if (index == -1) {
            bidPrice.add(price);
            bidSize.add(size);
        }
        else {
            processOfAddFields(index, price, size, bidSize, bidPrice);
        }
    }
    public static void addUpdateToAsk(List<String> strings) {
        if (strings == null) return;

        int price = Integer.parseInt(strings.get(1));
        int size = Integer.parseInt(strings.get(2));
        int index = askPrice.indexOf(price);

        if (index == -1 && size > 0) {
            askPrice.add(price);
            askSize.add(size);
        }
        else {
            processOfAddFields(index, price, size, askSize, askPrice);
        }
    }
    public static void processOrder(int size, boolean isBuy) {
        List<Integer> priceList = isBuy ? askPrice : bidPrice;
        List<Integer> sizeList = isBuy ? askSize : bidSize;

        int index;
        if (isBuy) {
            index = priceList.indexOf(priceList.stream().min(Integer::compareTo).orElse(0));
        }
        else {
            index = priceList.indexOf(priceList.stream().max(Integer::compareTo).orElse(0));
        }
        int sizeRemainder = size;
        while (sizeRemainder > 0 && !priceList.isEmpty()) {
            int currentSize = sizeList.get(index);
            if (currentSize <= sizeRemainder) {
                sizeRemainder -= currentSize;
                priceList.remove(index);
                sizeList.remove(index);
            } else {
                sizeList.set(index, currentSize - sizeRemainder);
                sizeRemainder = 0;
            }
        }
    }
    public static void findQuery(int price, List<Integer> prices, List<Integer> sizes) {
        if (prices == null || sizes == null) return;

        int index = prices.indexOf(price);
        int size = sizes.get(index);

        workWithFile.writeFile(price + "," + size + "\n");
    }
}