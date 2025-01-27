package agh.ics.oop.model;

import agh.ics.oop.model.util.Boundary;

import java.util.*;

public abstract class AbstractAnimal implements Animal {

    protected UUID id = UUID.randomUUID();
    protected int direction;
    protected Vector2d position;
    protected AnimalStats statistics;
    protected final Genotype genotype;
    protected final List<Animal> parents = new ArrayList<>();


    public AbstractAnimal(Vector2d startPosition, int startEnergy, Genotype genes){
        this.direction =  new Random().nextInt(8);
        this.position = startPosition;
        this.statistics = new AnimalStats(startEnergy);
        this.genotype = genes;
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
    public void updateDescendants(Set<Animal> ancestors) {
        for (Animal animal : parents) {
            ancestors.add(animal);
            animal.updateDescendants(ancestors);
        }
    }

    @Override
    public void move(Boundary boundary) {
        int[] dx = {-1, 0, 1, 1, 1, 0, -1, -1};
        int[] dy = {-1, -1, -1, 0, 1, 1, 1, 0};

        int gene = genotype.getActiveGene();
        int moveDirection = (direction + gene) % 8;

        position = new Vector2d(position.getX() + dx[moveDirection], position.getY() + dy[moveDirection]);
        if(position.getY() < boundary.start().getY() || position.getY() > boundary.end().getY()){
            moveDirection = (moveDirection + 4) % 8;
            position = position.add(new Vector2d(dx[moveDirection], dy[moveDirection]));
        }
        if(position.getX() < boundary.start().getX()){
            position = new Vector2d(boundary.end().getX(), position.getY());
        }
        if(position.getX() > boundary.end().getX()){
            position = new Vector2d(boundary.start().getX(), position.getY());
        }

        direction = moveDirection;
        this.statistics.setEnergy(statistics.getEnergy() - 1);
        statistics.incrementAge();
    }



    @Override
    public boolean canReproduce(int reproductionCost) {
        return statistics.getEnergy() >= reproductionCost;
    }

    @Override
    public List<Integer> splitChildGenes(Animal stronger, Animal weaker, double energyProportionStronger, double energyProportionWeaker) {

        Random random = new Random();
        int strongerSide = random.nextInt(2);
        int genotypeSize = stronger.getGenotype().size();
        int strongerSplice = (int)( Math.round(energyProportionStronger * genotypeSize) );
        List<Integer> childGenes = new ArrayList<>();

        if(strongerSide == 0){
            childGenes.addAll(stronger.getGenotype().subList(0, strongerSplice));
            childGenes.addAll(weaker.getGenotype().subList(strongerSplice, genotypeSize));
        }
        else{
            childGenes.addAll(weaker.getGenotype().subList(0, genotypeSize -strongerSplice));
            childGenes.addAll(stronger.getGenotype().subList(genotypeSize-strongerSplice, genotypeSize));
        }
        return childGenes;
    }


    @Override
    public Animal reproduce(Animal partner, int reproductionCost, int minMutations, int maxMutations) {
        double energyProportionThis = (double) this.getEnergy() / (partner.getEnergy() + this.getEnergy());
        double energyProportionOther = 1 - energyProportionThis;

        List<Integer> childGenes;
        if (energyProportionThis > energyProportionOther) {
            childGenes = splitChildGenes(this, partner, energyProportionThis, energyProportionOther);
        } else {
            childGenes = splitChildGenes(partner, this, energyProportionOther, energyProportionThis);
        }

        int energyToChild = reproductionCost * 2;
        partner.getStats().setEnergy(partner.getEnergy() - reproductionCost);
        this.statistics.setEnergy(this.statistics.getEnergy() - reproductionCost);

        Genotype childGenotype = new Genotype(childGenes);
        childGenotype.mutate(maxMutations, minMutations);

        Animal child = createChild(this.position, energyToChild, childGenotype, partner);

        this.statistics.incrementChildren();
        partner.getStats().incrementChildren();

        Set<Animal> ancestors = new HashSet<>();
        child.updateDescendants(ancestors);
        ancestors.forEach(Animal::incrementDescendants);

        return child;
    }

    protected abstract Animal createChild(Vector2d position, int energyToChild, Genotype genotype, Animal partner);

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Genotype:\n").append(genotype.getGenes().toString()).append("\n");
        sb.append("Active gene:\n").append(genotype.getActiveGene()).append("\n");
        sb.append("Energy:\n").append(statistics.getEnergy()).append("\n");
        sb.append("Eaten plants:\n").append(statistics.getEatenPlants()).append("\n");
        sb.append("Children:\n").append(statistics.getChildren()).append("\n");
        sb.append("Descendants:\n").append(statistics.getDescendants()).append("\n");
        sb.append("Age:\n").append(statistics.getAge()).append("\n");
        if (!isAlive()) {
            sb.append("Date of death:\n").append(statistics.getDateOfDeath()).append("\n");
        }
        return sb.toString();
    }
}
