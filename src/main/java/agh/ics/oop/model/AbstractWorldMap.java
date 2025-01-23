package agh.ics.oop.model;

import agh.ics.oop.model.util.Boundary;

import java.util.HashMap;
import java.util.*;

public abstract class AbstractWorldMap implements WorldMap {
    protected final Boundary boundary;
    protected final Map<Vector2d, List<Animal>> animals = new HashMap<>();
    protected final Map<Vector2d, Plant> plants = new HashMap<>();
    protected final Set<Vector2d> preferredPlantSpots = new HashSet<>();
    protected final Set<Vector2d> neutralPlantSpots = new HashSet<>();
    protected final List<Animal> deadAnimals = new ArrayList<>();
    protected final GamePresenter presenter;


    public AbstractWorldMap(int width, int height, GamePresenter presenter) {
        this.boundary = new Boundary(new Vector2d(0, 0), new Vector2d(width -1, height -1));
        this.presenter = presenter;
    }

    public Boundary getBoundary() {
        return boundary;
    }

    public boolean hasPlant(Vector2d pos) {
        return plants.containsKey(pos);
    }

    public Animal animalAt(Vector2d pos) {
        return (!animals.containsKey(pos)) ? null : prioritizeAnimals(pos).getFirst();
    }

    protected Vector2d checkPlacementOnMap(Vector2d animalPosition) {
        if(animalPosition.getX() < this.boundary.start().getX()){
            animalPosition = new Vector2d(this.boundary.end().getX(), animalPosition.getY());
        }
        if(animalPosition.getY() < this.boundary.start().getY()){
            animalPosition = new Vector2d(animalPosition.getX(), this.boundary.end().getY());
        }
        if(animalPosition.getX() > this.boundary.end().getX()){
            animalPosition = new Vector2d(this.boundary.start().getX(), animalPosition.getY());
        }
        if(animalPosition.getY() > this.boundary.end().getY()){
            animalPosition = new Vector2d(animalPosition.getX(), this.boundary.start().getY());
        }
        return animalPosition;
    }

    public List<Animal> prioritizeAnimals(Vector2d location) {
        List<Animal> candidates = animals.get(location);

        candidates.sort(Comparator.comparing(Animal::getEnergy)
                .thenComparing(Animal::getAge)
                .thenComparing(Animal::getChildren)
                .thenComparing(Animal::getId)
                .reversed());
//        if (candidates.stream().distinct().count() == 1) {
//            Collections.shuffle(candidates);
//        }
        return candidates;
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
        deadAnimals.clear();
        mapChanged();
    }

    //Przy założeniu, że ruch pobiera 1 energii
    public void moveAnimals() {
        List<Animal> newPositions = new ArrayList<>();
        List<Vector2d> toRemoveFields = new ArrayList<>();
        for (List<Animal> oneField : animals.values()) {
            Vector2d position = oneField.get(0).getPosition();
            List<Animal> toRemoveAnimals = new ArrayList<>();
            for (Animal animal : oneField) {
                if (animal.getEnergy() > 0) {
                    animal.move();
                    animal.moveToSpecificPoint(checkPlacementOnMap(animal.getPosition()));
                    newPositions.add(animal);
                    toRemoveAnimals.add(animal);
                }
                else {
                    deadAnimals.add(animal);
                }
            }
            oneField.removeAll(toRemoveAnimals);
            if (oneField.isEmpty()) {
                toRemoveFields.add(position);
            }
        }
        toRemoveFields.forEach(animals.keySet()::remove);

        for (Animal animal : newPositions) {
            this.placeAnimal(animal);
        }
        mapChanged();
    }

    public void eatPlants(int energyFromEating) {
        for (Vector2d position : animals.keySet()) {
            if (plants.containsKey(position)) {
                Animal strongest = animals.get(position).size() == 1 ? animals.get(position).getFirst() : prioritizeAnimals(position).getFirst();
                strongest.eat(energyFromEating);
                removePlant(position);
            }
        }
        mapChanged();
    }

    public abstract void removePlant(Vector2d position);

    public void reproduceAnimals(int energyThreshold, int reproductionCost, int minMutations, int maxMutations) {
        for (Vector2d position : animals.keySet()) {
            if (animals.get(position).size() >= 2) {
                List<Animal> couple = animals.get(position).size() == 2 ? animals.get(position) : prioritizeAnimals(position).subList(0, 2);
                if(couple.get(0).canReproduce(energyThreshold) && couple.get(1).canReproduce(energyThreshold)) {
                    Animal child = couple.get(0).reproduce(couple.get(1), reproductionCost, minMutations, maxMutations);
                    this.placeAnimal(child);
                }
            }
        }
    }

    public abstract boolean isPreferred(Vector2d position);

    public abstract void growPlants(int amount);

    protected void mapChanged() {
        presenter.mapChanged();
    }
}
