package agh.ics.oop.model;

import java.util.*;

public class Animal implements WorldElement {

    private int direction;
    private Vector2d position;
    private AnimalStats statistics;
    private final Genotype genotype;


    public Animal(Vector2d startPosition, int startEnergy, Genotype genotype){
        this.direction =  new Random().nextInt(8);
        this.position = startPosition;
        this.statistics = new AnimalStats(startEnergy);
        this.genotype = genotype;
    }

    public int getEnergy() {return statistics.getEnergy();}
    public int getChildren() {return statistics.getChildren();}
    public int getAge() {return statistics.getAge();}

    public List<Integer> getGenotype() {return genotype.getGenes();}


    @Override
    public Vector2d getPosition(){
        return this.position;
    }

    public Boolean isAt(Vector2d vector2d)
    {
        return this.position.equals(vector2d);
    }

    public void eat(int plantEnergy){
        statistics.incrementEatenPlants();
        statistics.changeEnergy(plantEnergy);}


    public void move() {
        int[] dx = {-1, 0, 1, 1, 1, 0, -1, -1};
        int[] dy = {-1, -1, -1, 0, 1, 1, 1, 0};

        int moveDirection = (direction + genotype.getActiveGene()) % 8;
        this.position = new Vector2d(position.getX() + dx[moveDirection], position.getY() + dy[moveDirection]);


        genotype.moveToNextGene();
        statistics.incrementAge();
    }

    public boolean canReproduce(int reproductionCost) {
        return statistics.getEnergy() >= reproductionCost;
    }

    public void reproduce(Animal partner, int energyThreshold, int reproductionCost, int minMutations, int maxMutations) {
        if (canReproduce(energyThreshold) && partner.canReproduce(energyThreshold)) {
            int energyToChild = statistics.getEnergy() / 4;
            statistics.setEnergy(statistics.getEnergy() - energyToChild);
            partner.statistics.setEnergy(partner.getEnergy() - energyToChild);

            int splitPoint = (int) ((statistics.getEnergy() / (double) (statistics.getEnergy() + partner.statistics.getEnergy())) * genotype.getGenes().size());
            List<Integer> childGenes = new ArrayList<>();
            childGenes.addAll(genotype.getGenes().subList(0, splitPoint));
            childGenes.addAll(partner.genotype.getGenes().subList(splitPoint, partner.genotype.getGenes().size()));

            Random random = new Random();
            int mutationsCount = random.nextInt(maxMutations) + minMutations;
            for (int i = 0; i < mutationsCount; i++) {
                int mutationIndex = random.nextInt(childGenes.size());
                childGenes.set(mutationIndex, random.nextInt(8));
            }

            Genotype childGenotype = new Genotype(childGenes);
            Animal child = new Animal(position, energyToChild, childGenotype);

            statistics.incrementDescendants();
            partner.statistics.incrementDescendants();
        }
    }
}
