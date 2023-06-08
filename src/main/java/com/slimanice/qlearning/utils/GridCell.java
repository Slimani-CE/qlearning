package com.slimanice.qlearning.utils;

import com.slimanice.qlearning.MainController;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class GridCell extends Pane {
    private final MainController controller;
    private boolean isClicked;
    private int row;
    private int col;
    private String cellType;

    public GridCell(double width, double height, int row, int col, MainController controller) {
        this.row = row;
        this.col = col;
        this.setPrefSize(width, height);
        this.setMinSize(width, height);
        this.setMaxSize(width, height);
        this.controller = controller;
        this.setStyle("-fx-border-color: black; -fx-border-width: 1px;");
        this.setOnMouseClicked(e -> {
            System.out.println("GridCell debug: setOnMouseClicked() | isClicked: " + isClicked);
            if (!isClicked)
                controller.checkCell(this);
            else
                controller.uncheckCell(this);
        });
    }

    public boolean isClicked() {
        return isClicked;
    }

    public void setClicked(boolean clicked) {
        isClicked = clicked;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public String getCellType() {
        return cellType;
    }

    public void setCellType(String cellType) {
        this.cellType = cellType;
    }

    public void unclick() {
        if(isClicked) {
            this.setStyle(null);
            this.setStyle("-fx-border-color: black; -fx-border-width: 1px;");
            if (this.getChildren().size() != 0) {
                this.getChildren().remove(0);
            }
            isClicked = false;
        }
    }
}
