package agh.ics.oop.model.util;

public record Configuration(
        int width,
        int height,
        int startingPlantCount,
        int plantsPerDay,
        int energyFromEating,
        int startingAnimalCount,
        int startingEnergyAmount,
        int energyForReproduction,
        int reproductionCost,
        int minMutations,
        int maxMutations,
        int genomeLength,

        // Te 3 raczej nie będą int
        int mapVariant,
        int plantVariant,
        int animalVariant
) {}
