package com.barakah.orders.controller;

import com.barakah.orders.dto.OrderRequestDto;
import com.barakah.orders.model.Order;
import com.barakah.orders.service.OrderBookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController
{
    @Autowired
    private OrderBookService orderBookService;

    @PostMapping
    public Order getAllOrders(@RequestBody @Valid OrderRequestDto orderRequestDto)
    {
        return orderBookService.placeOrder(orderRequestDto);
    }

    @GetMapping("/{orderId}")
    public Order fetchOrderById(@PathVariable Long orderId)
    {
        return orderBookService.fetchOrderById(orderId);
    }
}
