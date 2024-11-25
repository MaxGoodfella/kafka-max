package com.max.service;

import com.max.model.Order;
import org.springframework.web.bind.annotation.RequestBody;

public interface OrderService {
    Order send(@RequestBody Order order);
}