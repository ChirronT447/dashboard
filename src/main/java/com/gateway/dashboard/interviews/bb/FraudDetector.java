package com.gateway.dashboard.interviews.bb;

import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/*
Given a list of transactions consisting of a name, amount, time, and location, determine which transactions are potentially fraudulent.
A transaction is potentially fraudulent, if either of the following conditions is true:

    1. The amount is greater than $1,000.
    2. The previous or next transaction with the same name occurs at a different location within an hour (<= 60).

The input will be a string.
Each transaction will be on a separate line and will have the format: <name>, <amount>, <time>, <location>.
Time is measured in minutes.
Transactions are sorted by increasing order by time.

Anne, 100, 1, Boston
Anne, 2000, 10, Boston
Bob, 50, 20, Boston
Cindy, 100, 50, New York
Bob, 50, 70, New York
*/

record Transaction(String name, int amount, int timeInMinutes, String location) {
    public Transaction {
        Objects.requireNonNull(name);
        Objects.requireNonNull(location);
    }
}

public class FraudDetector {

    // Rule 1) Flag transactions as potentially fraudulent if > than this amount.
    private static final int MAX_AMOUNT = 1000;
    // Time window for location change
    private static final int TIME_WINDOW = 60;

    public FraudDetector() {}

    public Set<Transaction> detectFraudulentTransactions(List<Transaction> transactions) {
        if(transactions == null || transactions.isEmpty()){
            return Set.of();
        }

        final Set<Transaction> fraudulentTransactions = new HashSet<>();

        // 1. Group transactions by name:
        final Map<String, List<Transaction>> transactionsByName = transactions.stream().collect(Collectors.groupingBy(Transaction::name));

        // 2. For each person: Sort by time just in case
        for(List<Transaction> userTransactions : transactionsByName.values()) {
            userTransactions.sort(Comparator.comparing(Transaction::timeInMinutes));

            // 3. Iterate through to check fraud conditions
            for(int i = 0; i < userTransactions.size(); i++) {
                final Transaction transaction = userTransactions.get(i);
                if(transaction.amount() > MAX_AMOUNT) {
                    fraudulentTransactions.add(transaction);
                }

                // Check the transaction before
                if(i > 0) {
                    Transaction prevTransaction = userTransactions.get(i - 1);
                    if(isNeighbourFraudulent(transaction, prevTransaction)) {
                        fraudulentTransactions.add(transaction);
                        continue;
                    }
                }

                // Check the transaction after:
                if(i < (userTransactions.size() - 1)) {
                    System.out.println("Index: " + i);
                    Transaction nextTransaction = userTransactions.get(i + 1);
                    if(isNeighbourFraudulent(transaction, nextTransaction)){
                        fraudulentTransactions.add(transaction);
                    }
                }
            }
        }
        return fraudulentTransactions;
    }

    // The previous or next transaction with the same name occurs at a different location within an hour (<= 60).
    private boolean isNeighbourFraudulent(Transaction current, Transaction neighbour) {
        final boolean differentLocation = !current.location().equalsIgnoreCase(neighbour.location());
        final boolean withinTimeWindow = Math.abs(current.timeInMinutes() - neighbour.timeInMinutes()) <= TIME_WINDOW;
        return differentLocation && withinTimeWindow;
    }

}

class Solution {

    public static void main(String[] args) {
        var t1 = new Transaction("Anne", 100, 1, "Boston");
        var t2 = new Transaction("Anne", 2000, 10, "Boston");
        var t3 = new Transaction("Bob", 50, 20, "Boston");
        var t4 = new Transaction("Cindy", 100, 50, "New York");
        var t5 = new Transaction("Bob", 50, 70, "New York");
        final List<Transaction> transactions = List.of(t1, t2, t3, t4, t5);

        final FraudDetector fraudDetector = new FraudDetector();
        final Set<Transaction> flaggedTransactions = fraudDetector.detectFraudulentTransactions(transactions);

        System.out.println(flaggedTransactions);
    }
}
