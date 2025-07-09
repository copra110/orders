package com.barakah.orders.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private Long id;
    private String asset;
    private float amount;
    private double price;
    private Direction direction;
    private float pendingAmount;
    private LocalDateTime timeStamp;
    private List<Trade> trades;

    public Order(Long id, String asset, float amount, double price, Direction direction) {
        this.id = id;
        this.asset = asset;
        this.amount = amount;
        this.price = price;
        this.direction = direction;
        this.pendingAmount=amount;
        this.timeStamp=LocalDateTime.now();
        this.trades= new ArrayList<>();
    }

    @JsonIgnore
    public boolean isCompleted()
    {
        return this.pendingAmount==0;
    }

    public void fill(Trade trade)
    {
        this.pendingAmount-=trade.getAmount();
        this.trades.add(trade);
    }
}
