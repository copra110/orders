package com.barakah.orders.core;

import com.barakah.orders.model.Order;

import java.util.Comparator;

public class SellComparator implements Comparator<Order> {
    @Override
    public int compare(Order o1, Order o2) {
        if (Double.compare(o1.getPrice(), o2.getPrice()) != 0) {
            return Double.compare(o1.getPrice(), o2.getPrice());
        } else {
            return o1.getTimeStamp().compareTo(o2.getTimeStamp());
        }
    }
}