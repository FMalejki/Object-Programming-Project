package agh.ics.oop.model;

public class AnimalStats {
    private int energy;
    private int eatenPlants = 0;
    private int children = 0;
    private int descendants = 0;
    private int age = 0;
    private int dateOfDeath;
    private boolean alive = true;

    public AnimalStats(int energy) {
        this.energy = energy;
    }

    public int getEnergy() {
        return energy;
    }
    public void setEnergy(int energy) {
        this.energy = energy;
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
    public boolean isAlive() {return alive;}
    public int getDateOfDeath() {return dateOfDeath;}
    public void kill(int date) {
        this.alive = false;
        this.dateOfDeath = date;
    }
}

