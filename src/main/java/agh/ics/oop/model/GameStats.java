package agh.ics.oop.model;

import java.util.List;

public class GameStats {
    private int allAnimals;
    private int allPlants;
    private int freeFields;
    private float avgEnergy;
    private float avgLifespan;
    private float avgChildren;
    private List<List<Integer>> genotypes;

    public GameStats(int allAnimals, int allPlants, int freeFields, int totalEnergy, float avgLifespan, int totalChildren, List<List<Integer>> genotypes) {
        this.allAnimals = allAnimals;
        this.allPlants = allPlants;
        this.freeFields = freeFields;
        this.avgEnergy = (float) totalEnergy / allAnimals;
        this.avgLifespan = avgLifespan;
        this.avgChildren= (float) totalChildren / allAnimals;
        this.genotypes = genotypes;
    }
    public int getAllAnimals() {
        return allAnimals;
    }
    public int getAllPlants() {
        return allPlants;
    }
    public int getFreeFields() {
        return freeFields;
    }
    public double getAvgEnergy() {
        return avgEnergy;
    }
    public double getAvgLifespan() {
        return avgLifespan;
    }
    public double getAvgChildren() {
        return avgChildren;
    }
    public List<List<Integer>> getGenotypes() {
        return genotypes;
    }

    public void updateAllAnimals(int allAnimals) {
        this.allAnimals = allAnimals;
    }
    public void updateAllPlants(int allPlants) {
        this.allPlants = allPlants;
    }
    public void updateFreeFields(int freeFields) {
        this.freeFields = freeFields;
    }
    public void updateAvgEnergy(int totalEnergy) {
        this.avgEnergy = (float) totalEnergy / allAnimals;
    }
    public void updateAvgLifespan(float avgLifespan) {
        this.avgLifespan = avgLifespan;
    }
    public void updateAvgChildren(int totalChildren) {
        this.avgChildren = (float) totalChildren / allAnimals;
    }
    public void updateGenotypes(List<List<Integer>> genotypes) {
        this.genotypes = genotypes;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("All Animals:\n").append(allAnimals).append("\n");
        sb.append("All Plants:\n").append(allPlants).append("\n");
        sb.append("Free Fields:\n").append(freeFields).append("\n");
        sb.append("Avg Energy:\n").append(avgEnergy).append("\n");
        sb.append("Avg Lifespan:\n").append(avgLifespan).append("\n");
        sb.append("Avg Children:\n").append(avgChildren).append("\n");
        sb.append("Genotypes:\n");
        for (List<Integer> genotype : genotypes) {
            sb.append(genotype.toString()).append("\n");
        }
        return sb.toString();
    }
}
