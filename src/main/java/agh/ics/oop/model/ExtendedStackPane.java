package agh.ics.oop.model;

import javafx.scene.layout.StackPane;

public class ExtendedStackPane extends StackPane {
    private final Animal animal;
    private final GamePresenter presenter;
    public ExtendedStackPane(Animal animal, GamePresenter presenter) {
        this.animal = animal;
        this.presenter = presenter;
        this.setOnMouseClicked(event -> {presenter.trackAnimal(animal, this);});
    }
}
