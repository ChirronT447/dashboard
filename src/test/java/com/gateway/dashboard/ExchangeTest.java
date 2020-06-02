package com.gateway.dashboard;

import com.gateway.dashboard.exchange.Direction;
import com.gateway.dashboard.exchange.Exchange;
import com.gateway.dashboard.exchange.Order;
import com.gateway.dashboard.exchange.Order.*;
import com.gateway.dashboard.exchange.OrderBook;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.assertEquals;

/*
    String direction; // BUY or SELL
    String price; // price for htis order
    Integer units; // units being offered
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ExchangeTest {

    Exchange exchange;

    @BeforeEach
    public void setup() {
        exchange = new Exchange();
    }

    @Test
    public void testSubmitBuyOrder() {
        Order order = new OrderBuilder()
                .setDirection(Direction.BUY)
                .setPrice(1.0)
                .setUnits(15)
                .build();

        exchange.submitOrder(order);

        OrderBook orderBook = exchange.getOrderBook();
        Integer units = orderBook.getBuySide().get(1.0);
        //Double price = orderBook.getBuySide().get(15);

        assertEquals(units, 15);
        //assertEquals(price, 1.0);
    }

    @Test
    public void testSubmitSellOrder() {
        Order order = new OrderBuilder()
                .setDirection(Direction.SELL)
                .setPrice(1.0)
                .setUnits(15)
                .build();

        exchange.submitOrder(order);

        OrderBook orderBook = exchange.getOrderBook();
        Integer units = orderBook.getSellSide().get(1.0);
        //Double price = orderBook.getSellSide().getPriceAtUnits(15);

        assertEquals(units, 15);
        //assertEquals(price, 1.0);
    }

    @Test
    public void testSubmitSellOrderDifferentPriceAndUnits() {
        Order order = new OrderBuilder()
                .setDirection(Direction.SELL)
                .setPrice(5.0)
                .setUnits(10)
                .build();

        exchange.submitOrder(order);

        OrderBook orderBook = exchange.getOrderBook();
        Integer units = orderBook.getSellSide().get(5.0);
        //Double price = orderBook.getBuySide().getPriceAtUnits(10);

        assertEquals(units, 10);
        //assertEquals(price, 5.0);
    }

    // BUy side should be descending
    // Sell side should be ordered ascending

}
