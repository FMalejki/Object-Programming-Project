package agh.ics.oop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GenotypeTest {

    private Genotype genotype;
    private List<Integer> initialGenes;

    @BeforeEach
    void setUp() {
        initialGenes = List.of(0, 1, 2, 3, 4, 5, 6, 7);
        genotype = new Genotype(initialGenes);
    }

    @Test
    void testConstructorWithGenes() {
        assertEquals(initialGenes, genotype.getGenes());
        assertNotNull(genotype.getActiveGene());
    }

    @Test
    void testConstructorWithNumberOfGenes() {
        int numberOfGenes = 10;
        Genotype randomGenotype = new Genotype(numberOfGenes);

        assertEquals(numberOfGenes, randomGenotype.getGenes().size());
        assertTrue(randomGenotype.getGenes().stream().allMatch(gene -> gene >= 0 && gene < 8));
    }

    @Test
    void testGetActiveGene() {
        int activeGene = genotype.getActiveGene();
        assertTrue(activeGene >= 0 && activeGene < 8);
    }

    @Test
    void testMoveToNextGene() {
        int previousActiveGene = genotype.getActiveGene();
        genotype.moveToNextGene();

        int newActiveGene = genotype.getActiveGene();
        assertTrue(newActiveGene >= 0 && newActiveGene < 8);
        assertNotEquals(previousActiveGene, newActiveGene); // Most of the time, they will differ.
    }

    @Test
    void testGenerateGenes() {
        int numberOfGenes = 5;
        ArrayList<Integer> generatedGenes = genotype.generateGenes(numberOfGenes);

        assertEquals(numberOfGenes, generatedGenes.size());
        assertTrue(generatedGenes.stream().allMatch(gene -> gene >= 0 && gene < 8));
    }
}

