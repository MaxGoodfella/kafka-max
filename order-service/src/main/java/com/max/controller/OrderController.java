package com.max.controller;

import com.max.kafka.KafkaProducer;
import com.max.model.Order;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {

    private final KafkaProducer kafkaProducer;

    @Getter
    private final Map<String, Boolean> orderCache = new HashMap<>();

    @PostMapping
    public Order send(@RequestBody Order order) throws BadRequestException {
        log.info("Received order: {}", order);

        if (order.getId() == null || order.getId().isEmpty()) {
            order.setId(UUID.randomUUID().toString());
        }

        if (orderCache.containsKey(order.getId())) {
            log.warn("Order with id {} already exists", order.getId());
            throw new BadRequestException("Order with the same id already exists");
        }

        kafkaProducer.send(order);

        orderCache.put(order.getId(), true);

        return order;
    }

}