package agh.ics.oop.model;

import java.util.Objects;

public class Vector2d {
    private final int x;
    private final int y;

    public Vector2d(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public String toString() {
        return "(" + this.x + "," + this.y + ")";
    }

    public boolean precedes(Vector2d other) {
        return this.x <= other.x && this.y <= other.y;
    }

    public boolean follows(Vector2d other) {
        return this.x >= other.x && this.y >= other.y;
    }

    public Vector2d add(Vector2d other) {
        return new Vector2d(this.x + other.x, this.y + other.y);
    }

    public Vector2d subtract(Vector2d other) {
        return new Vector2d(this.x - other.x, this.y - other.y);
    }

    public Vector2d upperRight(Vector2d other) {
        return new Vector2d((Math.max(this.x, other.x)),  (Math.max(this.y, other.y)));
    }

    public Vector2d lowerLeft(Vector2d other) {
        return new Vector2d((Math.min(this.x, other.x)),  (Math.min(this.y, other.y)));
    }

    public Vector2d opposite() {
        return new Vector2d(-this.x, -this.y);
    }

    public boolean instanceofVector2d(Object other) {
        return other instanceof Vector2d;
    }

//    public boolean equals(Object other) {
//        //if( this == other ) return true;
//        if (instanceofVector2d(other)) {
//            Vector2d otherVec = (Vector2d) other;
//            return this.x == otherVec.x && this.y == otherVec.y;
//        } else {
//            return false;
//        }
//    }
//    equals other - simpler method

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof Vector2d otherVec)) return false;
        return this.x == otherVec.x && this.y == otherVec.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
