package com.gateway.dashboard;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Considerations:
 * Are values with zero-probability allowed? Are duplicate values allowed?
 * Probabilities of duplicate values could be combined if computing cumulative probabilities.
 * We might want to create a builder to handle the constructor argument checking in a nicer way.
 */
public class RandomGen {

    // Behold: The Java random number generator
    private final Random random = new Random();

    private int[] randomNums;
    private float[] probabilities;
    private final int lengthOfRandomNums;

    /**
     * Creates a class which will pick a number from the supplied randomNums array according to the supplied probabilities.
     * @param randomNums Values that will (pseudo-randomly) be returned by calls to nextNum()
     * @param probabilities The probability of the occurrence of each associated number in randomNums
     */
    public RandomGen(int[] randomNums, float[] probabilities){
        if(randomNums == null || probabilities == null || randomNums.length != probabilities.length || randomNums.length == 0) {
            throw new IllegalArgumentException("Constructor arguments are missing / incorrect");
        }

        this.lengthOfRandomNums = randomNums.length;
        this.randomNums = Arrays.copyOf(randomNums, lengthOfRandomNums);
        this.probabilities = Arrays.copyOf(probabilities, lengthOfRandomNums);
        transformProbabilities();

        if(this.probabilities[lengthOfRandomNums - 1] != 1f){
            throw new IllegalArgumentException("Probabilities must add up to 1.0");
        }
    }

    // Probabilities are transformed:
    //  {0.1f, 0.2f, 0.5f, 0.15f, 0.05f} => {0.1f, 0.3f, 0.8f, 0.95f, 1.0f};
    private void transformProbabilities() {
        float sum = probabilities[0];
        for(int i = 0; i < lengthOfRandomNums - 1; i++) {
            probabilities[i] = sum;
            sum += probabilities[i+1];
        }
        probabilities[lengthOfRandomNums-1] = sum;
    }

    /**
     * Returns one of the numbers in the array randomNums. When this method is called multiple times over a long period,
     *  it should return the numbers roughly in line with the initialised probabilities.
     *
     *  Probabilities are transformed:
     *  {0.1f, 0.2f, 0.5f, 0.15f, 0.05f} => {0.1f, 0.3f, 0.8f, 0.95f, 1.0f};
     *
     *  So we can do this:
     *  if random number <= transformedProbabilities[i] return randomNums[i];
     *
     * Eg:
     * randomNums =>        [-1,    0,    1,     2,     3  ]
     * probabilities =>     [0.1f, 0.2f, 0.5f, 0.15f, 0.05f]
     * Possible result =>   1
     *
     * @return
     */
    public int nextNum() {
        Float randomNumber = random.nextFloat();
        for(int i = 0; i < lengthOfRandomNums; i++) {
            if(randomNumber <= probabilities[i]) {
                return randomNums[i];
            }
        }
        throw new IllegalStateException("A call to RandomGen.nextNum() has failed. randomNumber: " + randomNumber);
    }

}

class TestClass {

    // Correct random numbers
    private int[] randomNums = {-2, -1, 0, 1, 2, 3, 4, 5, 6};

    // Correct Probabilities
    private float[] probabilities = {0, 0.1f, 0.2f, 0.50f, 0.15f, 0.05f, 0f, 0f, 0f};

    @Test
    public void testSingleCall() {
        RandomGen randomGen = new RandomGen(randomNums, probabilities);
        int result = randomGen.nextNum();
        List<Integer> list = Arrays.stream(randomNums).boxed().collect(Collectors.toList());
        Assert.isTrue(list.contains(result), "RandomGen.nextNum returned an unexpected result: " + result);
    }

    // Test method always returns number in randomNums array
    // Can't easily check probability distribution because we could theoretically get very different values than expected
    //  because random number generator
    @Test
    public void testRandomGenMultiCall() {
        Map<Integer, Integer> result;

        // Check we produced 7 numbers
        result = generateDistribution(7);
        int count = result.values().stream().mapToInt(Integer::intValue).sum();
        Assert.isTrue(count == 7, "Expected to have 7 elements");

        // Regenerate and check that the data set contains each number in the randomNums array
        result = generateDistribution(100);
        count = result.values().stream().mapToInt(Integer::intValue).sum();
        Assert.isTrue(count == 100, "Expected to have 100 elements");
    }

    // Check it handles null arrays.
    @Test
    public void testMisconfiguredGenerator_nullArrays(){
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new RandomGen(null, null);
        });
    }

    // Check it handles empty arrays.
    @Test
    public void testMisconfiguredGenerator_emptyArrays(){
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new RandomGen(new int[]{}, new float[]{});
        });

    }

    // Check it handles arrays of different lengths.
    @Test
    public void testMisconfiguredGenerator_incorrectRandomNumberLength(){
        int[] randomNumsWrongLength = {-1, 0, 1, 2};
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new RandomGen(randomNumsWrongLength, probabilities);
        });
    }

    // Check it handles probabilities that don't add up.
    @Test
    public void testMisconfiguredGenerator_incorrectProbabilities(){
        float[] probabilitiesDontAddUp = {0.1f, 0.2f, 0.30f, 0.15f, 0.05f, 0f, 0f, 0f};
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new RandomGen(randomNums, probabilitiesDontAddUp);
        });
    }

    /**
     * Helper function to generate a distribution.
     * @return A map of elements in randomNums to a count of the number of times they were returned by nextNum()
     */
    public Map<Integer, Integer> generateDistribution(int count) {
        final RandomGen randomGen = new RandomGen(randomNums, probabilities);
        final Map<Integer, Integer> result = new HashMap<>();
        for(int i = 0; i < count; i ++) {
            result.merge(randomGen.nextNum(), 1, Integer::sum);
        }
        return result;
    }

}


/*
-- SQL

create table product
(
    product_id number primary key,
    name varchar2(128 byte) not null,
    rrp number not null,
    available_from date not null
);

create table orders
(
    order_id number primary key,
    product_id number not null,
    quantity number not null,
    order_price number not null,
    dispatch_date date not null,
    foreign key (product_id) references product(product_id)
);

-- SQL query to find: books that have sold fewer than 10 copies in the last year,
--               excluding books that have been available for less than 1 month.

-- MySQL syntax

SELECT p.name FROM product p
LEFT JOIN orders o
    ON p.product_id = o.product_id
WHERE o.dispatch_date BETWEEN
    SUBDATE(curdate(), INTERVAL 1 MONTH) AND
    SUBDATE(curdate(), INTERVAL 12 MONTH)
GROUP BY p.id
HAVING SUM(o.quantity) < 10;

 */
