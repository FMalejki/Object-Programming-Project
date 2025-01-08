package agh.ics.oop.model;

import agh.ics.oop.model.util.Boundary;

import java.util.HashMap;
import java.util.*;

public class WorldMap {
    private final Boundary boundary;
    private final Map<Vector2d, List<Animal>> animals = new HashMap<>();
    private final Map<Vector2d, Plant> plants = new HashMap<>();
    private final Set<Vector2d> preferredPlantSpots = new HashSet<>();
    private final Set<Vector2d> neutralPlantSpots = new HashSet<>();
    private final List<Animal> deadAnimals = new ArrayList<>();


    public WorldMap(int width, int height) {
        this.boundary = new Boundary(new Vector2d(0, 0), new Vector2d(width, height));
    }

    //TODO jeśli getChildren nie rozstrzygnie, trzeba brać losowe
    public List<Animal> prioritizeAnimals(Vector2d location) {
        List<Animal> candidates = animals.get(location);
        return candidates.sort(Comparator.comparing(Animal::getEnergy)
                                  .thenComparing(Animal::getAge)
                                  .thenComparing(Animal::getChildren)
                                  .reversed());
    }

    public void placeAnimal(Animal animal) {
        Vector2d position = animal.getPosition();
        if (!animals.containsKey(position)) {
            animals.put(position, new ArrayList<>());
        }
        animals.get(position).add(animal);
    }

    public void removeDeadAnimals() {
        for (Animal animal : deadAnimals) {
            Vector2d position = animal.getPosition();
            animals.get(position).remove(animal);
            if (animals.get(position).isEmpty()) {
                animals.remove(position);
            }
        }
    }

    //Przy założeniu, że ruch pobiera 1 energii
    public void moveAnimals() {
        List<Animal> newPositions = new ArrayList<>();
        for (List<Animal> oneField : animals.values()>) {
            for (Animal animal : oneField) {
                if (animal.getEnergy > 0) {
                    Vector2d position = animal.getPosition();
                    animal.move();
                    newPositions.add(animal);
                    oneField.remove(animal);
                    if (oneField.isEmpty()) {
                        animals.remove(position);
                    }
                }
                else {
                    deadAnimals.add(animal);
                }
            }
        }
        for (Animal animal : newPositions) {
            this.placeAnimal(animal);
        }
    }

    public void eatPlants(int energyFromEating) {
        for (Vector2d position : animals.keySet()) {
            if (plants.containsKey(position)) {
                Animal strongest = animals.get(position).size() == 1 ? animals.get(position).getFirst() : prioritizeAnimals(position).getFirst();
                strongest.eat(energyFromEating);
                removePlant(position);
            }
        }
    }

    //TODO
    public boolean isPreferred(Vector2d position) {
        return true;
    }

    public void removePlant(Vector2d position) {
        plants.remove(position);
        if (this.isPreferred(position)) {
            preferredPlantSpots.add(position);
        }
        else {
            neutralPlantSpots.add(position);
        }
    }

    public void reproduceAnimals(int energyForReproduction, int reproductionCost, int minMutations, int maxMutations) {
        for (Vector2d position : animals.keySet()) {
            if (animals.get(position).size() >= 2) {
                List<Animal> couple = animals.get(position).size() == 2 ? animals.get(position) : prioritizeAnimals(position).subList(0, 2);
                couple.get(0).reproduce(couple.get(1), energyForReproduction, reproductionCost, minMutations, maxMutations);
            }
        }
    }

    public void growPlants(int amount) {
        Random rand = new Random();

        for (int i = 0; i < amount; i++) {
            int chance = rand.nextInt(100);
            if ((preferredPlantSpots.isEmpty() || chance < 20) && !neutralPlantSpots.isEmpty()) {
                Vector2d position = neutralPlantSpots.stream().toList().get(rand.nextInt(neutralPlantSpots.size()));
                plants.put(position, new Plant(position));
                neutralPlantSpots.remove(position);
            }
            else if (!preferredPlantSpots.isEmpty()) {
                Vector2d position = preferredPlantSpots.stream().toList().get(rand.nextInt(preferredPlantSpots.size()));
                plants.put(position, new Plant(position));
                preferredPlantSpots.remove(position);
            }
        }
    }


}
