package com.foodeventproducer.foodeventproducer.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data //getter and setters
@Builder
public class Order {
    private Integer foodId;
    private String foodName;
    private String foodRestaurant;
}
