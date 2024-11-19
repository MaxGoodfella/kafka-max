package com.max.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;

@Slf4j
@Service
@EnableKafka
@RequiredArgsConstructor
public class KafkaConsumer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @KafkaListener(topics = "orders", groupId = "payment_service_group")
    public void listen(String message) {
        log.info("Received Message: {}", message);

        boolean paymentSuccess = new Random().nextBoolean();

        String orderId = extractOrderId(message);
        String paymentStatus = paymentSuccess ? "SUCCESS" : "FAILURE";
        String resultMessage = String.format("order id: %s, payment status: %s", orderId, paymentStatus);

        if (paymentSuccess) {
            kafkaTemplate.send("order-payments", resultMessage);
            log.info("Payment processed successfully for order with id = {}", orderId);
        } else {
            kafkaTemplate.send("failed-orders", resultMessage);
            log.error("Payment failed for order with id = {}", orderId);
        }
    }

    private String extractOrderId(String message) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(message);
            return jsonNode.get("id").asText();
        } catch (JsonProcessingException e) {
            log.error("Error extracting order id from message: {}", message, e);
            throw new RuntimeException("Error extracting orderId from message: " + message, e);
        }
    }

}