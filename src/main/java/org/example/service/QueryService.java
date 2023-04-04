package org.example.service;

import org.example.db.WorkWithFile;
import org.example.entity.OrdersEntity;
import org.example.entity.UpdatesEntity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class QueryService {
    private final WorkWithFile workWithFile = new WorkWithFile();
    public void processInputLists(List<List<String>> lists)  {
        if (lists == null) return;

        List<OrdersEntity> orders = new ArrayList<>();
        List<UpdatesEntity> bids = new ArrayList<>();
        List<UpdatesEntity> asks = new ArrayList<>();
        List<UpdatesEntity> updateAll = new ArrayList<>();
        List<UpdatesEntity> updatesForOrders = new ArrayList<>();
        for (List<String> stringList: lists) {
            switch (stringList.get(0)) {
                case "o" -> {

                }
                case "u" -> {
                    if (stringList.get(3).equals("bid")) {
                        UpdatesEntity updatesEntity = new UpdatesEntity(
                                stringList.get(0), Integer.parseInt(stringList.get(1)),
                                Integer.parseInt(stringList.get(2)), stringList.get(3)
                        );
                        addToCollection(updatesEntity, bids, updateAll, updatesForOrders);
                    } else if (stringList.get(3).equals("ask")) {
                        UpdatesEntity updatesEntity = new UpdatesEntity(
                                stringList.get(0), Integer.parseInt(stringList.get(1)),
                                Integer.parseInt(stringList.get(2)), stringList.get(3)
                        );
                        addToCollection(updatesEntity, asks, updateAll, updatesForOrders);
                    }
                }
                case "q" -> {
                    if (stringList.size() == 2) {
                        switch (stringList.get(1)) {
                            case "best_bid" -> {
                                if (!bids.isEmpty()) {
                                    UpdatesEntity bestBid = findBestUpdatesEntity("bid", bids);
                                    workWithFile.writeAskOfBid(bestBid);
                                }
                            }
                            case "best_ask" -> {
                                if (!asks.isEmpty()) {
                                    UpdatesEntity bestAsk = findBestUpdatesEntity("ask", asks);
                                    workWithFile.writeAskOfBid(bestAsk);
                                }
                            }
                        }
                    }
                    else if (stringList.size() == 3) {
                        List<UpdatesEntity> updatesEntities = findUpdatesEntityByPrice(Integer.parseInt(stringList.get(2)), updateAll);
                        workWithFile.writeFile(updatesEntities.size() + "\n");
                    }
                }
            }
        }
    }



    private List<UpdatesEntity> findUpdatesEntityByPrice(int price, List<UpdatesEntity> mainCollection) {
        if (mainCollection == null) return null;
        List<UpdatesEntity> updatesEntities = new ArrayList<>(
                mainCollection.stream().filter(x -> x.getPrice() == price).toList()
        );
        mainCollection.removeAll(updatesEntities);
        return updatesEntities;
    }
    private void addToCollection(UpdatesEntity update, List<UpdatesEntity> specialCollection, List<UpdatesEntity> mainCollection, List<UpdatesEntity> updatesForOrders) {
        if (update == null || specialCollection == null || mainCollection == null) return;
        specialCollection.add(update);
        mainCollection.add(update);
        updatesForOrders.add(update);
    }
    private UpdatesEntity findBestUpdatesEntity(String string, List<UpdatesEntity> list) {
        if (list == null || string == null) return null;
        List<UpdatesEntity> elements = list.stream()
                .filter(x -> x.getTypeOfOrder().equals(string) && x.getSize() != 0)
                .toList();
        UpdatesEntity entity;

        if (string.equals("bid")) {
            entity = elements.stream()
                    .max(Comparator.comparingInt(UpdatesEntity::getPrice))
                    .orElse(null);
        } else if (string.equals("ask")) {
            entity = elements.stream()
                    .min(Comparator.comparingInt(UpdatesEntity::getPrice))
                    .orElse(null);
        } else {
            entity = null;
        }
        list.remove(entity);

        return entity;
    }
}
