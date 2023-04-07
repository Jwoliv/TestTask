package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {
    public static ArrayList<Integer> bidPrice = new ArrayList<>();
    public static ArrayList<Integer> askPrice = new ArrayList<>();
    public static ArrayList<Integer> bidSize = new ArrayList<>();
    public static ArrayList<Integer> askSize = new ArrayList<>();
    public static WorkWithFile workWithFile = new WorkWithFile();

    public static void main(String[] args) {
        List<List<String>> stringList = workWithFile.readFileByName();
        if (stringList == null || stringList.isEmpty()) return;

        for (List<String> strings : stringList) {
            switch (strings.get(0)) {
                case "u" -> {
                    if (strings.get(3).equals("bid")) {
                        addUpdate(strings, bidSize, bidPrice);
                    } else if (strings.get(3).equals("ask")) {
                        addUpdate(strings, askSize, askPrice);
                    }
                }
                case "o" -> {
                    int orderSize = Integer.parseInt(strings.get(2));
                    if (strings.get(1).equals("sell")) {
                        processOrder(orderSize, false);
                    }
                    else if (strings.get(1).equals("buy")) {
                        processOrder(orderSize, true);
                    }
                }
                case "q" -> {
                    if (strings.size() == 2) {
                        if (strings.get(1).equals("best_bid")) {
                            int maxPrice = bidPrice.stream().max(Integer::compare).orElse(Integer.MIN_VALUE);
                            if (maxPrice != 0) {
                                findQuery(maxPrice, bidPrice, bidSize);
                            }
                        } else if (strings.get(0).equals("best_ask")) {
                            int minPrice = askPrice.stream().min(Integer::compare).orElse(Integer.MAX_VALUE);
                            if (minPrice != 0) {
                                findQuery(minPrice, askPrice, askSize);
                            }
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

        if (!prices.isEmpty() || !sizes.isEmpty()) {
            if (price == prices.get(index)) {
                sizes.set(index, size);
            } else {
                prices.add(index, price);
                sizes.add(index, size);
            }
        }
        else {
            prices.add(0, price);
            sizes.add(0, size);
        }
    }
    public static void addUpdate(List<String> strings, List<Integer> sizes, List<Integer> prices) {
        if (strings == null || strings.isEmpty()) return;

        int price = Integer.parseInt(strings.get(1));
        int size = Integer.parseInt(strings.get(2));

        if (!prices.isEmpty() && !sizes.isEmpty()) {
            int index = prices.indexOf(price);
            if (index == -1) {
                prices.add(price);
                sizes.add(size);
            } else {
                processOfAddFields(index, price, size, sizes, prices);
            }
        } else {
            processOfAddFields(0, price, size, sizes, prices);
        }
    }
    public static void processOrder(int size, boolean isBuy) {
        List<Integer> priceList = isBuy ? askPrice : bidPrice;
        List<Integer> sizeList = isBuy ? askSize : bidSize;

        if (!priceList.isEmpty() && !sizeList.isEmpty()) {
            int targetPrice = findBestPriceAskOrBid(isBuy, priceList, sizeList);
            int index = priceList.indexOf(targetPrice);

            while (size > 0 || index >= 0 && index < priceList.size() || targetPrice == -1) {
                int currentSize = sizeList.get(index);

                if (currentSize < size) {
                    size -= currentSize;
                    priceList.set(index, targetPrice);
                    sizeList.set(index, 0);
                } else if (currentSize == size) {
                    size -= currentSize;
                    sizeList.set(index, size);
                    break;
                }
                if (sizeList.stream().allMatch(x -> x == 0)) {
                    break;
                }
                targetPrice = findBestPriceAskOrBid(isBuy, priceList, sizeList);
                index = priceList.indexOf(targetPrice);
            }
        }
    }
    public static int findBestPriceAskOrBid(boolean isBuy, List<Integer> priceList, List<Integer> sizeList) {
        int price = -1;
        if (priceList == null || sizeList == null) return price;

        if (!priceList.isEmpty() && !sizeList.isEmpty()) {
            if (isBuy) {
                price = Integer.MIN_VALUE;
                for (int i = 0; i < sizeList.size(); i++) {
                    if (sizeList.get(i) != 0 && price < priceList.get(i)) {
                        price = priceList.get(i);
                    }
                }
            } else {
                price = Integer.MAX_VALUE;
                for (int i = 0; i < sizeList.size(); i++) {
                    if (sizeList.get(i) != 0 && price > priceList.get(i)) {
                        price = priceList.get(i);
                    }
                }
            }
        }
        return price;
    }
    public static void findQuery(int price, List<Integer> prices, List<Integer> sizes) {
        if (prices == null || sizes == null || prices.isEmpty() || sizes.isEmpty()) return;

        int index = prices.indexOf(price);
        int size = sizes.get(index);

        workWithFile.writeFile(price + "," + size + "\n");
    }
}