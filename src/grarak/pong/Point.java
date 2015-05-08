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
 * Created by willi on 08.05.15.
 */
public class Point extends DrawObject {

    private final Main.PLAYER player;
    protected int point = 0;

    public Point(GraphicsContext graphicsContext, Main.PLAYER player) {
        super(graphicsContext);
        this.player = player;
    }

    @Override
    public void draw(double width, double height) {
        double w = 0;
        switch (player) {
            case ONE:
                w = width / 4;
                break;
            case TWO:
                w = width - width / 4;
                break;
        }
        getGraphicsContext().setFill(Color.WHITE);
        getGraphicsContext().fillText(String.valueOf(point), w, 40);
    }

}
