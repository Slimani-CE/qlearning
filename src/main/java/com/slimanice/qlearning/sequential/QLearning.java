package com.slimanice.qlearning.sequential;

import com.slimanice.qlearning.utils.Action;

import java.util.ArrayList;
import java.util.Random;

public class QLearning {
    private int stateI;
    private int stateJ;
    private final int ACTION_SIZE = 4;
    private int gridSize ;
    private double alpha = 0.1;
    private double gamma = 0.9;
    private int maxEpoch = 10000;
    private double eps = 0.9;
    private double[][] qTable;
    private final int[][] actions;
    private final String[] strActions;
    private final int[][] grid;
    private boolean isStopped = false;
    private int startI, startJ;
    private ArrayList<Action> bestPath = new ArrayList<>();

    public QLearning(int [][]grid, int startI, int startJ, int gridSize, double alpha, double gamma, int maxEpoch, double eps){
        actions = new int[][]{
                {0, -1},  // Left
                {0, 1},   // Right
                {1, 0},   // Down
                {-1, 0}   // Up
        };
        strActions = new String[]{
          "Left",
          "Right",
          "Down",
          "Up"
        };
        this.grid = grid;
        this.gridSize = gridSize;
        this.startI = startI;
        this.startJ = startJ;
        this.alpha = alpha;
        this.gamma = gamma;
        this.maxEpoch = maxEpoch;
        this.eps = eps;
        qTable = new double[gridSize * gridSize][ACTION_SIZE];
    }

    private void resetState(){
        isStopped = false;
        stateI = startI;
        stateJ = startJ;
    }

    private int chooseAction(double eps){
        Random random = new Random();
        double bestQ = 0;
        int act = 0;
        if(random.nextDouble() < eps){
            // Exploration
            act = random.nextInt(ACTION_SIZE);
        }else{
            // Exploitation
            int st = stateI* gridSize + stateJ;
            for(int i = 0; i < ACTION_SIZE; i++){
                if(qTable[st][i] > bestQ){
                    bestQ = qTable[st][i];
                    act = i;
                }
            }
        }
        return act;
    }

    private int executeAction(int act){
        int tmpStateI = Math.max(0, Math.min(actions[act][0] + stateI, gridSize - 1));
        int tmpStateJ = Math.max(0, Math.min(actions[act][1] + stateJ, gridSize - 1));
        // Check if position changed
        if(tmpStateI == stateI && tmpStateJ == stateJ){
            isStopped = true;
        }
        else {
            stateI = tmpStateI;
            stateJ = tmpStateJ;
        }
        return stateI * gridSize + stateJ;
    }

    private boolean finished(){
        return grid[stateI][stateJ] == 1;
    }


    public void runQLearning(){
        int it = 0;
        int currentState;
        int nextState;
        int act, bestAction;
        while(it < maxEpoch) {
            resetState();
            while (!finished()) {
                currentState = stateI * gridSize + stateJ;
                act = chooseAction(eps);
                nextState = executeAction(act);
                bestAction = chooseAction(0);
                qTable[currentState][act] = qTable[currentState][act] + alpha * (grid[stateI][stateJ] + gamma * qTable[nextState][bestAction] - qTable[currentState][act]);
            }
            it++;
        }
//        displayQTable();
//        displayActions();
        makeBestPath();
    }
    private void displayActions(){
        System.out.println("**************** Best actions ****************");
        resetState();
        while(!finished() && !isStopped){
            int act = chooseAction(0);
            System.out.println("(" + stateI + ", " + stateJ + ")" + " Action: " + strActions[act]);
            executeAction(act);
        }
        if(isStopped){
            System.out.println("Agent is stopped by a wall!!!");
        }
        else System.out.println("(" + stateI + ", " + stateJ + ")");
    }

    private void displayQTable(){
        System.out.println("**************** Q Table ****************");
        for(double []row: qTable){
            System.out.print("[");
            for(double qvalue: row){
                System.out.printf(qvalue + ", ");
            }
            System.out.println("]");
        }
    }
    private void makeBestPath(){
        bestPath.clear();
        resetState();
        while(!finished() && !isStopped){
            int act = chooseAction(0);
            bestPath.add(new Action(stateI, stateJ, strActions[act]));
            executeAction(act);
        }
        if(isStopped){
//            System.out.println("Agent is stopped by a wall!!!");
            bestPath.add(new Action(stateI, stateJ, "Stopped by wall!"));
        }
        else bestPath.add(new Action(stateI, stateJ, "Agent arrived!"));
    }
    public ArrayList<Action> getBestPath() {
        return bestPath;
    }

    public void setBestPath(ArrayList<Action> bestPath) {
        this.bestPath = bestPath;
    }
}
