package com.gateway.dashboard.datastructures;

import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import static org.junit.jupiter.api.Assertions.*;

class DequeImplTest {

    @Test
    public void testDeque() {
        DequeImpl.Deque deque = DequeImpl.Deque.getInstance();
        deque.addFirst("test1");
        deque.addFirst("test2");
        deque.addFirst("test3");
        deque.addFirst("test4");
        deque.addLast("test1");
        Assert.isTrue(deque.getSize() == 5, "Should be size = 5");
        deque.removeFirst();
        Assert.isTrue(deque.getSize() == 4, "Should be size = 4");
        Assert.isTrue(!deque.isEmpty(), "Should not be empty");
    }

}