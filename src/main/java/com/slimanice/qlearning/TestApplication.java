package com.slimanice.qlearning;

import com.slimanice.qlearning.sequential.QLearning;

public class TestApplication {
    public static void main(String[] args) {
        int [][]grid = new int[][]{
                {0, 0, 0, 0, 1},
                {0, -1, -1, -1, -1},
                {0, 0, 0, 0, 0},
                {-1, -1, -1, -1, 0},
                {0, 0, 0, 0, 0},
        };
        new QLearning(grid, 4, 0, 5, 0.1, 0.9, 1000).runQLearning();
    }
}
