package agh.ics.oop.model;

public class Grass {

    private Vector2d position;

    public Grass(Vector2d vector2d) {
        this.position = vector2d;
    }

    public Vector2d getPosition() {
        return position;
    }

    public String toString() {
        return "*";
    }


}
