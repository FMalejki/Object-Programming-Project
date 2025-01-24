package agh.ics.oop.model;

import java.util.List;
import java.util.UUID;

public interface Animal extends WorldElement {

    int getEnergy();

    int getChildren();

    int getAge();

    void incrementDescendants();

    List<Integer> getGenotype();

    void die(int deathDate);

    boolean isAlive();

    AnimalStats getStats();

    @Override
    Vector2d getPosition();

    Boolean isAt(Vector2d vector2d);

    UUID getId();

    void eat(int plantEnergy);

    void setParents(Animal parent1, Animal parent2);

    void updateDescendants();

    void move();

    void moveToSpecificPoint(Vector2d position);

    boolean canReproduce(int reproductionCost);

    List<Integer> splitChildGenes(Animal stronger, Animal weaker, double energyProportionStronger, double energyProportionWeaker);

    Animal reproduce(Animal partner, int reproductionCost, int minMutations, int maxMutations);

    String toString();
}
