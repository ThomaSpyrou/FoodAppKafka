package com.foodeventproducer.foodeventproducer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.foodeventproducer.foodeventproducer.domain.FoodAppEventType;
import com.foodeventproducer.foodeventproducer.domain.FoodOrderEvent;
import com.foodeventproducer.foodeventproducer.producer.FoodAppEventProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/kafkaProducerAPI")
@Slf4j
public class FoodEventsController {

    @Autowired
    FoodAppEventProducer foodAppEventProducer;

    @PostMapping(path = "foodEvent")
    public ResponseEntity<FoodOrderEvent> createFoodEvent(@RequestBody FoodOrderEvent foodOrderEvent)
            throws JsonProcessingException {
        //invoke Kafka Producer
        log.info("before");
        foodOrderEvent.setFoodAppEventType(FoodAppEventType.NEW);
        foodAppEventProducer.sendFoodAppEvent(foodOrderEvent);
        log.info("after");

        return ResponseEntity.status(HttpStatus.CREATED).body(foodOrderEvent);
    }
}
