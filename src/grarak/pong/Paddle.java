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

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Created by willi on 07.05.15.
 */
public class Paddle extends DrawObject {

    private final Main.PLAYER player;

    private double height;
    private double offset;
    private double length;
    private double thickness;

    private boolean moveUp;
    private boolean moveDown;

    public Paddle(GraphicsContext graphicsContext, Main.PLAYER player) {
        super(graphicsContext);
        this.player = player;
        offset = 0;
    }

    @Override
    public void draw(double x, double y) {

        length = y / 4;

        double position = offset;

        if (position < 0) position = -position;
        if (position + length / 2 <= y / 2) {
            if (moveDown) offset += y / 100;
            if (moveUp) offset -= y / 100;
        } else {
            if (offset <= 0) offset = -(y / 2 - length / 2);
            else offset = y / 2 - length / 2;
        }

        double w = 0;
        double h = y / 2 - length / 2 + offset;

        thickness = x / 80;
        switch (player) {
            case ONE:
                w = 10;
                break;
            case TWO:
                w = x - 10 - thickness;
                break;
        }

        getGraphicsContext().setFill(Color.BLUE);
        getGraphicsContext().fillRect(w, (height = h), thickness, length);
    }

    public void moveUp() {
        moveUp = true;
    }

    public void moveDown() {
        moveDown = true;
    }

    public void stopMoving() {
        moveUp = false;
        moveDown = false;
    }

    public void reset() {
        offset = 0;
    }

    public double getHeight() {
        return height;
    }

    public double getLength() {
        return length;
    }

    public double getWidth() {
        return thickness + 10;
    }

}
