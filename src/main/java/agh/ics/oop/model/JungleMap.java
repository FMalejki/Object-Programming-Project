package agh.ics.oop.model;

import agh.ics.oop.model.util.Plant;
import agh.ics.oop.model.util.Vector2d;

import java.util.*;

public class JungleMap extends AbstractWorldMap{

    private final Map<Vector2d, Integer> nearPlants = new HashMap<>();

    public JungleMap(int width, int height) {
        super(width, height);
        for (int x = 0; x < width; x++){
            for (int y = 0; y < height; y++){
                neutralPlantSpots.add(new Vector2d(x, y));
            }
        }

    }

    public List<Vector2d> getSurroundings(Vector2d position){
        List<Vector2d> surroundings = List.of(
                new Vector2d(position.x()-1, position.y()-1),
                new Vector2d(position.x(), position.y()-1),
                new Vector2d(position.x()+1, position.y()-1),
                new Vector2d(position.x()+1, position.y()),
                new Vector2d(position.x()+1, position.y()+1),
                new Vector2d(position.x(), position.y()+1),
                new Vector2d(position.x()-1, position.y()+1),
                new Vector2d(position.x()-1, position.y()));
        return surroundings.stream().filter(v -> v.follows(boundary.start())).filter(v -> v.precedes(boundary.end())).toList();
    }

    @Override
    public boolean isPreferred(Vector2d position) {
        return nearPlants.containsKey(position);
    }

    @Override
    public void removePlant(Vector2d position) {
        List<Vector2d> surroundings = getSurroundings(position);
        plants.remove(position);
        for (Vector2d field : surroundings){
            if (nearPlants.get(field) == 1){
                nearPlants.remove(field);
                preferredPlantSpots.remove(field);
            }
            else {
                nearPlants.replace(field, nearPlants.get(field)-1);
            }
        }
        if (isPreferred(position)){
            preferredPlantSpots.add(position);
        }
        else {
            neutralPlantSpots.add(position);
        }
    }
    private void addPlants(List<Vector2d> positions){
        for (Vector2d position : positions) {
            if (isPreferred(position)){
                nearPlants.replace(position, nearPlants.get(position) + 1);
            }
            else if (!isPreferred(position) && plants.containsKey(position)) {
                nearPlants.put(position, 1);
            }
            else {
                preferredPlantSpots.add(position);
                neutralPlantSpots.remove(position);
                nearPlants.put(position, 1);
            }
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
                addPlants(getSurroundings(position));
            } else if (!preferredPlantSpots.isEmpty()) {
                Vector2d position = preferredPlantSpots.stream().toList().get(rand.nextInt(preferredPlantSpots.size()));
                plants.put(position, new Plant(position));
                preferredPlantSpots.remove(position);
                addPlants(getSurroundings(position));
            }

        }
    }

    @Override
    public Set<Vector2d> preferredSpots() {
        return super.preferredSpots();
    }
}
