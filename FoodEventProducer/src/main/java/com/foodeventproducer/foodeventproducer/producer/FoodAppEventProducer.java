package com.foodeventproducer.foodeventproducer.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodeventproducer.foodeventproducer.domain.FoodOrderEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
@Slf4j
public class FoodAppEventProducer {

    @Autowired
    KafkaTemplate<Integer, String>  kafkaTemplate;

    @Autowired
    ObjectMapper objectMapper;

    public void sendFoodAppEvent(FoodOrderEvent foodOrderEvent) throws JsonProcessingException {
        Integer key = foodOrderEvent.getFoodEventId();
        String message = objectMapper.writeValueAsString(foodOrderEvent);

        ListenableFuture<SendResult<Integer, String>> listenableFuture = kafkaTemplate.sendDefault(key,message);

        listenableFuture.addCallback(new ListenableFutureCallback<SendResult<Integer, String>>() {
            @Override
            public void onFailure(Throwable ex) {
                handlerFailure(key, message, ex);
            }

            @Override
            public void onSuccess(SendResult<Integer, String> result) {
                handlerSuccess(key, message, result);
            }
        });
    }

    private ProducerRecord<Integer, String> buildProducerRecord(Integer key, String value, String topic){
        List<Header> recordHeaders = List.of(new RecordHeader("event-source",
                "scanner".getBytes(StandardCharsets.UTF_8)));

        return new ProducerRecord<>(topic, null, key, value, recordHeaders);
    }

    private void handlerFailure(Integer key, String message, Throwable ex) {
        log.error("Error sending the message. Exception: {}", ex.getMessage());
        try{
            throw ex;
        } catch (Throwable throwable){
            log.error("Error in OnFailure: {}", throwable.getMessage());
        }
    }

    private void handlerSuccess(Integer key, String message, SendResult<Integer, String> result) {
        log.info("Message send successfully from the key: {} and the value {}, partition is {}",
                key, message, result.getRecordMetadata().partition());
    }
}
