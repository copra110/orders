package com.barakah.orders.core;

import com.barakah.orders.model.Order;
import com.barakah.orders.model.Trade;
import lombok.Builder;
import lombok.Data;

import java.util.Map;
import java.util.PriorityQueue;

@Data
@Builder
public class OrderBook {
    private final String asset;
    private final PriorityQueue<Order> buyQueue;
    private final PriorityQueue<Order> sellQueue;

    public synchronized Order fullFill(Order order, Map<Long,Order> allOrders)
    {
        switch (order.getDirection())
        {
            case BUY:
            {
                while (!order.isCompleted() && !this.sellQueue.isEmpty())
                {
                    Order activeSellOrder = this.sellQueue.peek();
                    if(order.getPrice()>=activeSellOrder.getPrice())
                    {
                        float tradeAmount=Math.min(order.getAmount(),activeSellOrder.getAmount());
                        order.fill(Trade.builder()
                                .orderId(activeSellOrder.getId())
                                .amount(tradeAmount)
                                .price(activeSellOrder.getPrice())
                                .build());
                        activeSellOrder.fill(Trade.builder()
                                .orderId(order.getId())
                                .amount(tradeAmount)
                                .price(activeSellOrder.getPrice())
                                .build());
                        if(activeSellOrder.isCompleted())
                        {
                            allOrders.put(activeSellOrder.getId(),this.sellQueue.poll());
                        }
                        else
                        {
                            break;
                        }
                    }
                    else
                    {
                        break;
                    }
                }
                if(!order.isCompleted())
                {
                    this.buyQueue.add(order);
                }
                allOrders.put(order.getId(),order);
                return order;
            }
            case SELL:
            {
                while (!order.isCompleted() && !this.buyQueue.isEmpty())
                {
                    Order activeBuyOrder = this.buyQueue.peek();
                    if(order.getPrice()<=activeBuyOrder.getPrice())
                    {
                        float tradeAmount=Math.min(order.getAmount(),activeBuyOrder.getAmount());
                        order.fill(Trade.builder()
                                .orderId(activeBuyOrder.getId())
                                .amount(tradeAmount)
                                .price(activeBuyOrder.getPrice())
                                .build());
                        activeBuyOrder.fill(Trade.builder()
                                .orderId(order.getId())
                                .amount(tradeAmount)
                                .price(activeBuyOrder.getPrice())
                                .build());
                        if(activeBuyOrder.isCompleted())
                        {
                            allOrders.put(activeBuyOrder.getId(),this.buyQueue.poll());
                        }
                        else
                        {
                            break;
                        }
                    }
                    else
                    {
                        break;
                    }
                }
                if(!order.isCompleted())
                {
                    this.sellQueue.add(order);
                }
                allOrders.put(order.getId(),order);
                return order;
            }
            default:
                throw new RuntimeException("Wrong direction added somehow");
        }
    }

}
