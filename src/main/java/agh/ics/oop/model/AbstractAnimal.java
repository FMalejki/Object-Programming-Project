package agh.ics.oop.model;

import java.util.*;

public abstract class AbstractAnimal implements Animal {

    protected UUID id = UUID.randomUUID();
    protected int direction;
    protected Vector2d position;
    protected AnimalStats statistics;
    protected final Genotype genotype;
    protected final List<Animal> parents = new ArrayList<>();


    public AbstractAnimal(Vector2d startPosition, int startEnergy, Genotype genotype){
        this.direction =  new Random().nextInt(8);
        this.position = startPosition;
        this.statistics = new AnimalStats(startEnergy);
        this.genotype = genotype;
    }

    @Override
    public int getEnergy() {return statistics.getEnergy();}
    @Override
    public int getChildren() {return statistics.getChildren();}
    @Override
    public int getAge() {return statistics.getAge();}
    @Override
    public void incrementDescendants() {
        statistics.incrementDescendants();
    }
    public AnimalStats getStatistics() {return statistics;}
    @Override
    public List<Integer> getGenotype() {return genotype.getGenes();}
    @Override
    public void die(int deathDate) {
        statistics.kill(deathDate);
    }
    @Override
    public boolean isAlive() {return statistics.isAlive();}
    @Override
    public AnimalStats getStats() {return statistics;}

    @Override
    public Vector2d getPosition(){
        return this.position;
    }

    @Override
    public Boolean isAt(Vector2d vector2d)
    {
        return this.position.equals(vector2d);
    }

    @Override
    public UUID getId(){return this.id;}

    @Override
    public void eat(int plantEnergy){
        statistics.incrementEatenPlants();
        statistics.changeEnergy(plantEnergy);}

    @Override
    public void setParents(Animal parent1, Animal parent2){
        parents.add(parent1);
        parents.add(parent2);
    }
    @Override
    public void updateDescendants() {
        for (Animal animal : parents) {
            animal.incrementDescendants();
            animal.updateDescendants();
        }
    }

    @Override
    public void move() {
        int[] dx = {-1, 0, 1, 1, 1, 0, -1, -1};
        int[] dy = {-1, -1, -1, 0, 1, 1, 1, 0};

        int moveDirection = (direction + genotype.getActiveGene()) % 8;
        this.position = new Vector2d(position.getX() + dx[moveDirection], position.getY() + dy[moveDirection]);
        this.statistics.setEnergy(statistics.getEnergy() - 1);

        genotype.moveToNextGene();
        statistics.incrementAge();
    }

    @Override
    public void moveToSpecificPoint(Vector2d position) {
        this.position = new Vector2d(position.getX(), position.getY());
    }

    @Override
    public boolean canReproduce(int reproductionCost) {
        return statistics.getEnergy() >= reproductionCost;
    }

    @Override
    public List<Integer> splitChildGenes(Animal stronger, Animal weaker, double energyProportionStronger, double energyProportionWeaker) {

        Random random = new Random();
        int strongerSide = random.nextInt(2);
        //0 - left
        int genotypeSize = stronger.getGenotype().size();
        int strongerSplice = (int)( Math.round(energyProportionStronger * genotypeSize) );
        int weakerSplice = genotypeSize - strongerSplice;
        List<Integer> childGenes = new ArrayList<>();

        if(strongerSide == 0){
            childGenes.addAll(stronger.getGenotype().subList(0, strongerSplice));
            childGenes.addAll(weaker.getGenotype().subList(strongerSplice, genotypeSize));
//            for(int i = 0; i < strongerSplice; i++){
//                childGenes.add(stronger.getGenotype().get(i));
//            }
//            for(int i = stronger.getGenotype().size()-1; i > weakerSplice; i--){
//                childGenes.add(weaker.getGenotype().get(i));
//            }
        }
        else{
            childGenes.addAll(weaker.getGenotype().subList(0, genotypeSize -strongerSplice));
            childGenes.addAll(stronger.getGenotype().subList(genotypeSize-strongerSplice, genotypeSize));
//            for(int i = stronger.getGenotype().size()-1; i > strongerSplice; i--){
//                childGenes.add(stronger.getGenotype().get(i));
//            }
//            for(int i = 0; i < weakerSplice; i++){
//                childGenes.add(weaker.getGenotype().get(i));
//            }
        }
        return childGenes;
    }


    public abstract Animal reproduce(Animal partner, int reproductionCost, int minMutations, int maxMutations);

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Genotype:\n"+genotype.getGenes().toString()+"\n");
        sb.append("Active gene:\n"+genotype.getActiveGene()+"\n");
        sb.append("Energy:\n"+statistics.getEnergy()+"\n");
        sb.append("Eaten plants:\n"+statistics.getEatenPlants()+"\n");
        sb.append("Children:\n"+statistics.getChildren()+"\n");
        sb.append("Descendants:\n"+statistics.getDescendants()+"\n");
        sb.append("Age:\n"+statistics.getAge()+"\n");
        if (!isAlive()) {
            sb.append("Date of death:\n" + statistics.getDateOfDeath() + "\n");
        }
        return sb.toString();
    }
}
