package agh.ics.oop.model;

import java.util.List;

public class AnimalStats {
    private int energy;
    private int eatenPlants = 0;
    private int children = 0;
    private int descendants = 0;
    private int age = 0;
    private int dateOfDeath;
    private boolean alive = true;
    private List<Integer> genome;

    public AnimalStats(int energy, List<Integer> genome) {
        this.energy = energy;
        this.genome = genome;
    }

    public int getEnergy() {
        return energy;
    }
    public void changeEnergy(int energy) {
        this.energy += energy;
    }
    public int getEatenPlants() {
        return eatenPlants;
    }
    public void incrementEatenPlants() {
        this.eatenPlants += 1;
    }
    public int getChildren() {
        return children;
    }
    public void incrementChildren() {
        this.children += 1;
    }
    public int getDescendants() {
        return descendants;
    }
    public void incrementDescendants() {
        this.descendants += 1;
    }
    public int getAge() {
        return age;
    }
    public void incrementAge() {
        this.age += 1;
    }
    public boolean isAlive() {
        return alive;
    }
    public void kill(int date) {
        this.alive = false;
        this.dateOfDeath = date;
    }
    public List<Integer> getGenome() {
        return genome;
    }
    public void setGenome(List<Integer> genome) {
        this.genome = genome;
    }
}

