package agh.ics.oop.model;

import agh.ics.oop.model.util.Configuration;
import agh.ics.oop.model.util.Vector2d;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.Set;

public class GamePresenter {

    @FXML
    private GridPane mapGrid;
    @FXML
    private BorderPane borderPane;
    @FXML
    private Button pause;
    @FXML
    private Button showPreferredByPlants;
    @FXML
    private Button showDominatingGenotype;
    @FXML
    private TextArea generalStats;
    @FXML
    private TextArea trackedStats;

    private WorldMap worldMap;
    private Configuration config;
    private boolean paused = false;
    private Simulation simulation;
    private Animal trackedAnimal = null;
    private ExtendedStackPane trackedPane = null;


    public GamePresenter() {
    }

    public void setWorldMap(WorldMap worldMap) {
        this.worldMap = worldMap;
    }

    public void setConfig(Configuration config) {
        this.config = config;
    }

    public void setSimulation(Simulation simulation) {
        this.simulation = simulation;
    }

    public void startSimulation(Simulation simulation) {
        simulation.switchPause();
        Thread newThread = new Thread(simulation);
        newThread.start();
    }

    public void windowClosed(){
        simulation.close();
    }

    private void clearGrid() {
        mapGrid.getChildren().clear();
    }

    private double getCellSize() {
        double paneWidth = borderPane.getWidth();
        double paneHeight = borderPane.getHeight();
        int width = config.width();
        int height = config.height();
        return Math.min((paneWidth-310)/width, (paneHeight-60)/height);
    }

    private void resetTrackingText() {
        trackedStats.setText("Select an animal to track");
    }

    public void trackAnimal(Animal animal, ExtendedStackPane pane) {
        if (animal == null) {
            return;
        }
        if (trackedAnimal == animal) {
            trackedAnimal = null;
            trackedPane = null;
            Circle circle = (Circle) pane.getChildren().getFirst();
            circle.setFill(Color.BLACK);
            Platform.runLater(this::resetTrackingText);
        }
        else {
            if (trackedPane != null) {
                ((Circle) trackedPane.getChildren().getFirst()).setFill(Color.BLACK);
            }
            trackedAnimal = animal;
            trackedPane = pane;
            ((Circle) pane.getChildren().getFirst()).setFill(Color.PINK);
            Platform.runLater(() -> trackedStats.setText(trackedAnimal.toString()));
        }
    }

    private StackPane drawMapCell(boolean plant, Animal animal) {
        StackPane cell = new ExtendedStackPane(animal, this);
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
            if (trackedAnimal != null && animal.position() == trackedAnimal.position()) {
                circle.setFill(Color.PINK);
            }
            Rectangle rectangle = new Rectangle();
            adjustEnergyBar(rectangle, animal.getEnergy(), size);
            cell.getChildren().addAll(circle, rectangle);

        }
        return cell;
    }

    private void adjustEnergyBar(Rectangle rectangle, int energy, double size) {
        float percentage = Math.min((float) energy / (2 * config.reproductionCost()), 1);
        rectangle.setFill(javafx.scene.paint.Color.rgb(Math.round(Math.min(255, 2*255*(1-percentage))), Math.round(Math.min(255, 2*255*percentage)), 0));
        StackPane.setAlignment(rectangle, Pos.BOTTOM_LEFT);
        rectangle.setHeight(size*0.17);
        rectangle.setWidth(size*percentage);
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

    public void onClickPause() throws InterruptedException {
        if (paused) {
            unpause();
        }
        else {
            pause();
        }
    }
    private void pause() throws InterruptedException {
        paused = true;
        simulation.switchPause();
        pause.setText("Resume");
        showDominatingGenotype.setDisable(false);
        showPreferredByPlants.setDisable(false);
    }
    private void unpause() {
        paused = false;
        startSimulation(simulation);
        pause.setText("Pause");
        showDominatingGenotype.setDisable(true);
        showPreferredByPlants.setDisable(true);
    }

    public void onClickShowPreferredByPlants() {
        Set<Vector2d> preferredByPlants = worldMap.getPreferredPlantSpots();
        preferredByPlants.stream().map(pos -> mapGrid.getChildren().get(pos.x() * config.height() + pos.y())).map(cell -> (ExtendedStackPane) cell).forEach(cell -> cell.setStyle("-fx-background-color: #57811f"));

    }
    public void onClickShowDominatingGenotype() {
        Set<Vector2d> positions = worldMap.dominatingGenotypePos();
        positions.stream().map(pos -> mapGrid.getChildren().get(pos.x() * config.height() + pos.y())).map(cell -> (Circle) (((ExtendedStackPane) cell).getChildren().get(0))).forEach(circle -> {circle.setFill(javafx.scene.paint.Color.CYAN);});
    }
    public void statsChanged(String gStats) {
        Platform.runLater(() -> {generalStats.setText(gStats);
                                 if (trackedAnimal!=null) {trackedStats.setText(trackedAnimal.toString());}});
    }
}
