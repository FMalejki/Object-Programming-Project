package agh.ics.oop.model;

public class CreazyAnimal extends Animal {

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
}
