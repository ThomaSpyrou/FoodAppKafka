package com.foodeventproducer.foodeventproducer.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data //getter and setters
@Builder
public class FoodOrderEvent {
    private Integer foodEventId;
    private FoodAppEventType foodAppEventType;
    private Order order;
}
