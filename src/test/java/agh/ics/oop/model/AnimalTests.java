package agh.ics.oop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AnimalTest {

    private Animal animal;
    private Animal partner;
    private Vector2d startPosition;
    private Genotype genotype;

    @BeforeEach
    void setUp() {
        startPosition = new Vector2d(5, 5);
        List<Integer> genes = new ArrayList<>(List.of(0, 1, 2, 3, 4, 5, 6, 7));
        genotype = new Genotype(genes);
        animal = new Animal(startPosition, 100, genotype);
        partner = new Animal(startPosition, 100, genotype);
    }

    @Test
    void testGetEnergy() {
        assertEquals(100, animal.getEnergy());
    }

    @Test
    void testGetChildren() {
        assertEquals(0, animal.getChildren());
    }

    @Test
    void testGetAge() {
        assertEquals(0, animal.getAge());
    }

    @Test
    void testGetGenotype() {
        assertEquals(genotype.getGenes(), animal.getGenotype());
    }

    @Test
    void testGetPosition() {
        assertEquals(startPosition, animal.getPosition());
    }

    @Test
    void testIsAt() {
        assertTrue(animal.isAt(startPosition));
        assertFalse(animal.isAt(new Vector2d(0, 0)));
    }

    @Test
    void testEat() {
        animal.eat(50);
        assertEquals(150, animal.getEnergy());
    }

    @Test
    void testMove() {
        animal.move();
        Vector2d newPosition = animal.getPosition();
        assertNotEquals(startPosition, newPosition);
    }

    @Test
    void testCanReproduce() {
        assertTrue(animal.canReproduce(50));
        assertFalse(animal.canReproduce(150));
    }

    /*@Test
    void testReproduce() {
        animal.reproduce(partner, 50, 1, 3);
        assertEquals(75, animal.getEnergy());
        assertEquals(75, partner.getEnergy());
        assertEquals(1, animal.getChildren());
        assertEquals(1, partner.getChildren());
    }*/
}
