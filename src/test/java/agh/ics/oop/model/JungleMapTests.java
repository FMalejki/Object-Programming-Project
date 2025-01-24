package agh.ics.oop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JungleMapTest {

    private JungleMap jungleMap;
    private final int width = 10;
    private final int height = 10;
    private final GamePresenter observer = new GamePresenter();

    @BeforeEach
    void setUp() {
        jungleMap = new JungleMap(width, height);
        jungleMap.setPresenter(observer);
        observer.setWorldMap(jungleMap);
    }

    @Test
    void testConstructorInitializesNeutralSpots() {
        assertEquals(width * height, jungleMap.neutralPlantSpots.size());
    }

    @Test
    void testGetSurroundings() {
        Vector2d position = new Vector2d(5, 5);
        List<Vector2d> surroundings = jungleMap.getSurroundings(position);

        assertEquals(8, surroundings.size());
        assertTrue(surroundings.contains(new Vector2d(4, 4)));
        assertTrue(surroundings.contains(new Vector2d(6, 6)));
    }

    @Test
    void testGrowPlantsAddsPlants() {
        jungleMap.growPlants(5);
        assertEquals(5, jungleMap.plants.size());
    }

}

