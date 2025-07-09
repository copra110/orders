package com.barakah.orders.core;

import com.barakah.orders.model.Order;

import java.util.Comparator;

public class BuyComparator implements Comparator<Order> {
    @Override
    public int compare(Order o1, Order o2) {
        if(Double.compare(o2.getPrice(),o1.getPrice())!=0)
        {
            return Double.compare(o2.getPrice(),o1.getPrice());
        }
        else
        {
            return o1.getTimeStamp().compareTo(o2.getTimeStamp());
        }
    }
}
