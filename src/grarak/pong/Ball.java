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

import com.sun.javafx.geom.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Created by willi on 07.05.15.
 */
public class Ball extends DrawObject {

    private final Paddle paddleOne;
    private final Paddle paddleTwo;
    private final OnPointListener onPointListener;

    private Vec2d position;
    private Vec2d velocity;
    private int radius;

    private boolean gameover;
    private double angleOffset;

    public Ball(GraphicsContext graphicsContext, Paddle paddleOne, Paddle paddleTwo, OnPointListener onPointListener) {
        super(graphicsContext);
        this.paddleOne = paddleOne;
        this.paddleTwo = paddleTwo;
        this.onPointListener = onPointListener;
        radius = 10;
        angleOffset = 0;
    }

    public void draw(double width, double height) {
        if (gameover) return;
        if (position == null) position = new Vec2d(width / 2, height / 2);
        if (velocity == null) velocity = new Vec2d(-4, 0);

        position.x += velocity.x;
        position.y += velocity.y;
        if (velocity.x < 0
                && position.x - radius < paddleOne.getWidth()
                && touchedPaddle(paddleOne)) {
            velocity.x *= -1;
            velocity.y += getVelocityY(paddleOne);
            position.x = paddleOne.getWidth() + radius * 2;
        }

        if (velocity.x > 0
                && position.x + radius > width - paddleTwo.getWidth()
                && touchedPaddle(paddleTwo)) {
            velocity.x *= -1;
            velocity.y += getVelocityY(paddleTwo);
            position.x = width - paddleTwo.getWidth();
        }

        if (touchedWall(height)) velocity.y *= -1;

        position.y += angleOffset;
        if (position.x >= 0 && position.x <= width) {
            getGraphicsContext().setFill(Color.RED);
            getGraphicsContext().fillOval(position.x - radius, position.y - radius, radius, radius);
        } else {
            gameover = true;
            if (velocity.x < 0) onPointListener.playerTwo();
            else onPointListener.playerOne();
        }
    }

    public void reset() {
        position = null;
        velocity = null;
        gameover = false;
        angleOffset = 0;
    }

    private boolean touchedWall(double wallHeight) {
        return position.y <= 0 || position.y >= wallHeight;
    }

    private boolean touchedPaddle(Paddle paddle) {
        return position.y > paddle.getHeight() && position.y < paddle.getHeight() + paddle.getLength();
    }

    private double getVelocityY(Paddle paddle) {
        Double[][] hitboxes = new Double[5][2];
        for (int i = 0; i < hitboxes.length; i++) {
            hitboxes[i][0] = (paddle.getLength() / hitboxes.length) * i + paddle.getHeight();
            hitboxes[i][1] = (paddle.getLength() / hitboxes.length) * (i + 1) + paddle.getHeight();
            if (i == 0) hitboxes[i][0] -= radius;
            else if (i == hitboxes.length - 1) hitboxes[i][1] += radius;
        }

        for (int i = 0; i < hitboxes.length; i++) {
            if (position.y > hitboxes[i][0] && position.y < hitboxes[i][1])
                switch (i) {
                    case 0:
                        return -4;
                    case 4:
                        return 4;
                    case 1:
                        return -2;
                    case 3:
                        return 2;
                    case 2:
                        return 0;
                }
        }
        return angleOffset;
    }

    public interface OnPointListener {
        void playerOne();

        void playerTwo();
    }

}
