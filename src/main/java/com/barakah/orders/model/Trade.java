package com.barakah.orders.model;

import lombok.*;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Trade {
    private long orderId;
    private float amount;
    private double price;
}
