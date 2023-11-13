package com.example.order.RestApi.service;

import com.example.order.RestApi.model.Orders;
import com.example.order.RestApi.repository.OrdersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class OrdersService {

    private final OrdersRepository ordersRepository;

    public List<Orders> getAll() {
        return ordersRepository.findAll();
    }

    public Orders getById(Long id) {
        return ordersRepository.findById(id).orElseThrow(() -> new RuntimeException("There is no order with id: " + id));
    }

    public Orders add(Orders orders) {
        return ordersRepository.save(orders);
    }

    public Orders update(Orders orders) {
        var foundOrder = getById(orders.getId());
        foundOrder.setFirstName(orders.getFirstName());
        foundOrder.setLastName(orders.getLastName());
        foundOrder.setPhone(orders.getPhone());
        foundOrder.setEmail(orders.getEmail());
        foundOrder.setShippingAddress(orders.getShippingAddress());
        foundOrder.setPaymentMethod(orders.getPaymentMethod());
        foundOrder.setDelivered(orders.getDelivered());
        return ordersRepository.save(foundOrder);
    }

    public void delete(Long id) {
        ordersRepository.delete(getById(id));
    }
}
