package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

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
                        addUpdate(strings, bidPrice, bidSize);
                    } else if (strings.get(3).equals("ask")) {
                        addUpdate(strings, askPrice, askSize);
                    }
                }
                case "o" -> {
                    int orderSize = Integer.parseInt(strings.get(2));
                    if (strings.get(1).equals("sell")) {
                        int bestBidPrice = bidPrice.stream()
                                .max(Integer::compare)
                                .orElse(0);

                        if (bestBidPrice >= Integer.parseInt(strings.get(2))) {
                            processOrder(bestBidPrice, orderSize, bidPrice, bidSize, strings);
                        }
                    }
                    else if (strings.get(1).equals("buy")) {
                        int bestAskPrice = askPrice.stream()
                                .min(Integer::compare)
                                .orElse(Integer.MAX_VALUE);

                        if (bestAskPrice <= Integer.parseInt(strings.get(2))) {
                            processOrder(bestAskPrice, orderSize, askPrice, askSize, strings);
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

                        int sumOfSizes = IntStream.range(0, askPrice.size())
                                .filter(i -> askPrice.get(i) == price)
                                .map(i -> askSize.get(i))
                                .sum();

                        sumOfSizes += IntStream.range(0, bidPrice.size())
                                .filter(i -> bidPrice.get(i) == price)
                                .map(i -> bidSize.get(i))
                                .sum();

                        workWithFile.writeFile(sumOfSizes + "\n");
                    }
                }
            }
        }
    }

    public static void processOrder(int price, int orderSize, List<Integer> prices, List<Integer> sizes, List<String> strings) {
        int index = prices.indexOf(price);
        int size = sizes.get(index);

        if (size >= orderSize) {
            sizes.set(index, size - orderSize);
            int bidPriceElement = Integer.parseInt(strings.get(2));
            int bidIndex = bidPrice.indexOf(bidPriceElement);
            if (bidIndex == -1) {
                prices.add(bidPriceElement);
                sizes.add(orderSize);
            } else {
                sizes.set(bidIndex, sizes.get(bidIndex) + orderSize);
            }
        }
    }
    public static void findQuery(int price, List<Integer> prices, List<Integer> sizes) {
        if (prices == null || sizes == null) return;

        int index = prices.indexOf(price);
        int size = sizes.get(index);

        workWithFile.writeFile(price + "," + size + "\n");
    }
    public static void addUpdate(List<String> strings, List<Integer> prices, List<Integer> sizes) {
        if (strings == null || prices == null || sizes == null) return;

        int price = Integer.parseInt(strings.get(1));
        int size = Integer.parseInt(strings.get(2));

        int index = prices.indexOf(price);
        if (index == -1) {
            prices.add(price);
            sizes.add(size);
        } else {
            sizes.set(index, sizes.get(index) + size);
        }
    }
}