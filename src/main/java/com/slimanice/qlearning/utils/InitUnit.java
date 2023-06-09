package com.slimanice.qlearning.utils;

import java.util.Arrays;

// Create object that initialize the Q Learning algorithm
// The object will be containing the start position, the grid, alpha, gamma, epochs, ..etc
public class InitUnit {
    private int gridSize = 5;
    private double alpha = 0.1;
    private double gamma = 0.9;
    private int maxEpoch = 10;
    private int startI = 2;
    private int startJ = 4;
    private int nbrAgents = 4;
    private double eps;
    private int[][] grid = new int[][]{
            {0, 0, 0, 0, 1},
            {0, -1, -1, -1, -1},
            {0, 0, 0, 0, 0},
            {-1, -1, -1, -1, 0},
            {0, 0, 0, 0, 0},
    };

    public InitUnit(){

    }

    public InitUnit(int gridSize, double alpha, double gamma, int maxEpoch, int startI, int startJ, int[][] grid, int nbrAgents, double eps) {
        this.gridSize = gridSize;
        this.alpha = alpha;
        this.gamma = gamma;
        this.maxEpoch = maxEpoch;
        this.startI = startI;
        this.startJ = startJ;
        this.grid = grid;
        this.nbrAgents = nbrAgents;
        this.eps = eps;
    }

    public int getGridSize() {
        return gridSize;
    }

    public void setGridSize(int gridSize) {
        this.gridSize = gridSize;
    }

    public double getAlpha() {
        return alpha;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    public double getGamma() {
        return gamma;
    }

    public void setGamma(double gamma) {
        this.gamma = gamma;
    }

    public int getMaxEpoch() {
        return maxEpoch;
    }

    public void setMaxEpoch(int maxEpoch) {
        this.maxEpoch = maxEpoch;
    }

    public int getStartI() {
        return startI;
    }

    public void setStartI(int startI) {
        this.startI = startI;
    }

    public int getStartJ() {
        return startJ;
    }

    public void setStartJ(int startJ) {
        this.startJ = startJ;
    }

    public int[][] getGrid() {
        return grid;
    }

    public void setGrid(int[][] grid) {
        this.grid = grid;
    }

    public void setNbrAgents(int nbrAgents) {
        this.nbrAgents = nbrAgents;
    }

    public int getNbrAgents() {
        return nbrAgents;
    }

    public void setEps(double eps) {
        this.eps = eps;
    }

    public double getEps() {
        return eps;
    }

    @Override
    public String toString() {
        return "InitUnit{" +
                "gridSize=" + gridSize +
                ", alpha=" + alpha +
                ", gamma=" + gamma +
                ", maxEpoch=" + maxEpoch +
                ", startI=" + startI +
                ", startJ=" + startJ +
                ", grid=" + Arrays.toString(grid) +
                '}';
    }
    
}
