package com.max.controller;

import com.max.model.Order;
import com.max.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public Order send(@RequestBody Order order) {
        log.info("Received order: {}", order);
        Order savedOrder = orderService.send(order);
        log.info("Order saved with id = {}", savedOrder.getId());
        return savedOrder;
    }

}