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
        String mapVariant,
        String animalVariant
) {}
