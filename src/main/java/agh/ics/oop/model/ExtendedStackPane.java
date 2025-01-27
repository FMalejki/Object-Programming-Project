package agh.ics.oop.model;

import javafx.scene.layout.StackPane;

public class ExtendedStackPane extends StackPane {
    public ExtendedStackPane(Animal animal, GamePresenter presenter) {
        this.setOnMouseClicked(event -> {presenter.trackAnimal(animal, this);});
    }
}
