package agh.ics.oop.model;

import agh.ics.oop.model.util.Boundary;
import agh.ics.oop.model.util.Vector2d;
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
        animal = new BasicAnimal(startPosition, 100, genotype);
        partner = new BasicAnimal(startPosition, 100, genotype);
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
        assertEquals(startPosition, animal.position());
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
        animal.move(new Boundary(new Vector2d(0, 0), new Vector2d(10, 10)));
        Vector2d newPosition = animal.position();
        assertNotEquals(startPosition, newPosition);
    }

    @Test
    void testReproduce() {
        animal.reproduce(partner, 50, 1, 3);
        assertEquals(50, animal.getEnergy());
        assertEquals(50, partner.getEnergy());
        assertEquals(1, animal.getChildren());
        assertEquals(1, partner.getChildren());
    }
}
