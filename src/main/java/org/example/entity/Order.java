package org.example.entity;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private char type;
    private String typeOfOrder;
    private int size;
}
