package com.barakah.orders.service.impl;

import com.barakah.orders.core.OrderBook;
import com.barakah.orders.core.SellComparator;
import com.barakah.orders.dto.OrderRequestDto;
import com.barakah.orders.model.Order;
import com.barakah.orders.model.Trade;
import com.barakah.orders.service.OrderBookService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class OrderBookServiceImpl implements OrderBookService {

    private final AtomicLong idGenerator=new AtomicLong();
    private final Map<String, OrderBook> orderBooks= new ConcurrentHashMap<>();
    private final Map<Long,Order> allOrders=new ConcurrentHashMap<>();


    @Override
    public Order placeOrder(OrderRequestDto orderRequestDto) {
        OrderBook orderBook = orderBooks.computeIfAbsent(orderRequestDto.getAsset(),asset->
                OrderBook.builder()
                        .asset(asset)
                        .sellQueue(new PriorityQueue<Order>(new SellComparator()))
                        .buyQueue(new PriorityQueue<Order>(new SellComparator()))
                        .build());

        long orderId=idGenerator.getAndIncrement();
        Order order = Order.builder()
                .id(orderId)
                .amount(orderRequestDto.getAmount())
                .pendingAmount(orderRequestDto.getAmount())
                .asset(orderRequestDto.getAsset())
                .direction(orderRequestDto.getDirection())
                .price(orderRequestDto.getPrice())
                .timeStamp(LocalDateTime.now())
                .trades(new ArrayList<>())
                .build();

        return orderBook.fullFill(order,allOrders);
    }

    @Override
    public Order fetchOrderById(long orderId) {
        return allOrders.get(orderId);
    }
}
