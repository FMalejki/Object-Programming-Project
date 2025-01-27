package agh.ics.oop.model;

import agh.ics.oop.model.util.Boundary;
import agh.ics.oop.model.util.Vector2d;

import java.util.List;
import java.util.Set;

public interface WorldMap {
    List<Animal> prioritizeAnimals(Vector2d location);
    void placeAnimal(Animal animal);
    void removeDeadAnimals();
    void moveAnimals();
    void eatPlants(int energyFromEating);
    void reproduceAnimals(int energyThreshold, int reproductionCost, int minMutations, int maxMutations);
    void growPlants(int amount);
    Boundary getBoundary();
    boolean hasPlant(Vector2d pos);
    Animal animalAt(Vector2d pos);
    int countAnimals();
    int countPlants();
    int countFreeFields();
    List<List<Integer>> getPopularGenotypes();
    int totalEnergy();
    float avgLifespan();
    int totalChildren();
    void incrementDay();
    void statsChanged(String stats);
    Set<Vector2d> dominatingGenotypePos();
    Set<Vector2d> getPreferredPlantSpots();
    int getDay();
    void setPresenter(GamePresenter presenter);
    void mapChanged();
}
