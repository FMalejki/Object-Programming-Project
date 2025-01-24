package agh.ics.oop.model;

import java.util.List;

public class CreazyAnimal extends AbstractAnimal {

    public CreazyAnimal(Vector2d startPosition, int startEnergy, Genotype genotype) {
        super(startPosition, startEnergy, genotype);
    }

    @Override
    public void move() {
        int[] dx = {-1, 0, 1, 1, 1, 0, -1, -1};
        int[] dy = {-1, -1, -1, 0, 1, 1, 1, 0};
        int moveDirection = (super.direction + super.genotype.getActiveGene()) % 8;
        super.position = new Vector2d(position.getX() + dx[moveDirection], position.getY() + dy[moveDirection]);
        super.genotype.moveToNextGeneCreazy();
        statistics.incrementAge();
    }

    @Override
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
        partner.getStats().setEnergy(partner.getEnergy()-reproductionCost);
        this.statistics.setEnergy(this.statistics.getEnergy()-reproductionCost);

        Genotype childGenotype = new Genotype(childGenes);
        childGenotype.mutate(maxMutations, minMutations);

        Animal child = new CreazyAnimal(position, energyToChild, childGenotype);
        child.setParents(this, partner);

        statistics.incrementChildren();
        partner.getStats().incrementChildren();
        child.updateDescendants();
        return child;
    }
}
