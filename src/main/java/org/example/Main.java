package org.example;

import org.example.db.WorkWithFile;
import org.example.entity.Update;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        List<List<String>> lists = new WorkWithFile().readFileByName();
        exampleMethod(lists);
    }
    public static void exampleMethod(List<List<String>> lists) {
        if (lists == null) return;

        WorkWithFile workWithFile = new WorkWithFile();
        workWithFile.cleanFile();

        List<Update> updatesAsk = new ArrayList<>();
        List<Update> updatesBid = new ArrayList<>();
        List<Update> updatesAll = new ArrayList<>();


        for (List<String> strings: lists) {
            char typeRequest = strings.get(0).charAt(0);
            switch (typeRequest) {
                case 'u' -> {
                    int price = Integer.parseInt(strings.get(1));
                    int size = Integer.parseInt(strings.get(2));
                    String typeOfUpdate = strings.get(3);

                    Optional<Update> existingUpdate = updatesAll.stream()
                            .filter(u -> u.getPrice() == price && u.getTypeOfUpdate().equals(typeOfUpdate))
                            .findFirst();

                    if (existingUpdate.isPresent()) {
                        Update update = existingUpdate.get();
                        int newSize = update.getSize() + size;

                        updatesAll.remove(update);
                        if (typeOfUpdate.equals("bid")) {
                            updatesBid.remove(update);
                            update.setSize(newSize);
                            updatesBid.add(update);
                        }
                        else if (typeOfUpdate.equals("ask")) {
                            updatesAsk.remove(update);
                            update.setSize(newSize);
                            updatesAsk.add(update);
                        }

                        update.setSize(newSize);
                        updatesAll.add(update);
                    }
                    else {
                        Update update = new Update(typeRequest, price, size, typeOfUpdate);
                        updatesAll.add(update);

                        if (typeOfUpdate.equals("bid")) {
                            updatesBid.add(update);
                        }
                        else if (typeOfUpdate.equals("ask")) {
                            updatesAsk.add(update);
                        }
                    }
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
                            updatesAll.remove(update);
                            update.setSize(update.getSize() - Integer.parseInt(strings.get(2)));
                            updatesBid.add(update);
                            updatesAll.add(update);
                        }
                    }
                    else if (typeOfOrder.equals("buy")) {
                        update = updatesAsk.stream()
                                .filter(x -> x.getSize() != 0 && x.getTypeOfUpdate().equals("ask"))
                                .min(Comparator.comparingInt(Update::getPrice))
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
                                    .min(Comparator.comparingInt(Update::getPrice))
                                    .orElse(null);
                        }
                        if (update != null) {
                            workWithFile.writeFile(update.getPrice() + "," + update.getSize() + "\n");
                        }
                    }
                    else if (strings.size() == 3) {
                        int price = Integer.parseInt(strings.get(2));
                        long count = updatesAll.stream().filter(x -> x.getPrice() == price).mapToLong(Update::getSize).sum();

                        workWithFile.writeFile(count + "\n");
                    }
                }
            }
        }
    }
}