package agh.ics.oop.model;

import agh.ics.oop.model.util.Boundary;

import java.util.Random;

public class BasicMap extends AbstractWorldMap {

    private Boundary jungleBonds;

    public BasicMap(int width, int height) {
        super(width, height);
        this.initializeJungle();
    }

    private void initializeJungle() {
        Vector2d lowerBound = boundary.start();
        Vector2d upperBound = boundary.end();
        int height = upperBound.getY() - lowerBound.getY() + 1;
        int jungleHeight = height/5;
        if (0.2 - (float) jungleHeight/height > 0.2 - (float) (jungleHeight + 1)/height) {}
            jungleHeight += 1;
        lowerBound = new Vector2d(0, (height - jungleHeight) / 2);
        upperBound = new Vector2d(upperBound.getX(), lowerBound.getY() + jungleHeight - 1);
        jungleBonds = new Boundary(lowerBound, upperBound);
    }

    @Override
    public boolean isPreferred(Vector2d position) {
        return (position.follows(jungleBonds.start()) && position.precedes(jungleBonds.end()));
    }

    @Override
    public void removePlant(Vector2d position) {
        plants.remove(position);
        if (this.isPreferred(position)) {
            preferredPlantSpots.add(position);
        }
        else {
            neutralPlantSpots.add(position);
        }
    }

    @Override
    public void growPlants(int amount) {
        Random rand = new Random();
        for (int i = 0; i < amount; i++) {
            int chance = rand.nextInt(100);
            if ((preferredPlantSpots.isEmpty() || chance < 20) && !neutralPlantSpots.isEmpty()) {
                Vector2d position = neutralPlantSpots.stream().toList().get(rand.nextInt(neutralPlantSpots.size()));
                plants.put(position, new Plant(position));
                neutralPlantSpots.remove(position);
            } else if (!preferredPlantSpots.isEmpty()) {
                Vector2d position = preferredPlantSpots.stream().toList().get(rand.nextInt(preferredPlantSpots.size()));
                plants.put(position, new Plant(position));
                preferredPlantSpots.remove(position);
            }
        }
    }
}
