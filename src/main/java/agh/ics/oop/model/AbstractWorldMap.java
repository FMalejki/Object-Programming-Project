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
    protected int day = 0;
    protected int totalDeadAnimals = 0;
    protected int deadAnimalAgeSum = 0;


    public AbstractWorldMap(int width, int height, GamePresenter presenter) {
        this.boundary = new Boundary(new Vector2d(0, 0), new Vector2d(width -1, height -1));
        this.presenter = presenter;
    }

    public Boundary getBoundary() {
        return boundary;
    }

    public void incrementDay() {
        day++;
    }

    public int getDay() {
        return day;
    }

    public boolean hasPlant(Vector2d pos) {
        return plants.containsKey(pos);
    }

    public Set<Vector2d> getPreferredPlantSpots() {
        return preferredPlantSpots;
    }

    public Animal animalAt(Vector2d pos) {
        return (!animals.containsKey(pos) || animals.get(pos).isEmpty()) ? null : prioritizeAnimals(pos).getFirst();
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
        List<Animal> candidates = new ArrayList<>(List.copyOf(animals.get(location)));

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
            totalDeadAnimals++;
            deadAnimalAgeSum += animal.getAge();
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
            if (oneField.size() == 0) {System.out.println("xddd");}
            Vector2d position = oneField.get(0).getPosition();
            List<Animal> toRemoveAnimals = new ArrayList<>();
            for (Animal animal : oneField) {
                if (animal.getEnergy() > 0) {
                    animal.move();
                    animal.moveToSpecificPoint(checkPlacementOnMap(animal.getPosition()));

                }
                else {
                    animal.die(day);
                    deadAnimals.add(animal);
                }
                toRemoveAnimals.add(animal);
                newPositions.add(animal);
            }
            oneField.removeAll(toRemoveAnimals);
            if (oneField.isEmpty()) {
                toRemoveFields.add(position);
            }
        }
        toRemoveFields.forEach(animals.keySet()::remove);

        for (Animal animal : newPositions) {
            placeAnimal(animal);
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
        Set<Animal> children = new HashSet<>();
        for (Vector2d position : animals.keySet()) {
            if (animals.get(position).size() >= 2) {
                List<Animal> couple = animals.get(position).size() == 2 ? animals.get(position) : prioritizeAnimals(position).subList(0, 2);
                if(couple.get(0).canReproduce(energyThreshold) && couple.get(1).canReproduce(energyThreshold)) {
                    Animal child = couple.get(0).reproduce(couple.get(1), reproductionCost, minMutations, maxMutations);
                    children.add(child);
                }
            }
        }
        for (Animal child : children) {
            placeAnimal(child);
        }
    }

    public abstract boolean isPreferred(Vector2d position);

    public abstract void growPlants(int amount);

    protected void mapChanged() {
        presenter.mapChanged();
    }

    public int countAnimals() {
        int cnt = 0;
        for (Vector2d position : animals.keySet()) {
            for (Animal animal : animals.get(position)) {
                if (animal.isAlive()) {
                    cnt++;
                }
            }
        }
        return cnt;
    }

    public int countPlants() {
        return plants.size();
    }

    public int countFreeFields() {
        int cnt = 0;
        for (int x = 0; x < boundary.end().getX()+1; x++) {
            for (int y = 0; y < boundary.end().getY()+1; y++) {
                Vector2d position = new Vector2d(x, y);
                if (!(animals.containsKey(position) || plants.containsKey(position))) {
                    cnt++;
                }
            }
        }
        return cnt;
    }

    public List<List<Integer>> getPopularGenotypes() {
        HashMap<List<Integer>, Integer> genotypes = new HashMap<>();
        for (Vector2d position : animals.keySet()) {
            for (Animal animal : animals.get(position)) {
                List<Integer> genotype = animal.getGenotype();
                if (!genotypes.containsKey(genotype)) {
                    genotypes.put(genotype, 1);
                }
                else {
                    genotypes.put(genotype, genotypes.get(genotype) + 1);
                }
            }
        }
        List<List<Integer>> top = genotypes.keySet().stream().sorted(Comparator.comparing(genotypes::get).reversed()).toList();
        top = top.subList(0, Math.min(3, top.size()));
        return top;
    }

    public int totalEnergy() {
        int total = 0;
        for (Vector2d position : animals.keySet()) {
            for (Animal animal : animals.get(position)) {
                if (animal.isAlive()) {
                    total += animal.getEnergy();
                }
            }
        }
        return total;
    }

    public float avgLifespan() {
        return totalDeadAnimals != 0 ? (float) deadAnimalAgeSum / totalDeadAnimals : 0;
    }

    public int totalChildren() {
        int total = 0;
        for (Vector2d position : animals.keySet()) {
            for (Animal animal : animals.get(position)) {
                if (animal.isAlive()) {
                    total += animal.getChildren();
                }
            }
        }
        return total;
    }

    public void statsChanged(String stats) {
        presenter.statsChanged(stats);
    }

    public Set<Vector2d> dominatingGenotypePos() {
        List<Integer> dominating = getPopularGenotypes().get(0);
        Set<Vector2d> positions = new HashSet<>();
        for (Vector2d position : animals.keySet()) {
            for (Animal animal : animals.get(position)) {
                if (animal.isAlive() && animal.getGenotype().equals(dominating)) {
                    positions.add(position);
                }
            }
        }
        return positions;
    }

    public Set<Vector2d> preferredSpots() {
        return preferredPlantSpots;
    }
}
