package com.kapusniak.tomasz.service;

import com.kapusniak.tomasz.entity.Order;
import com.kapusniak.tomasz.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public void save(Order order){
        orderRepository.save(order);
    }

    public List<Order> findAll(){
        return orderRepository.findAll();
    }
}
