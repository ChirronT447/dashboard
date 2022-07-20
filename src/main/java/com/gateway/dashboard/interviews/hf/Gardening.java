package com.gateway.dashboard.interviews.hf;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This is a bin packing problem, we need to fill the space with all the objects which are a certain "size"
 * We need to order the flowers by size, bin packing problems are NP Complete so we'll need to pick some
 * heuristics; for ordering.
 */
public class Gardening {

    public static void main(String[] args) {

        // Step 1. Load the garden
        Garden garden = Garden.build(Helper.readFile(args[0]));
        garden.prettyPrint();

        // Step 2. Load the flowers
        List<Flower> flowers = Flower.build(Helper.readFile(args[1]));
        flowers.forEach(Flower::prettyPrint);

        // Step 3. Output a flower bed design for the garden subject to constraints.
        try {
            Garden gardenDesign = Gardening.designGarden(garden, flowers);
            gardenDesign.prettyPrint();
        } catch (Exception e) {
            // Catch any problem, reset the garden and try a different order - bit of a hack...
            garden = Garden.build(Helper.readFile(args[0]));
            List<Flower> newOrder = flowers.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
            Garden gardenDesign = Gardening.designGarden(garden, newOrder);
            gardenDesign.prettyPrint();
        }
    }

    /**
     * NB: Mutating garden in the method (not a good idea but quick and "easy")
     * @param garden
     * @param flowers
     * @return
     */
    public static Garden designGarden(Garden garden, List<Flower> flowers) {
        garden.analyseGarden();
        int numberOfFlowersToPlant = flowers.size();
        for(Flower flower : flowers) {
            int numberToPlant = flower.getNumberAvailableForPlanting();
            while(numberToPlant != 0) {
                boolean success = garden.plant(flower);
                if(success) {
                    numberToPlant--;
                } else {
                    System.err.println("Planting error, current garden: ");
                    garden.prettyPrint();
                    throw new RuntimeException(String.format(
                            "Unable to plant: %s, number remaining to plant: %s, number of flowers remaining including this: %s",
                                    flower.getType(), numberToPlant, numberOfFlowersToPlant
                            )
                    );
                }
            }
            numberOfFlowersToPlant--;
        }
        garden.prettyPrint();
        garden.cleanup();
        return garden;
    }

}
