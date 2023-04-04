package org.example.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class OrdersEntity {
    private String type;
    private int price;
    private String typeOfActivity;
    private int size;

    public OrdersEntity(String type, String typeOfActivity, int size) {
        this.type = type;
        this.price = 0;
        this.typeOfActivity = typeOfActivity;
        this.size = size;
    }
    public OrdersEntity(String type, int price, String typeOfActivity, int size) {
        this.type = type;
        this.price = price;
        this.typeOfActivity = typeOfActivity;
        this.size = size;
    }
}
