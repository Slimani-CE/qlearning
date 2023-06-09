package com.slimanice.qlearning;

import com.slimanice.qlearning.sequential.QLearning;
import com.slimanice.qlearning.utils.Action;
import com.slimanice.qlearning.utils.GridCell;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class MainController implements Initializable {

    @FXML
    private GridPane grid;
    @FXML
    private ToggleGroup radio;
    @FXML
    private Button runBtn;
    @FXML
    private Button resetBtn;
    @FXML
    private TextField gridSizeField;
    @FXML
    private Button initBtn;
    private int gridSize;
    private int startI = -1;
    private int startJ = -1;
    private double alpha;
    private double gamma;
    private int maxEpoch;
    private double eps;
    @FXML
    private TextField alphaField;
    @FXML
    private TextField gammaField;
    @FXML
    private TextField maxEpochField;
    @FXML
    private Label detailsLabel;
    @FXML
    private Slider slider;
    @FXML
    private Label explorationLabel;
    @FXML
    private Label exploitationLabel;
    int intGrid[][];
    private GridCell currStartPosition;
    private QLearning qLearning;
    ArrayList<Action> bestPath = new ArrayList<>();
    ArrayList<GridCell> cells = new ArrayList<>();
    private double cellHeight;
    private double cellWidth;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initGrid();
        detailsLabel.setText("Specify the size of your grid");

        // Add listener to the init btn
        initBtn.setOnMouseClicked(mouseEvent -> {
            System.out.println("Init...");

            initGrid();
            System.out.println(gridSize);
        });

        // Add listener to the run button
        runBtn.setOnMouseClicked(mouseEvent -> {
            if(checkInputFields()) {
                // Remove old path in the gui
                removeOldPath();
                detailsLabel.setText("Q Learning is running...");
                qLearning = new QLearning(intGrid, startI, startJ, gridSize, alpha, gamma, maxEpoch, eps);
                qLearning.runQLearning();
                bestPath = qLearning.getBestPath();
                displayBestPath();
            }
        });

        // Add listener to the reset button
        resetBtn.setOnMouseClicked(mouseEvent -> {
            displayGrid();
            removeOldPath();
        });

        // Add listener to the slider element
        slider.setOnMouseMoved(mouseEvent -> {
            double percent = slider.getValue();
            explorationLabel.setText("Exploration ("+ (int)percent +"%)");
            exploitationLabel.setText("Exploration ("+ (100 - (int)percent) +"%)");
        });
    }

    private boolean checkInputFields() {
        // Check fields
        if (gridSizeField.getText().isEmpty() || maxEpochField.getText().isEmpty() || alphaField.getText().isEmpty() || gammaField.getText().isEmpty()) {
            // Alert user to fill all fields
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Please fill all fields!");
            alert.showAndWait();
            return false;
        }
        try{
            alpha = Double.parseDouble(alphaField.getText());
            gamma = Double.parseDouble(gammaField.getText());
            maxEpoch = Integer.parseInt(maxEpochField.getText());
            eps = slider.getValue() / 100;
            System.out.println("Eps: " + eps);
        }catch (Exception e){
            // Alert user to fill all fields
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Please fill all fields!");
            alert.showAndWait();
            return false;
        }
        // Check if grid contains a start position and at least one target
        boolean isStartExists = false;
        boolean isTargetExists = false;
        for(int[] row : intGrid)
            for(int cell : row){
                if(cell == 1) {
                    isTargetExists = true;
                    break;
                }
            }
        if(startI != -1 && startJ != -1)
            isStartExists = true;

        if(!isTargetExists || !isStartExists){
            // Alert user to add target and start position
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Please add targets and a start position!");
            alert.showAndWait();
        }
        return isTargetExists && isStartExists;
    }

    private void initGrid() {
        if(gridSizeField.getText().isEmpty()) {
            detailsLabel.setText("Specify the size of your grid");
            return;
        }
        // Get grid size
        try{
            gridSize = Integer.parseInt(gridSizeField.getText());
        }
        catch (Exception e){

        }
        detailsLabel.setText("");
        intGrid = new int[gridSize][gridSize];
        cells.clear();
        bestPath.clear();
        grid.getChildren().clear();
        grid.getRowConstraints().clear();
        grid.getColumnConstraints().clear();
        for (int row = 0; row < gridSize; row++) {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setFillHeight(true);
            grid.getRowConstraints().add(rowConstraints);
        }

        for (int col = 0; col < gridSize; col++) {
            ColumnConstraints colConstraints = new ColumnConstraints();
            colConstraints.setFillWidth(true);
            grid.getColumnConstraints().add(colConstraints);
        }
        grid.setVgap(.2);
        grid.setHgap(.2);
        cellHeight = grid.getPrefHeight() / gridSize;
        cellWidth = grid.getPrefWidth() / gridSize;

        // Initialize the grid and intGrid
        for(int i = 0; i < gridSize; i++)
            for(int j = 0; j < gridSize; j++){
                GridCell cell = new GridCell(cellWidth, cellHeight, i, j, this);
                grid.add(cell, j, i);
                cells.add(cell);
            }
    }

    private void removeOldPath() {
        for(Action action : bestPath){
            if(action.getCurrI() == startI && action.getCurrJ() == startJ || intGrid[action.getCurrI()][action.getCurrJ()] == 1)
                continue;
            GridCell cell = cells.get(action.getCurrI()*gridSize + action.getCurrJ());
            cell.unclick();
        }
    }

    private void displayBestPath() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            int i = 1;
            @Override
            public void run() {
                if(i == bestPath.size() - 1) {
                    timer.cancel();
                    if (bestPath.size() != 0) {
                        Platform.runLater(() -> {
                            Action lastAction = bestPath.get(bestPath.size() - 1);
                            if (intGrid[lastAction.getCurrI()][lastAction.getCurrJ()] == 1) {
                                detailsLabel.setText("Arrived to target !");
                            } else
                                detailsLabel.setText("Stopped by wall");
                        });
                    }
                }
                else {
                    Action action = bestPath.get(i);
                    GridCell cell = cells.get(action.getCurrI() * gridSize + action.getCurrJ());
                    cell.setClicked(true);
                    cell.setCellType("path");
                    cell.setStyle("-fx-border-color: black; -fx-border-width: 1px;" +
                            " -fx-background-image: url(" + MainController.class.getResource("media/" + action.getAction() + ".png") + ");" +
                            "-fx-background-repeat: no-repeat;" +
                            "-fx-background-position: center center; " +
                            "-fx-alignment: center;" +
                            "-fx-background-size: 100%; ");
                    Label label = new Label();
//            label.setText(action.getAction());
                    label.setStyle("-fx-text-fill: rgba(32,14,79,0.73)");
                    Platform.runLater(() -> {
                        cell.getChildren().add(label);
                    });
                    i++;
                }
            }
        }, 0, 100);
    }

    private void displayGrid() {
        System.out.println("Grid = [");
        for(int[] row : intGrid) {
            System.out.print("\t[");
            for (int cell : row) {
                System.out.print(cell + ", ");
            }
            System.out.println("], ");
        }
        System.out.println("]");
    }

    public void checkCell(GridCell cell){
        cell.setClicked(true);
        displayGrid();
        removeOldPath();
        // Check clicked radio
        String label = ((RadioButton)radio.getSelectedToggle()).getText();
        switch (label){
            case "Target" : putTarget(cell); break;
            case "Wall" : putWall(cell); break;
            case "Start position": putStartPosition(cell); break;
        }
        Platform.runLater(() -> {
            removeCellFromPath(cell);
        });
    }

    public void uncheckCell(GridCell cell){

        String label = ((RadioButton)radio.getSelectedToggle()).getText();
        String cellType = cell.getCellType();
        cell.setClicked(false);

        switch (cell.getCellType()){
            case "Target" : removeTarget(cell); break;
            case "Wall" : removeWall(cell); break;
            case "Start position": removeStartPosition(cell); break;
            case "path": cell.unclick(); Platform.runLater(() -> {
                removeCellFromPath(cell);
            }); break;
        }
        if(!label.equals(cellType)){
            checkCell(cell);
        }
    }

    private void removeCellFromPath(GridCell cell) {
        for(Action action : bestPath){
            if(action.getCurrI() == cell.getRow() && action.getCurrJ() == cell.getCol()){
                bestPath.remove(action);
            }
        }
    }


    private void removeStartPosition(GridCell cell) {

        // Change gui
        cell.setStyle(null);
        cell.setStyle("-fx-border-color: black; -fx-border-width: 1px;");
        cell.getChildren().clear();

        startI = -1;
        startJ = -1;
    }

    private void removeWall(GridCell cell) {
        // Change gui
        cell.setStyle(null);
        cell.setStyle("-fx-border-color: black; -fx-border-width: 1px;");
        cell.getChildren().clear();

        intGrid[cell.getRow()][cell.getCol()] = 0;
    }

    private void removeTarget(GridCell cell) {
        // Change gui
        cell.setStyle(null);
        cell.setStyle("-fx-border-color: black; -fx-border-width: 1px;");
        cell.getChildren().clear();

        intGrid[cell.getRow()][cell.getCol()] = 0;
    }

    private void putStartPosition(GridCell cell) {
        // Change gui
        cell.setStyle("-fx-border-color: black; -fx-border-width: 1px;" +
                " -fx-background-image: url("+ MainController.class.getResource("media/robot1.png")+");" +
                "-fx-background-repeat: no-repeat;" +
                "-fx-background-position: center center; " +
                "-fx-alignment: center;" +
                "-fx-background-size: 60%");

        cell.setCellType("Start position");
        Label label = new Label();
//        label.setText("Start");
        cell.getChildren().add(label);
        if(currStartPosition != null && (currStartPosition.getRow() != cell.getRow() || currStartPosition.getCol() != cell.getCol())) {
            uncheckCell(currStartPosition);
        }
        currStartPosition = cell;

        // Set start position
        startI = cell.getRow();
        startJ = cell.getCol();

        // Change any old value in the intGrid with 0
        intGrid[startI][startJ] = 0;
    }

    private void putWall(GridCell cell) {
        // Change gui
        cell.setStyle("-fx-border-color: black; -fx-border-width: 1px;" +
                " -fx-background-image: url("+ MainController.class.getResource("media/wall.png")+");" +
                "-fx-background-repeat: no-repeat;" +
                "-fx-background-position: center center; " +
                "-fx-alignment: center;" +
                "-fx-background-size: 100%");
        Label label = new Label();
//        label.setText("Wall");
        label.setStyle("-fx-text-fill: white");
        cell.getChildren().add(label);
        cell.setCellType("Wall");

        // Change intGrid
        intGrid[cell.getRow()][cell.getCol()] = -1;
    }

    private void putTarget(GridCell cell) {
        // Change gui
        cell.setStyle("-fx-border-color: black; -fx-border-width: 1px;" +
                " -fx-background-image: url("+ MainController.class.getResource("media/target.png")+");" +
                "-fx-background-repeat: no-repeat;" +
                "-fx-background-position: center center; " +
                "-fx-alignment: center;" +
                "-fx-background-size: 60%");
        Label label = new Label();
//        label.setText("Target");
        cell.getChildren().add(label);
        cell.setCellType("Target");

        // Change intGrid
        intGrid[cell.getRow()][cell.getCol()] = 1;
    }
}