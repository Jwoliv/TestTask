package org.example.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class QueriesEntity {
    private String type;
    private String typeOfOrder;
    private String size;
    private int price;


    public QueriesEntity(String type, String typeOfOrder) {
        this.type = type;
        this.typeOfOrder = typeOfOrder;
        this.size = "";
        this.price = 0;
    }

    public QueriesEntity(String type, String size, int price) {
        this.type = type;
        this.typeOfOrder = "";
        this.size = size;
        this.price = price;
    }
}
