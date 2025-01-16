package agh.ics.oop.model;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Animal animal = new Animal(new Vector2d(2,2), 8, new Genotype(6));
        System.out.println(animal.getEnergy());
    }
}
