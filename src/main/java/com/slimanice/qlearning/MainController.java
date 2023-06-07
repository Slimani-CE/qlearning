package com.slimanice.qlearning;

import com.slimanice.qlearning.sequential.QLearning;
import com.slimanice.qlearning.utils.Action;
import com.slimanice.qlearning.utils.GridCell;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

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
    @FXML
    private TextField alphaField;
    @FXML
    private TextField gammaField;
    @FXML
    private TextField maxEpochField;
    @FXML
    private Label detailsLabel;
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

        // Add listener to the init btn
        initBtn.setOnMouseClicked(mouseEvent -> {

            initGrid();
            System.out.println(gridSize);
        });

        // Add listener to the run button
        runBtn.setOnMouseClicked(mouseEvent -> {
            if(checkInputFields()) {
                // Remove old path in the gui
                removeOldPath();
                qLearning = new QLearning(intGrid, startI, startJ, gridSize, alpha, gamma, maxEpoch);
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
        // Get grid size
        try{
            gridSize = Integer.parseInt(gridSizeField.getText());
        }
        catch (Exception e){

        }
//        grid.getChildren().clear();
        cellHeight = grid.getPrefHeight() / gridSize;
        cellWidth = grid.getPrefWidth() / gridSize;

        // Initialize the grid and intGrid
        for(int i = 0; i < gridSize; i++)
            for(int j = 0; j < gridSize; j++){
                GridCell cell = new GridCell(cellWidth, cellHeight, i, j, this);
                grid.add(cell, j, i);
                cells.add(cell);
            }
        grid.setGridLinesVisible(true);
        intGrid = new int[gridSize][gridSize];
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
        for(int i = 1; i < bestPath.size() - 1; i++){
            Action action = bestPath.get(i);
            GridCell cell = cells.get(action.getCurrI()*gridSize + action.getCurrJ());
            cell.setClicked(true);
            cell.setCellType("path");
            // Change gui
            cell.setStyle("-fx-border-color: black; -fx-border-width: 1px; -fx-background-color: #fd862c");
            Label label = new Label();
            label.setText(action.getAction());
            label.setStyle("-fx-text-fill: rgba(32,14,79,0.73)");
            cell.getChildren().add(label);
        }
        if(bestPath.size() != 0){
            Action lastAction = bestPath.get(bestPath.size() - 1);
            if(intGrid[lastAction.getCurrI()][lastAction.getCurrJ()] == 1){
                detailsLabel.setText("Arrived to target !");
            }
            else
                detailsLabel.setText("Stopped by wall");
        }
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
        cell.getChildren().clear();

        startI = -1;
        startJ = -1;
    }

    private void removeWall(GridCell cell) {
        // Change gui
        cell.setStyle(null);
        cell.getChildren().clear();

        intGrid[cell.getRow()][cell.getCol()] = 0;
    }

    private void removeTarget(GridCell cell) {
        // Change gui
        cell.setStyle(null);
        cell.getChildren().clear();

        intGrid[cell.getRow()][cell.getCol()] = 0;
    }

    private void putStartPosition(GridCell cell) {
        // Change gui
        cell.setStyle("-fx-border-color: black; -fx-border-width: 1px; -fx-background-color: #75779a");
        cell.setCellType("Start position");
        Label label = new Label();
        label.setText("Start");
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
        cell.setStyle("-fx-border-color: black; -fx-border-width: 1px; -fx-background-color: #25273a");
        Label label = new Label();
        label.setText("Wall");
        label.setStyle("-fx-text-fill: white");
        cell.getChildren().add(label);
        cell.setCellType("Wall");

        // Change intGrid
        intGrid[cell.getRow()][cell.getCol()] = -1;
    }

    private void putTarget(GridCell cell) {
        cell.setStyle("-fx-border-color: black; -fx-border-width: 1px; -fx-background-color: #36dc69");
        Label label = new Label();
        label.setText("Target");
        cell.getChildren().add(label);
        cell.setCellType("Target");

        // Change intGrid
        intGrid[cell.getRow()][cell.getCol()] = 1;
    }
}