package org.example;

import org.example.db.WorkWithFile;
import org.example.entity.Update;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<List<String>> lists = new WorkWithFile().readFileByName();
        exampleMethod(lists);
    }
    public static void exampleMethod(List<List<String>> lists) {
        if (lists == null) return;

        WorkWithFile workWithFile = new WorkWithFile();

        List<Update> updatesAsk = new ArrayList<>();
        List<Update> updatesBid = new ArrayList<>();
        List<Update> updatesAll = new ArrayList<>();


        for (List<String> strings: lists) {
            char typeRequest = strings.get(0).charAt(0);
            switch (typeRequest) {
                case 'u' -> {
                    Update update = new Update(
                            typeRequest, Integer.parseInt(strings.get(1)),
                            Integer.parseInt(strings.get(2)), strings.get(3)
                    );
                    if (strings.get(3).equals("bid")) {
                        updatesBid.add(update);
                    }
                    else if (strings.get(3).equals("ask")) {
                        updatesAsk.add(update);
                    }
                    updatesAll.add(update);
                }
                case 'o' -> {
                    String typeOfOrder = strings.get(1);
                    Update update;

                    if (typeOfOrder.equals("sell")) {
                        update = updatesBid.stream()
                                .filter(x -> x.getSize() != 0 && x.getTypeOfUpdate().equals("bid"))
                                .max(Comparator.comparingInt(Update::getPrice))
                                .orElse(null);

                        if (update != null && update.getSize() >= Integer.parseInt(strings.get(2))) {
                            updatesBid.remove(update);
                            updatesBid.remove(update);
                            update.setSize(update.getSize() - Integer.parseInt(strings.get(2)));
                            updatesBid.add(update);
                            updatesBid.add(update);
                            System.out.println(update.getSize());
                        }
                    }
                    else if (typeOfOrder.equals("buy")) {
                        update = updatesAsk.stream()
                                .filter(x -> x.getSize() != 0 && x.getTypeOfUpdate().equals("ask"))
                                .max(Comparator.comparingInt(Update::getPrice))
                                .orElse(null);

                        if (update != null && update.getSize() >= Integer.parseInt(strings.get(2))) {
                            updatesAsk.remove(update);
                            updatesAll.remove(update);
                            update.setSize(update.getSize() - Integer.parseInt(strings.get(2)));
                            updatesAsk.add(update);
                            updatesAll.add(update);
                        }
                    }
                }
                case 'q' -> {
                    if (strings.size() == 2) {
                        Update update = null;
                        if (strings.get(1).equals("best_bid")) {
                            update = updatesBid.stream()
                                    .filter(x -> x.getSize() != 0 && x.getTypeOfUpdate().equals("bid"))
                                    .max(Comparator.comparingInt(Update::getPrice))
                                    .orElse(null);
                        }
                        else if (strings.get(1).equals("best_ask")) {
                            update = updatesAsk.stream()
                                    .filter(x -> x.getSize() != 0 && x.getTypeOfUpdate().equals("ask"))
                                    .max(Comparator.comparingInt(Update::getPrice))
                                    .orElse(null);
                        }
                        if (update != null) {
                            workWithFile.writeFile(update.getPrice() + "," + update.getSize() + "\n");
                        }
                    }
                    else if (strings.size() == 3) {
                        int price = Integer.parseInt(strings.get(2));
                        long count = updatesAll.stream().filter(x -> x.getPrice() == price).mapToLong(Update::getSize).sum();

                        updatesAll.removeIf(x -> x.getPrice() == price && x.getSize() > 0);
                        workWithFile.writeFile(count + "\n");
                    }
                }
            }
        }
    }
}