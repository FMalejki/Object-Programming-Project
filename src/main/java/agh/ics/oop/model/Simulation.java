package agh.ics.oop.model;

import agh.ics.oop.model.util.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Simulation implements Runnable{

    private final WorldMap worldMap;
    private final Configuration config;
    //private final GameStats gameStats;

    public Simulation(Configuration config, WorldMap worldMap) {
        this.config = config;
        this.worldMap = worldMap;
    }

    private void initializeAnimals() {
        Random rand = new Random();
        int width = config.width();
        int height = config.height();

        for (int i = 0; i < config.startingAnimalCount(); i++) {
            Vector2d position = new Vector2d(rand.nextInt(width), rand.nextInt(height));
            Genotype genotype = new Genotype(config.genomeLength());
            Animal animal = new Animal(position, config.startingEnergyAmount(), genotype);
            worldMap.placeAnimal(animal);
        }


    }

    //TODO zmienić true na sensowny warunek w while
    @Override
    public void run() {
        initializeAnimals();
        worldMap.growPlants(config.startingPlantCount());
        while (true) {
            worldMap.removeDeadAnimals();
            worldMap.moveAnimals();
            worldMap.eatPlants(config.energyFromEating());
            worldMap.reproduceAnimals(config.energyForReproduction(), config.reproductionCost(), config.minMutations(), config.maxMutations());
            worldMap.growPlants(config.plantsPerDay());
        }
    }
}
