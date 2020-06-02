package com.gateway.dashboard.exchange;

/**
 * Represents an order
 */
public class Order {

    Direction direction; // BUY or SELL
    Double price; // price for htis order
    Integer units; // units being offered

    Order(Direction direction, Double price, Integer units) {
        this.direction = direction;
        this.price = price;
        this.units = units;
    }


    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getUnits() {
        return units;
    }

    public void setUnits(Integer units) {
        this.units = units;
    }

    public static class OrderBuilder {

        Direction direction; // BUY or SELL
        Double price; // price for htis order
        Integer units; // units being offered

        public OrderBuilder setDirection(Direction direction) {
            this.direction = direction;
            return this;
        }

        public OrderBuilder setPrice(Double price) {
            this.price = price;
            return this;
        }

        public OrderBuilder setUnits(int units) {
            this.units = units;
            return this;
        }

        public Order build() {
            return new Order(direction, price, units);
        }

    }

}
