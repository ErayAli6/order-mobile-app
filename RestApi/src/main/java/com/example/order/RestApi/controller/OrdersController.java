package com.example.order.RestApi.controller;

import com.example.order.RestApi.model.Orders;
import com.example.order.RestApi.service.OrdersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/orders")
public class OrdersController {

    private final OrdersService ordersService;

    @GetMapping
    public List<Orders> getAll() {
        return ordersService.getAll();
    }

    @GetMapping("/{id}")
    public Orders getById(@PathVariable Long id) {
        return ordersService.getById(id);
    }

    @PostMapping("/add/{id}")
    public Orders add(@RequestBody Orders orders) {
        return ordersService.add(orders);
    }

    @PutMapping("/update/{id}")
    public Orders update(@RequestBody Orders orders) {
        return ordersService.update(orders);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        ordersService.delete(id);
    }
}
