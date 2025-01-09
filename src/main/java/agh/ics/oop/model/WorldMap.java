package agh.ics.oop.model;

import agh.ics.oop.model.util.Boundary;

import java.util.List;

public interface WorldMap {
    public List<Animal> prioritizeAnimals(Vector2d location);
    public void placeAnimal(Animal animal);
    public void removeDeadAnimals();
    public void moveAnimals();
    public void eatPlants(int energyFromEating);
    public void reproduceAnimals(int energyForReproduction, int reproductionCost, int minMutations, int maxMutations);
    public abstract void growPlants(int amount);
    public Boundary getBoundary();

}
