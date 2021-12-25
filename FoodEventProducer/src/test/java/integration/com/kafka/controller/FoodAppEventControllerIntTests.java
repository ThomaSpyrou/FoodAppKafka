package com.kafka.controller;

import com.foodeventproducer.foodeventproducer.domain.FoodOrderEvent;
import com.foodeventproducer.foodeventproducer.domain.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FoodAppEventControllerIntTests {

    @Autowired
    TestRestTemplate testRestTemplate;


    @Test
    void postFoodAppEvent() {
        Order order = Order.builder()
                .foodId(12)
                .foodName("soupa")
                .foodRestaurant("sth")
                .build();

        FoodOrderEvent foodOrderEvent = FoodOrderEvent.builder()
                .foodEventId(null)
                .order(order)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.set("content-type", MediaType.APPLICATION_JSON.toString());
        HttpEntity<FoodOrderEvent> requestEvent = new HttpEntity<>(foodOrderEvent, headers);

        ResponseEntity<FoodOrderEvent> responseEntity =  testRestTemplate
                .exchange("/kafkaProducerAPI/foodEvent",
                HttpMethod.POST,  requestEvent, FoodOrderEvent.class);

        System.out.println("aaaaaaaaa" + responseEntity.getStatusCode());
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }
}
