package com.gateway.dashboard.coursera.algorithms_divide_conquer.week1;

import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class AssignmentOneTest {

    public static final UnaryOperator<String> STRIP = String::strip;
    public static final UnaryOperator<String> UPPER = String::toUpperCase;

    @Test
    void mathTest() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        List<Integer> result = AssignmentOne.math(list, x -> x * 2, x -> x * 2);
        System.out.println(result); // [4, 6, 8, 10, 12, 14, 16, 18, 20, 40]
    }

    @Test
    void stringTest() {
        List<String> list = Arrays.asList(" hello  ", " hi   ", " i am ", "  whitespace  ", "  ", "  for", ".");
        List<String> result = AssignmentOne.math(list, STRIP, UPPER);
        System.out.println(result); // [4, 6, 8, 10, 12, 14, 16, 18, 20, 40]
    }

    /**
     * what's the product of the following two 64-digit numbers?
     *
     *  3141592653589793238462643383279502884197169399375105820974944592
     *
     *  2718281828459045235360287471352662497757247093699959574966967627
     */
    @Test
    void multiplicationTest() {
        BigInteger a = new BigInteger("3141592653589793238462643383279502884197169399375105820974944592");
        BigInteger b = new BigInteger("2718281828459045235360287471352662497757247093699959574966967627");
        BigInteger result = AssignmentOne.karatsuba(a, b);
        System.out.println("Result: " + result);
        Assert.isTrue(
            result.equals(
                new BigInteger(
                "8539734222673567065463550869546574495034888535765114961879601127067743044893204848617875072216249073013374895871952806582723184"
                )
            ),
            "Uh oh"
        );
    }

    @Test
    void testAsync() {
        StringBuilder result = new StringBuilder();

        List<String> messages = Arrays.asList("a", "b", "c");

        List<CompletableFuture<String>> futures = messages.stream()
                .map(msg -> CompletableFuture.completedFuture(msg).thenApply(s -> delayedUpperCase(s)))
                .collect(Collectors.toList());

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()])).whenComplete((v, th) -> {
            futures.forEach(cf -> assertTrue(isUpperCase(cf.getNow(null))));
            result.append("done");
        });

        assertTrue(result.length() > 0, "Result was empty");
    }

    private boolean isUpperCase(String now) {
        return now.equals(now.toUpperCase());
    }

    private String delayedUpperCase(String s) {
        return s.toUpperCase();
    }

    public int pipeline() {
        return 0;
    }

    Function<StressTestInput, StressTest> loadDataStage = dataIn -> {
        System.out.println(dataIn.inputData);
        StressTest stressTest = new StressTest();
        stressTest.result = 5;
        return stressTest;
    };

    Stage<Integer, String> source = Stage.of(Object::toString);
    Stage<Integer, Integer> toHex = source.pipe(it -> Integer.parseInt(it, 16));
    // toHex.execute(11/*0x11*/);// return 17;

    // DataLoadStage<StressTestInput, StressTest> dataLoadStage = new DataLoadStage();

}

class StressTest {
    public int result = 0;
}

class StressTestInput {
    public int inputData = 0;
}

class DataLoadStage<StressTestInput, StressTest> implements Stage {

    // 1. TXPS Connector
    // 2. Datalink Connector [Flash DatalinkService]
    // 3. DynamoDB Connector

    @Override
    public Object execute(Object value) {
        // CompletableFuture - execute all stages in parallel
        return null;
    }

    @Override
    public Stage pipe(Stage source) {
        return null;
    }
}

class CalculatorStage implements Stage {

    @Override
    public Object execute(Object value) {
        return null;
    }

    @Override
    public Stage pipe(Stage source) {
        return null;
    }
}


interface Stage<Input, Output> {

    Output execute(Input value);

    default <Result> Stage<Input, Result> pipe(Stage<Output, Result> source) {
        return value -> source.execute(execute(value));
    }

    static <Input, Output> Stage<Input, Output> of(Stage<Input, Output> source) {
        return source;
    }
}