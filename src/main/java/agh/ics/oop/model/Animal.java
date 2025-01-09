package agh.ics.oop.model;

import java.util.List;

public class Animal implements WorldElement {

    private MapDirection direction;
    private Vector2d position;
    private AnimalStats stats;
    private int activeGene = 0;

    public Animal(){
        this.direction = MapDirection.NORTH;
        this.position = new Vector2d(2,2);
    }

    public Animal(Vector2d vector2d){
        this.position = vector2d;
        this.direction = MapDirection.NORTH;
    }

    public Animal(Vector2d position, int energy, List<Integer> genome){
        this.position = position;
        this.direction = MapDirection.randomDirection();

    }

    public int getEnergy() {
        return stats.getEnergy();
    }

    public int getChildren() {
        return stats.getChildren();
    }

    public int getAge() {
        return stats.getAge();
    }

    public List<Integer> getGenome() {
        return stats.getGenome();
    }

    private void setDirection(MapDirection direction) {
        this.direction = direction;
    }

    @Override
    public Vector2d getPosition(){
        return this.position;
    }

    public MapDirection getDirection(){
        return this.direction;
    }

    @Override
    public String toString(){
        return this.direction.toString();
    }

    public Boolean isAt(Vector2d vector2d)
    {
        return this.position.equals(vector2d);
    }

    private void updateActiveGene(){
        activeGene = (activeGene + 1)%getGenome().size();
    }

    public void move(WorldMap worldMap){
        int gene = this.getGenome().get(activeGene);
        this.updateActiveGene();
        MapDirection newDirection = this.direction.turn(gene);
        Vector2d newPosition = this.position.add(newDirection.toUnitVector());
        int width = worldMap.getBoundary().end().getX();
        int height = worldMap.getBoundary().end().getY();

        if (newPosition.getY() < 0 || newPosition.getY() > height) {
            newPosition = this.position;
            newDirection = this.direction.turn(4);
        }
        else if (newPosition.getX() < 0) {
            newPosition = new Vector2d(width,newPosition.getY());
        }
        else if (newPosition.getX() > width) {
            newPosition = new Vector2d(0, newPosition.getY());
        }
        this.position = newPosition;
        this.direction = newDirection;
    }

    public void eat(int energy) {
        stats.incrementEatenPlants();
        stats.changeEnergy(energy);
    }

    //TODO
    public void reproduce(Animal mate, int energyForReproduction, int reproductionCost, int minMutations, int maxMutations, WorldMap worldMap){
        int energyThis = this.getEnergy();
        int energyMate = mate.getEnergy();

        if (energyThis >= energyForReproduction && energyMate >= energyForReproduction) {


        }
    }

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
