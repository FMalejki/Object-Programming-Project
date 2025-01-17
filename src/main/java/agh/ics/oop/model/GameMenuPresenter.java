package agh.ics.oop.model;

import agh.ics.oop.model.util.Configuration;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.List;

public class GameMenuPresenter {
    @FXML
    private TextField width;
    @FXML
    private TextField height;
    @FXML
    private TextField startingPlantCount;
    @FXML
    private TextField plantsPerDay;
    @FXML
    private TextField energyFromEating;
    @FXML
    private TextField startingAnimalCount;
    @FXML
    private TextField startingEnergyAmount;
    @FXML
    private TextField energyForReproduction;
    @FXML
    private TextField reproductionCost;
    @FXML
    private TextField minMutations;
    @FXML
    private TextField maxMutations;
    @FXML
    private TextField genomeLength;
    @FXML
    private ChoiceBox<String> mapVariant;
    @FXML
    private ChoiceBox<String> animalVariant;
    @FXML
    private Button start;
    @FXML
    private Button loadConfig;
    @FXML
    private Button saveConfig;
    @FXML
    private CheckBox saveDailyStats;

    public GameMenuPresenter() {
    }


    private Configuration parseConfiguration() {
        int width = Integer.parseInt(this.width.getText());
        int height = Integer.parseInt(this.height.getText());
        int startingPlantCount = Integer.parseInt(this.startingPlantCount.getText());
        int plantsPerDay = Integer.parseInt(this.plantsPerDay.getText());
        int energyFromEating = Integer.parseInt(this.energyFromEating.getText());
        int startingAnimalCount = Integer.parseInt(this.startingAnimalCount.getText());
        int startingEnergyAmount = Integer.parseInt(this.startingEnergyAmount.getText());
        int energyForReproduction = Integer.parseInt(this.energyForReproduction.getText());
        int reproductionCost = Integer.parseInt(this.reproductionCost.getText());
        int minMutations = Integer.parseInt(this.minMutations.getText());
        int maxMutations = Integer.parseInt(this.maxMutations.getText());
        int genomeLength = Integer.parseInt(this.genomeLength.getText());
        String mapVariant = this.mapVariant.getValue();
        String animalVariant = this.animalVariant.getValue();

        return new Configuration(width, height, startingPlantCount, plantsPerDay, energyFromEating, startingAnimalCount, startingEnergyAmount, energyForReproduction, reproductionCost, minMutations, maxMutations, genomeLength, mapVariant, animalVariant);
    }

    private String parseSaveFile() {
        String c = width.getText();
        c = c + "," + height.getText();
        c = c + "," + startingPlantCount.getText();
        c = c + "," + plantsPerDay.getText();
        c = c + "," + energyFromEating.getText();
        c = c + "," + startingAnimalCount.getText();
        c = c + ", " + startingEnergyAmount.getText();
        c = c + "," + energyForReproduction.getText();
        c = c + "," + reproductionCost.getText();
        c = c + "," + minMutations.getText();
        c = c + "," + maxMutations.getText();
        c = c + "," + genomeLength.getText();
        c = c + "," + mapVariant.getValue();
        c = c + "," + animalVariant.getValue();
        return c;
    }

    private void fillWithLoadedConfig(String[] config) {
        width.setText(config[0]);
        height.setText(config[1]);
        startingPlantCount.setText(config[2]);
        plantsPerDay.setText(config[3]);
        energyFromEating.setText(config[4]);
        startingAnimalCount.setText(config[5]);
        startingEnergyAmount.setText(config[6]);
        energyForReproduction.setText(config[7]);
        reproductionCost.setText(config[8]);
        minMutations.setText(config[9]);
        maxMutations.setText(config[10]);
        genomeLength.setText(config[11]);
        mapVariant.setValue(config[12]);
        animalVariant.setValue(config[13]);
    }

    private WorldMap pickMap(Configuration config, GamePresenter presenter) {
        return switch (config.mapVariant()) {
            case "BasicMap" -> new BasicMap(config.width(), config.height(), presenter);
            case "JungleMap" -> new JungleMap(config.width(), config.height(), presenter);
            default -> new BasicMap(config.width(), config.height(), presenter);
        };
    }

    public void onClickStart() throws IOException {
        Configuration config = parseConfiguration();
        GamePresenter presenter = prepareStage();
        WorldMap map = pickMap(config, presenter);
        presenter.setWorldMap(map);
        Simulation simulation = new Simulation(config, map);
        simulation.run();
    }

    public void onClickSaveConfig() throws IOException {
        FileChooser fileChooser = new FileChooser();
        File configs = new File("src/main/resources/Configurations/");
        fileChooser.setInitialDirectory(configs);
        Stage stage = (Stage) saveConfig.getScene().getWindow();
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(parseSaveFile());
            writer.close();
        }
    }

    public void onClickLoadConfig() throws IOException {
        FileChooser fileChooser = new FileChooser();
        File configs = new File("src/main/resources/Configurations/");
        fileChooser.setInitialDirectory(configs);
        Stage stage = (Stage) loadConfig.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            if (line != null){
                String[] config = line.split(",");
                fillWithLoadedConfig(config);
            }
            else {
                throw new FileNotFoundException();
            }
        }
    }

    private GamePresenter prepareStage() throws IOException {
        Stage primaryStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("Game.fxml"));
        BorderPane viewRoot = loader.load();
        GamePresenter presenter = loader.getController();
        configureStage(primaryStage, viewRoot);
        primaryStage.show();
        return presenter;
    }

    private void configureStage(Stage primaryStage, BorderPane viewRoot) {
        var scene = new Scene(viewRoot);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Darwin World");
        primaryStage.minWidthProperty().bind(viewRoot.minWidthProperty());
        primaryStage.minHeightProperty().bind(viewRoot.minHeightProperty());
    }
}
