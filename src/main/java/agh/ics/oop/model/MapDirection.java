package agh.ics.oop.model;

import java.util.Random;

public enum MapDirection {
    NORTH,
    SOUTH,
    WEST,
    EAST,
    NORTH_WEST,
    NORTH_EAST,
    SOUTH_WEST,
    SOUTH_EAST,;

    public String toString() {
        return switch (this) {
            case EAST -> "E";
            case WEST -> "W";
            case NORTH -> "N";
            case SOUTH -> "S";
            case NORTH_WEST -> "NW";
            case NORTH_EAST -> "NE";
            case SOUTH_WEST -> "SW";
            case SOUTH_EAST -> "SE";
        };
    }

    public MapDirection next() {
        return switch (this) {
            case NORTH -> NORTH_EAST;
            case NORTH_EAST -> EAST;
            case EAST -> SOUTH_EAST;
            case SOUTH_EAST -> SOUTH;
            case SOUTH -> SOUTH_WEST;
            case SOUTH_WEST -> WEST;
            case WEST -> NORTH_WEST;
            case NORTH_WEST -> NORTH;
        };
    }

    public MapDirection previous() {
        return switch (this) {
            case NORTH_EAST -> NORTH;
            case EAST -> NORTH_EAST;
            case SOUTH_EAST -> EAST;
            case SOUTH -> SOUTH_EAST;
            case SOUTH_WEST -> SOUTH;
            case WEST -> SOUTH_WEST;
            case NORTH_WEST -> WEST;
            case NORTH -> NORTH_WEST;
        };
    }

    public Vector2d toUnitVector(){
        return switch (this){
            case NORTH -> new Vector2d(0,1);
            case NORTH_WEST -> new Vector2d(-1,1);
            case NORTH_EAST -> new Vector2d(1,1);
            case SOUTH_EAST -> new Vector2d(1,-1);
            case SOUTH_WEST -> new Vector2d(-1,-1);
            case SOUTH -> new Vector2d(0,-1);
            case WEST -> new Vector2d(-1,0);
            case EAST -> new Vector2d(1,0);
        };
    }

    public static MapDirection randomDirection() {
        MapDirection[] directions = values();
        int randomIndex = new Random().nextInt(directions.length);
        return directions[randomIndex];
    }
}
