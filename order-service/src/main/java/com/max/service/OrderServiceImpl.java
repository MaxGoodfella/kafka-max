package com.max.service;

import com.max.kafka.KafkaProducer;
import com.max.model.Order;
import com.max.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final KafkaProducer kafkaProducer;

    @Override
    public Order send(Order order) {
        if (order.getId() == null) {
            order.setId(ObjectId.get().toString());
        }

        Order savedOrder = orderRepository.save(order);

        kafkaProducer.send(savedOrder);

        return savedOrder;
    }

}