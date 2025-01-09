package agh.ics.oop.model;

import agh.ics.oop.model.util.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Simulation implements Runnable{

    private final WorldMap worldMap;
    private final Configuration config;
    private final GameStats gameStats;

    public Simulation(Configuration config) {
        this.config = config;
        this.worldMap = (config.mapVariant() == 0) ? new BasicMap(config.width(), config.height()) :
                        new JungleMap(config.width(), config.height());
    }

    private void initializeAnimals() {
        Random rand = new Random();
        int width = config.width();
        int height = config.height();

        for (int i = 0; i < config.startingAnimalCount(); i++) {
            Vector2d position = new Vector2d(rand.nextInt(width), rand.nextInt(height));
            ArrayList<Integer> genome = new ArrayList<>();
            for (int j = 0; j < config.genomeLength(); j++) {
                genome.add(rand.nextInt(8));
            }
            Animal animal = new Animal(position, config.startingEnergyAmount(), genome);
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
