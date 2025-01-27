package agh.ics.oop.model.util;

public record Vector2d(int x, int y) {
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
        return new Vector2d((Math.max(this.x, other.x)), (Math.max(this.y, other.y)));
    }

    public Vector2d lowerLeft(Vector2d other) {
        return new Vector2d((Math.min(this.x, other.x)), (Math.min(this.y, other.y)));
    }

    public Vector2d opposite() {
        return new Vector2d(-this.x, -this.y);
    }

    public boolean instanceofVector2d(Object other) {
        return other instanceof Vector2d;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof Vector2d otherVec)) return false;
        return this.x == otherVec.x && this.y == otherVec.y;
    }

}
