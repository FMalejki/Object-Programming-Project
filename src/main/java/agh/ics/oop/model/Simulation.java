package agh.ics.oop.model;

import agh.ics.oop.model.util.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Simulation implements Runnable{

    private final WorldMap worldMap;
    private final Configuration config;
    private final GameStats gameStats;
    private boolean running = false;

    public Simulation(Configuration config, WorldMap worldMap) {
        this.config = config;
        this.worldMap = worldMap;
        initializeAnimals();
        gameStats = calculateGameStats();
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

    public void switchPause() {
        running = !running;
    }

    public void close(){
        running = false;
    }

    private void removeDead() {
        worldMap.removeDeadAnimals();
        gameStats.updateAvgLifespan(worldMap.avgLifespan());
        gameStats.updateFreeFields(worldMap.countFreeFields());
        worldMap.statsChanged(gameStats.toString());
    }
    private void move() {
        worldMap.moveAnimals();
        gameStats.updateFreeFields(worldMap.countFreeFields());
        gameStats.updateAvgEnergy(worldMap.totalEnergy());
        worldMap.statsChanged(gameStats.toString());
    }
    private void eat() {
        worldMap.eatPlants(config.energyFromEating());
        gameStats.updateAvgEnergy(worldMap.totalEnergy());
        gameStats.updateAllPlants(worldMap.countPlants());
        worldMap.statsChanged(gameStats.toString());
    }
    private void reproduce() {
        worldMap.reproduceAnimals(config.energyForReproduction(), config.reproductionCost(), config.minMutations(), config.maxMutations());
        gameStats.updateAllAnimals(worldMap.countAnimals());
        gameStats.updateAvgChildren(worldMap.totalChildren());
        gameStats.updateGenotypes(worldMap.getPopularGenotypes());
        worldMap.statsChanged(gameStats.toString());
    }
    private void grow() {
        worldMap.growPlants(config.plantsPerDay());
        gameStats.updateAllPlants(worldMap.countPlants());
        gameStats.updateFreeFields(worldMap.countFreeFields());
        worldMap.statsChanged(gameStats.toString());
    }

    @Override
    public void run() {
        while (running) {
            removeDead();
            move();
            eat();
            reproduce();
            grow();
            worldMap.incrementDay();
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private GameStats calculateGameStats() {
        return new GameStats(worldMap.countAnimals(),
                             worldMap.countPlants(),
                             worldMap.countFreeFields(),
                             worldMap.totalEnergy(),
                             worldMap.avgLifespan(),
                             worldMap.totalChildren(),
                             worldMap.getPopularGenotypes());
    }
}
