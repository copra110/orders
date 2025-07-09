package com.barakah.orders;

import com.barakah.orders.dto.OrderRequestDto;
import com.barakah.orders.model.Direction;
import com.barakah.orders.model.Order;
import com.barakah.orders.service.OrderBookService;
import com.barakah.orders.service.impl.OrderBookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderBookServiceTest {
    private OrderBookService orderBookService;

    @BeforeEach
    void init()
    {
        orderBookService=new OrderBookServiceImpl();
    }

    @Test
    void assignmentTestCase()
    {
        OrderRequestDto bo1 = new OrderRequestDto("BTC",1.0f,43251.0, Direction.SELL);
        OrderRequestDto bo2 = new OrderRequestDto("BTC",0.25f,43250.0,Direction.BUY);
        OrderRequestDto bo3 = new OrderRequestDto("BTC",0.35f,43253.0,Direction.BUY);

        Order o1 = orderBookService.placeOrder(bo1);
        assertEquals(0L,o1.getId());
        assertEquals(0,o1.getTrades().size());

        Order o2 = orderBookService.placeOrder(bo2);
        assertEquals(1L,o2.getId());
        assertEquals(0,o2.getTrades().size());

        Order o3 = orderBookService.placeOrder(bo3);

        Order getOrder1 = orderBookService.fetchOrderById(0L);
        assertEquals(0L,getOrder1.getId());
        assertEquals(1,o3.getTrades().size());
        assertEquals(1,o1.getTrades().size());
        assertEquals(2L,getOrder1.getTrades().getFirst().getOrderId());
        assertEquals(0.65,getOrder1.getPendingAmount() ,0.0001);
    }

    @Test
    void testExactMatch ()
    {
        OrderRequestDto bo1 = new OrderRequestDto("BTC",1.0f,43251.0, Direction.SELL);
        OrderRequestDto bo2 = new OrderRequestDto("BTC",1.0f,43251.0,Direction.BUY);

        Order o1 = orderBookService.placeOrder(bo1);
        Order o2 = orderBookService.placeOrder(bo2);

        assertEquals(0,o1.getPendingAmount());
        assertEquals(0,o2.getPendingAmount());

        assertEquals(1,o1.getTrades().size());
        assertEquals(1,o2.getTrades().size());
    }

    @Test
    void testNoMatch ()
    {
        OrderRequestDto bo1=new OrderRequestDto("BTC",1.0f,43254.0, Direction.SELL);
        OrderRequestDto bo2=new OrderRequestDto("BTC",1.0f,43251.0,Direction.BUY);

        Order o1 = orderBookService.placeOrder(bo1);
        Order o2 = orderBookService.placeOrder(bo2);

        assertEquals(1,o1.getPendingAmount());
        assertEquals(1,o2.getPendingAmount());

        assertEquals(0,o1.getTrades().size());
        assertEquals(0,o2.getTrades().size());
    }

    @Test
    void testMultipleFulfillMatch()
    {
        OrderRequestDto bo1 = new OrderRequestDto("BTC",3.0f,43251.0, Direction.SELL);
        OrderRequestDto bo2 = new OrderRequestDto("BTC",5.0f,43251.0,Direction.BUY);
        OrderRequestDto bo3 = new OrderRequestDto("BTC",1.0f,43251.0,Direction.SELL);
        OrderRequestDto bo4 = new OrderRequestDto("BTC",2.0f,43251.0,Direction.BUY);
        OrderRequestDto bo5 = new OrderRequestDto("BTC",1.0f,43251.0,Direction.SELL);
        OrderRequestDto bo6 = new OrderRequestDto("BTC",2.0f,43251.0,Direction.SELL);


        Order o1 = orderBookService.placeOrder(bo1);
        assertEquals(0L,o1.getId());

        Order o2 = orderBookService.placeOrder(bo2);
        assertEquals(1L,o2.getId());
        assertEquals(1,o1.getTrades().size());
        assertEquals(1,o2.getTrades().size());
        assertEquals(0,o1.getPendingAmount());
        assertEquals(2,o2.getPendingAmount());
        Order o3 = orderBookService.placeOrder(bo3);
        assertEquals(2L,o3.getId());
        assertEquals(1,o3.getTrades().size());
        assertEquals(0,o3.getPendingAmount());
        assertEquals(2,o2.getTrades().size());
        assertEquals(1,o2.getPendingAmount());
        Order o4 = orderBookService.placeOrder(bo4);
        assertEquals(3L,o4.getId());
        assertEquals(0,o4.getTrades().size());
        assertEquals(2L,o4.getPendingAmount());
        assertEquals(2,o2.getTrades().size());
        Order o5 = orderBookService.placeOrder(bo5);
        assertEquals(4L,o5.getId());
        assertEquals(0,o4.getTrades().size());
        assertEquals(2L,o4.getPendingAmount());
        assertEquals(3,o2.getTrades().size());
        assertEquals(0,o2.getPendingAmount());
        Order o6 = orderBookService.placeOrder(bo6);
        assertEquals(5L,o6.getId());
        assertEquals(1,o4.getTrades().size());
        assertEquals(0,o6.getPendingAmount());
        assertEquals(1,o6.getTrades().size());
        assertEquals(0,o6.getPendingAmount());
    }
}
