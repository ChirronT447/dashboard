package com.gateway.dashboard.interviews.anon;

import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import java.util.List;

class GardeningTest {

    /**
     *         // Step 1. Load the garden
     *         Garden garden = Garden.load(Helper.readFile(args[0]));
     *         garden.prettyPrint();
     *
     *         // Step 2. Load flowers
     *         List<Flower> flowers = Flower.load(Helper.readFile(args[1]));
     *
     *         // Step 3. Output a flower bed design for the garden subject to constraints.
     *         Garden gardenDesign = Gardening.designGarden(garden, flowers);
     *         gardenDesign.prettyPrint();
     */
    @Test
    public void testGardenLoading() {
        List<String> gardenPaths = Helper.fetchFilePaths("src/test/resources/gardening/garden");
        for(String gardenStr : gardenPaths) {
            Garden garden = Garden.build(gardenStr);
            Assert.notNull(garden, "Garden failed to load");
        }
    }

    @Test
    public void testFlowerLoading() {
        List<String> flowerPaths = Helper.fetchFilePaths("src/test/resources/gardening/flowers");
        for(String flowerStr : flowerPaths) {
            List<Flower> flowers = Flower.build(flowerStr);
            Assert.notNull(flowers, "Flowers failed to load");
            Assert.notEmpty(flowers, "Flowers failed to load");
        }
    }

    @Test
    public void testGardenDesign1() {
        Garden gardenTemplate = Garden.build("src/test/resources/gardening/garden/garden1.txt");
        List<Flower> flowers = Flower.build("src/test/resources/gardening/flowers/flowers1.txt");
        Garden garden = Gardening.designGarden(gardenTemplate, flowers);
        garden.prettyPrint();
    }

    @Test
    public void testGardenDesign2() {
        Garden gardenTemplate = Garden.build("src/test/resources/gardening/garden/garden2.txt");
        List<Flower> flowers = Flower.build("src/test/resources/gardening/flowers/flowers2.txt");
        Garden garden = Gardening.designGarden(gardenTemplate, flowers);
        garden.prettyPrint();
    }

    @Test
    public void testGardenDesign3() {
        Garden gardenTemplate = Garden.build("src/test/resources/gardening/garden/garden3.txt");
        List<Flower> flowers = Flower.build("src/test/resources/gardening/flowers/flowers3.txt");
        Garden garden = Gardening.designGarden(gardenTemplate, flowers);
        garden.prettyPrint();
    }

    @Test
    public void testGardenDesign4() {
        Garden gardenTemplate = Garden.build("src/test/resources/gardening/garden/garden4.txt");
        List<Flower> flowers = Flower.build("src/test/resources/gardening/flowers/flowers4.txt");
        Garden garden = Gardening.designGarden(gardenTemplate, flowers);
        garden.prettyPrint();
    }

}