package org.example.entity;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Update {
    private char type;
    private int price;
    private int size;
    private String typeOfUpdate;

    public Update(char type, int size) {
        this.type = type;
        this.size = size;
    }
}
