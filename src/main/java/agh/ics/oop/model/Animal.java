package agh.ics.oop.model;

import java.util.*;

public class Animal implements WorldElement {

    protected UUID id = UUID.randomUUID();
    protected int direction;
    protected Vector2d position;
    protected AnimalStats statistics;
    protected final Genotype genotype;


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

    public UUID getId(){return this.id;}

    public void eat(int plantEnergy){
        statistics.incrementEatenPlants();
        statistics.changeEnergy(plantEnergy);}


    public void move() {
        int[] dx = {-1, 0, 1, 1, 1, 0, -1, -1};
        int[] dy = {-1, -1, -1, 0, 1, 1, 1, 0};

        int moveDirection = (direction + genotype.getActiveGene()) % 8;
        this.position = new Vector2d(position.getX() + dx[moveDirection], position.getY() + dy[moveDirection]);
        this.statistics.setEnergy(statistics.getEnergy() - 1);

        genotype.moveToNextGene();
        statistics.incrementAge();
    }

    public void moveToSpecificPoint(Vector2d position) {
        this.position = new Vector2d(position.getX(), position.getY());
    }

    boolean canReproduce(int reproductionCost) {
        return statistics.getEnergy() >= reproductionCost;
    }

    private List<Integer> splitChildGenes(Animal stronger, Animal weaker, double energyProportionStronger, double energyProportionWeaker) {

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

    public Animal reproduce(Animal partner, int reproductionCost, int minMutations, int maxMutations) {
        double energyProportionThis = (double)this.getEnergy()/(partner.getEnergy()+this.getEnergy());
        double energyProporionOther = 1 - energyProportionThis;
        List<Integer> childGenes;

        if(energyProportionThis > energyProporionOther){
            childGenes = splitChildGenes(this, partner, energyProportionThis, energyProporionOther);
        }
        else{
            childGenes = splitChildGenes(partner, this, energyProporionOther, energyProportionThis);
        }

        int energyToChild = reproductionCost*2;
        partner.statistics.setEnergy(partner.getEnergy()-reproductionCost);
        this.statistics.setEnergy(this.statistics.getEnergy()-reproductionCost);

        Genotype childGenotype = new Genotype(childGenes);
        childGenotype.mutate(maxMutations, minMutations);

        Animal child = new Animal(position, energyToChild, childGenotype);

        statistics.incrementDescendants();
        partner.statistics.incrementDescendants();
        return child;
    }
}
