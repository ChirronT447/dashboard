package com.gateway.dashboard.hired;

import java.util.*;

class VendingItem {

    int column;
    int row;
    int costCents;
    int remainingQuantity;

    VendingItem(int[] arr) {
        this.column             = arr[0];
        this.row                = arr[1];
        this.costCents          = arr[2];
        this.remainingQuantity  = arr[3];
    }
}

class VendingMachine {

    /**
     * Given the list of inventory and purchase attempts; identify the total value of items remaining
     * in the machine after all successful purchase attempts.
     * Invalid items should be ignored
     * Purchase attempts for inventory out of stock should be ignored.
     * @param inventory
     * @param purchaseAttempts
     * @return
     */
    public static int solution(int[][] inventory, int[][] purchaseAttempts) {

        // Adding items to vending machine
        ArrayList<VendingItem> items = new ArrayList<>();
        for (int[] item : inventory) {
            try {
                items.add(new VendingItem(item));
            } catch (ArrayIndexOutOfBoundsException e) {
                // Ignoring invalid inventory
            }
        }

        int columnCount = items.stream()
                .mapToInt(item -> item.column)
                .reduce(0, Math::max) + 1;

        int rowCount = items.stream()
                .mapToInt(item -> item.row)
                .reduce(0, Math::max) + 1;

        VendingItem[][] grid = new VendingItem[columnCount][rowCount];
        for (VendingItem item : items) {
            grid[item.column][item.row] = item;
        }

        for (int[] purchaseCoordinate : purchaseAttempts) {
            if (purchaseCoordinate.length != 2) {
                continue;
            }

            int purchaseColumn = purchaseCoordinate[0];
            int purchaseRow = purchaseCoordinate[1];
            if (grid.length <= purchaseColumn) {
                continue;
            }

            VendingItem[] column = grid[purchaseColumn];
            if (column == null || column.length <= purchaseRow) {
                continue;
            }

            VendingItem item = column[purchaseRow];
            if(item == null) {
                continue;
            }

            item.remainingQuantity--;
        }

        int sum = 0;
        for (VendingItem item : items) {
            sum += item.remainingQuantity * item.costCents;
        }

        return sum;
    }
}


class Maths {

    /**
     * Given a set of unordered values; find the n-th largest multiple of k in the sequence
     * Ignore duplicates
     * Return -1 if no matches
     * @param values
     * @param n
     * @param k
     * @return
     */
    public static int findLargestMultiple(int[] values, int n, int k) {
        int[] filteredValues = Arrays.stream(values)
                .distinct()                 // Remove duplicates
                .filter(x -> x % k == 0)    // Multiple of k == no remainder
                .sorted()                   // Sort to get largest
                .toArray();

        // Check we're not asking for an unreasonable n:
        if(filteredValues.length >= n){
            return filteredValues[n-1];
        } else {
            return -1;
        }
    }
}
