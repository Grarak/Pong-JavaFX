/*
 * Copyright (C) 2015 Willi Ye
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package grarak.pong;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Created by willi on 07.05.15.
 */
public class Main extends Application {

    public enum PLAYER {
        ONE, TWO
    }

    private double width = 800;
    private double height = 400;
    private int fps = 0;

    private Paddle paddleOne;
    private Paddle paddleTwo;
    private Ball ball;
    private Point pointOne;
    private Point pointTwo;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Pane root = new Pane();
        StackPane holder = new StackPane();
        holder.setStyle("-fx-background-color: black");

        Canvas canvas = new Canvas(width, height);

        holder.getChildren().add(canvas);
        root.getChildren().add(holder);

        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        start(graphicsContext);

        Scene scene = new Scene(root);

        scene.widthProperty().addListener((observable, oldValue, newValue) -> {
            width = newValue.doubleValue();
        });
        scene.heightProperty().addListener((observable, oldValue, newValue) -> {
            height = newValue.doubleValue();
        });

        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case LEFT:
                    paddleTwo.moveUp();
                    break;
                case RIGHT:
                    paddleTwo.moveDown();
                    break;
                case A:
                    paddleOne.moveUp();
                    break;
                case D:
                    paddleOne.moveDown();
                    break;
            }
        });
        scene.setOnKeyReleased(event -> {
            switch (event.getCode()) {
                case LEFT:
                case RIGHT:
                    paddleTwo.stopMoving();
                    break;
                case A:
                case D:
                    paddleOne.stopMoving();
                    break;
            }
        });

        primaryStage.setTitle("Pong");
        primaryStage.setScene(scene);
        primaryStage.show();
        draw(canvas, graphicsContext);
    }

    private void start(GraphicsContext graphicsContext) {
        paddleOne = new Paddle(graphicsContext, PLAYER.ONE);
        paddleTwo = new Paddle(graphicsContext, PLAYER.TWO);
        ball = new Ball(graphicsContext, paddleOne, paddleTwo, new Ball.OnPointListener() {

            @Override
            public void playerOne() {
                pointOne.point++;
                reset();
            }

            @Override
            public void playerTwo() {
                pointTwo.point++;
                reset();
            }

            private void reset() {
                new Thread(() -> {
                    try {
                        Thread.sleep(1000);
                        ball.reset();
                        paddleOne.reset();
                        paddleTwo.reset();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        });
        pointOne = new Point(graphicsContext, PLAYER.ONE);
        pointTwo = new Point(graphicsContext, PLAYER.TWO);
    }

    private void draw(Canvas canvas, GraphicsContext graphicsContext) {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                    System.out.println("FPS: " + fps);
                    fps = 0;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }).start();

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(17), event -> {
            graphicsContext.clearRect(0, 0, width, height);

            pointOne.draw(width, height);
            pointTwo.draw(width, height);
            ball.draw(width, height);
            paddleOne.draw(width, height);
            paddleTwo.draw(width, height);

            canvas.setHeight(height);
            canvas.setWidth(width);
            fps++;
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

}
