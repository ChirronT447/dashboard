package com.gateway.dashboard.interviews.ctdl.otc;

enum Direction { BUY, SELL }
record Order(String isin, int quantity, Direction direction){}
