package org.example.entity;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Query {
    private char type;
    private String request;
    private int size;
}
