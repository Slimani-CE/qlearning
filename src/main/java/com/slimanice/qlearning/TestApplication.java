package com.slimanice.qlearning;

import com.slimanice.qlearning.sequential.QLearning;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;

import java.io.IOException;

public class TestApplication {
    public static void main(String[] args) throws IOException {
//        int [][]grid = new int[][]{
//                {0, 0, 0, 0, 1},
//                {0, -1, -1, -1, -1},
//                {0, 0, 0, 0, 0},
//                {-1, -1, -1, -1, 0},
//                {0, 0, 0, 0, 0},
//        };
//        new QLearning(grid, 2, 4, 5, 0.1, 0.9, 10000).runQLearning();
        String imagePath = "com/slimanice/qlearning/media/6134346.png";
        TestApplication.class.getResource("media/robot.png");
//        Image image = new Image(imagePath);
        System.out.println();
    }
}
