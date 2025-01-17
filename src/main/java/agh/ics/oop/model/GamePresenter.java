package agh.ics.oop.model;

import agh.ics.oop.model.util.Boundary;
import agh.ics.oop.model.util.Configuration;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class GamePresenter {

    @FXML
    private GridPane mapGrid;
    @FXML
    private BorderPane borderPane;


    private WorldMap worldMap;
    private Configuration config;


    public GamePresenter() {

    }

    public void setWorldMap(WorldMap worldMap) {
        this.worldMap = worldMap;
    }

    private void clearGrid() {
        mapGrid.getChildren().retainAll(mapGrid.getChildren().get(0)); // hack to retain visible grid lines
    }

    private double getCellSize() {
        double paneWidth = borderPane.getWidth();
        double paneHeight = borderPane.getHeight();
        int width = config.width();
        int height = config.height();
        return Math.min((paneWidth-200)/width, (paneHeight-50)/height);
    }

    private StackPane drawMapCell(boolean plant, Animal animal) {
        StackPane cell = new StackPane();
        double size = getCellSize();
        cell.setMinWidth(size);
        cell.setMinHeight(size);
        if (plant) {
            cell.setStyle("-fx-background-color: #196914");
        }
        else {
            cell.setStyle("-fx-background-color: #67c261");
        }
        if (animal != null) {
            Circle circle = new Circle();
            circle.setCenterX(size/2);
            circle.setCenterY(0.8*size/2);
            circle.setRadius(0.75*size/2);
            Rectangle rectangle = new Rectangle();
            rectangle.setHeight(size*0.17);
            rectangle.setWidth(size*0.95);
            cell.getChildren().addAll(circle, rectangle);
        }
        return cell;
    }

    public void drawMap() {
        clearGrid();
        int width = config.width();
        int height = config.height();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Vector2d pos = new Vector2d(x, y);
                mapGrid.add(drawMapCell(worldMap.hasPlant(pos), worldMap.animalAt(pos)), x, y);
            }
        }
    }

    public void mapChanged() {
        Platform.runLater(this::drawMap);
    }

}
