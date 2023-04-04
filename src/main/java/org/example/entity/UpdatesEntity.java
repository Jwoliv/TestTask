package org.example.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class UpdatesEntity {
    private String type;
    private int price;
    private int size;
    private String typeOfOrder;

    public UpdatesEntity(String type, int price, int size, String typeOfOrder) {
        this.type = type;
        this.price = price;
        this.size = size;
        this.typeOfOrder = typeOfOrder;
    }
}
