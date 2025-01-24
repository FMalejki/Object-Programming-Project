package agh.ics.oop.model;

import agh.ics.oop.model.util.Boundary;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BasicAnimal extends AbstractAnimal {

    public BasicAnimal(Vector2d startPosition, int startEnergy, Genotype genotype) {
        super(startPosition, startEnergy, genotype);
    }

    @Override
    public void move(Boundary boundary) {
        super.move(boundary);
        genotype.moveToNextGene();
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

        Animal child = new BasicAnimal(position, energyToChild, childGenotype);
        child.setParents(this, partner);

        statistics.incrementChildren();
        partner.getStats().incrementChildren();
        Set<Animal> ancestors = new HashSet<>();
        child.updateDescendants(ancestors);
        ancestors.forEach(Animal::incrementDescendants);
        return child;
    }
}
