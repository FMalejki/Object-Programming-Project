package agh.ics.oop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BasicMapTest {

    private BasicMap basicMap;
    private GamePresenter gamePresenter= new GamePresenter();

    @BeforeEach
    void setUp() {
        basicMap = new BasicMap(10, 10);
        basicMap.setPresenter(gamePresenter);
    }

    @Test
    void testIsPreferredInJungle() {
        Vector2d junglePosition = new Vector2d(5, 4); // Example jungle position
        assertTrue(basicMap.isPreferred(junglePosition));
    }

    @Test
    void testIsPreferredOutsideJungle() {
        Vector2d nonJunglePosition = new Vector2d(5, 0); // Example outside jungle position
        assertFalse(basicMap.isPreferred(nonJunglePosition));
    }

    @Test
    void testRemovePlant() {
        Vector2d position = new Vector2d(3, 3);
        Plant plant = new Plant(position);
        basicMap.plants.put(position, plant);

        basicMap.removePlant(position);
        assertFalse(basicMap.plants.containsKey(position));
        assertTrue(basicMap.preferredPlantSpots.contains(position) || basicMap.neutralPlantSpots.contains(position));
    }

    @Test
    void testGrowPlantsPreferredSpot() {
        Vector2d preferredPosition = new Vector2d(5, 5);
        basicMap.neutralPlantSpots.clear();
        basicMap.preferredPlantSpots.clear();
        basicMap.preferredPlantSpots.add(preferredPosition);

        basicMap.growPlants(1);
        assertTrue(basicMap.plants.containsKey(preferredPosition));
        assertFalse(basicMap.preferredPlantSpots.contains(preferredPosition));
    }

    @Test
    void testGrowPlantsNeutralSpot() {
        Vector2d neutralPosition = new Vector2d(0, 0);
        basicMap.neutralPlantSpots.clear();
        basicMap.preferredPlantSpots.clear();
        basicMap.neutralPlantSpots.add(neutralPosition);

        basicMap.growPlants(1);
        assertTrue(basicMap.plants.containsKey(neutralPosition));
        assertFalse(basicMap.neutralPlantSpots.contains(neutralPosition));
    }

    @Test
    void testGrowPlantsNoSpots() {
        basicMap.preferredPlantSpots.clear();
        basicMap.neutralPlantSpots.clear();

        basicMap.growPlants(1);
        assertTrue(basicMap.plants.isEmpty());
    }

    @Test
    void testGrowPlantsMultiple() {
        Vector2d preferredPosition = new Vector2d(5, 5);
        Vector2d neutralPosition = new Vector2d(0, 0);
        basicMap.neutralPlantSpots.clear();
        basicMap.preferredPlantSpots.clear();
        basicMap.preferredPlantSpots.add(preferredPosition);
        basicMap.neutralPlantSpots.add(neutralPosition);

        basicMap.growPlants(2);

        assertEquals(2, basicMap.plants.size());
        assertFalse(basicMap.preferredPlantSpots.contains(preferredPosition));
        assertFalse(basicMap.neutralPlantSpots.contains(neutralPosition));
    }
}

