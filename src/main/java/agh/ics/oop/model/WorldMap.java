package agh.ics.oop.model;

import agh.ics.oop.model.util.Boundary;

import java.util.List;
import java.util.Set;

public interface WorldMap {
    public List<Animal> prioritizeAnimals(Vector2d location);
    public void placeAnimal(Animal animal);
    public void removeDeadAnimals();
    public void moveAnimals();
    public void eatPlants(int energyFromEating);
    public void reproduceAnimals(int energyThreshold, int reproductionCost, int minMutations, int maxMutations);
    public abstract void growPlants(int amount);
    public Boundary getBoundary();
    public boolean hasPlant(Vector2d pos);
    public Animal animalAt(Vector2d pos);
    public int countAnimals();
    public int countPlants();
    public int countFreeFields();
    public List<List<Integer>> getPopularGenotypes();
    public int totalEnergy();
    public float avgLifespan();
    public int totalChildren();
    public void incrementDay();
    public void statsChanged(String stats);
    public Set<Vector2d> dominatingGenotypePos();
    public Set<Vector2d> getPreferredPlantSpots();
}
