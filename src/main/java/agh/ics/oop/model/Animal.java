package agh.ics.oop.model;

public class Animal implements WorldElement {

    private MapDirection direction;
    private Vector2d position;

    public Animal(){
        this.direction = MapDirection.NORTH;
        this.position = new Vector2d(2,2);
    }

    public Animal(Vector2d vector2d){
        this.position = vector2d;
        this.direction = MapDirection.NORTH;
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
