package agh.ics.oop.model;

import agh.ics.oop.model.util.Configuration;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class Simulation implements Runnable{

    private final WorldMap worldMap;
    private final Configuration config;
    private final GameStats gameStats;
    private boolean running = false;
    private boolean exportCSV = false;
    private File file = null;


    public Simulation(Configuration config, WorldMap worldMap) {
        this.config = config;
        this.worldMap = worldMap;
        initializeAnimals();
        gameStats = calculateGameStats();
        worldMap.growPlants(config.startingPlantCount());
    }

    public void setExportCSV(boolean exportCSV) throws IOException {
        this.exportCSV = exportCSV;
        LocalDateTime now = LocalDateTime.now();
        file = new File("src/main/resources//Saves/" + now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss")) + ".csv");
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write("Day;allAnimals;allPlants;freeFields;avgEnergy;avgLifespan;avgChildren;genotype1;genotype2;genotype3\n");
        writer.close();
        exportDailyStats();
    }

    private void initializeAnimals() {
        Random rand = new Random();
        int width = config.width();
        int height = config.height();

        for (int i = 0; i < config.startingAnimalCount(); i++) {
            Vector2d position = new Vector2d(rand.nextInt(width), rand.nextInt(height));
            Genotype genotype = new Genotype(config.genomeLength());
            Animal animal = null;
            if (config.animalVariant().equals("BasicAnimal")) {
                animal = new BasicAnimal(position, config.startingEnergyAmount(), genotype);
            }
            if (config.animalVariant().equals("CrazyAnimal")) {
                animal = new CreazyAnimal(position, config.startingEnergyAmount(), genotype);
            }
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
            if (exportCSV) {
                try {
                    exportDailyStats();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            try {
                Thread.sleep(500);
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

    private void exportDailyStats() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
        int genotypeCount = gameStats.getGenotypes().size();
        String gen1 = genotypeCount > 0 ? gameStats.getGenotypes().get(0).toString() : "None";
        String gen2 = genotypeCount > 1 ? gameStats.getGenotypes().get(1).toString() : "None";
        String gen3 = genotypeCount > 2 ? gameStats.getGenotypes().get(2).toString() : "None";
        writer.write(worldMap.getDay() + ";"
                        + gameStats.getAllAnimals() + ";"
                        + gameStats.getAllPlants() + ";"
                        + gameStats.getFreeFields() + ";"
                        + gameStats.getAvgEnergy() + ";"
                        + gameStats.getAvgLifespan() + ";"
                        + gameStats.getAvgChildren() + ";"
                        + gen1 + ";" + gen2 + ";" + gen3 + "\n");
        writer.close();
    }
}
