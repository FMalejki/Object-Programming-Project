package agh.ics.oop.model;

import java.util.Random;
public class Animal implements WorldElement {

    private MapDirection direction;
    private Vector2d position;
    private AnimalStats statistics;
    private int activeGene = 0;
    private final Genotype genotype;


    public Animal(Vector2d startPosition, int startEnergy){
        this.direction = MapDirection.randomDirection();
        this.position = startPosition;
        this.statistics = new AnimalStats(startEnergy);
        this.genotype = new Genotype();
    }

    public Animal(Vector2d startPosition, int startEnergy, Genotype fatherGenotype, Genotype motherGenotype){
        this.direction = MapDirection.randomDirection();
        this.position = startPosition;
        this.statistics = new AnimalStats(startEnergy);
        this.genotype = new Genotype(fatherGenotype, motherGenotype);
    }

    public int getEnergy( ) {return statistics.getEnergy();}
    public int getChildren( ) {return statistics.getChildren();}
    public int getAge( ) {return statistics.getAge();}

    public Genotype getGenotype() {return statistics.getGenotype();}


    @Override
    public Vector2d getPosition(){
        return this.position;
    }

    @Override
    public String toString(){
        return this.direction.toString();
    }

    public Boolean isAt(Vector2d vector2d)
    {
        return this.position.equals(vector2d);
    }

    public void eat(int plantEnergy){
        statistics.incrementEatenPlants();
        statistics.changeEnergy(plantEnergy);}


    public void move(MoveDirection otherDirection, WorldMap map){
        Vector2d newPosition = this.position;

        switch(otherDirection){
            case RIGHT:
                this.direction = this.direction.next();
                return;
            case LEFT:
                this.direction = this.direction.previous();
                return;
            case FORWARD:
                newPosition = this.position.add(this.direction.toUnitVector());
                break;
            case BACKWARD:
                newPosition = this.position.add(this.direction.toUnitVector().opposite());
                break;
            default:
                return;

            if (map.canMoveTo(newPosition)) {
                this.position = newPosition;
            }
        }
    }

}
