package com.gateway.dashboard.exchange;

// Processes entries to the order book
public class Exchange {

    OrderBook orderBook = new OrderBook();

    public void submitOrder(Order order) {
        orderBook.insertOrder(order);
    }

    public OrderBook getOrderBook() {
        return orderBook;
    }

}
