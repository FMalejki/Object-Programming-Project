package agh.ics.oop.model;

import agh.ics.oop.model.util.Boundary;
import agh.ics.oop.model.util.Vector2d;

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
    protected Animal createChild(Vector2d position, int energyToChild, Genotype genotype, Animal partner) {
        return new CreazyAnimal(position, energyToChild, genotype);
    }
}
