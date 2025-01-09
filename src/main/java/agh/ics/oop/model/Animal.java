package agh.ics.oop.model;

public class Animal implements WorldElement {

    private MapDirection direction;
    private Vector2d position;
    private int energy;
    private final Genotype genotype;
    private final WorldMap map;
    private int age = 0;
    private int children;
    private boolean alive = true;

    public Animal(WorldMap map, Vector2d startPosition, int startEnergy){
        this.direction = MapDirection.randomDirection();
        this.position = startPosition;
        this.energy = startEnergy;
        this.genotype = new Genotype();
        this.map = map;
    }

    public Animal(WorldMap map, Vector2d startPosition, int startEnergy, Genotype fatherGenotype, Genotype motherGenotype){
        this.direction = MapDirection.randomDirection();
        this.position = startPosition;
        this.energy = startEnergy;
        this.genotype = new Genotype(fatherGenotype, motherGenotype);
        this.map = map;
    }

    @Override
    public Vector2d getPosition(){
        return this.position;
    }

    public MapDirection getDirection(){
        return this.direction;
    }

    public void death(){
        this.alive = false;
    }

    public int getEnergy(){
        return this.energy;
    }

    public int getChildren(){
        return this.children;
    }

    public int getAge(){
        return this.age;
    }

    public void eating(int plantEnergy){
        this.energy += plantEnergy;
    }

    public void breeding() {

    }

    public Genotype getGenotype() {
        return genotype;
    }

    @Override
    public String toString(){
        return this.direction.toString();
    }

    public Boolean isAt(Vector2d vector2d)
    {
        return this.position.equals(vector2d);
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
