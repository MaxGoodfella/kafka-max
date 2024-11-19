package com.max.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.max.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void send(Order order) {
        try {
            String jsonOrder = objectMapper.writeValueAsString(order);
            kafkaTemplate.send("orders", jsonOrder);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

}