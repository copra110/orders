package com.barakah.orders.service;

import com.barakah.orders.dto.OrderRequestDto;
import com.barakah.orders.model.Order;

public interface OrderBookService {
    Order placeOrder(OrderRequestDto orderRequestDto);
    Order fetchOrderById(long orderId);
}
