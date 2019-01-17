package com.ziems.neat_driving.neat.gameObjects;

import com.ziems.neat_driving.game.states.world.Map;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;

/**
 * Created by noahziems on 3/7/17.
 */
public class Whisker {

    private Car car;
    private float offset;
    private float whiskerLength;
    private float collision = 0.0f;
    private float x1 = -1;
    private float y1 = -1;

    public Whisker(Car car, float offset, float whiskerLength){
        this.car = car;
        this.offset = offset;
        this.whiskerLength = whiskerLength;
    }

    public void render(Graphics g, float x, float y) {

        if(collision > 0.66f)//Try to do gradient in the future
            g.setColor(Color.green);
        else if(collision > 0.33f)
            g.setColor(Color.orange);
        else
            g.setColor(Color.red);
        Line line = new Line(x, y, x1, y1);
        g.draw(line);
    }

    public void update(float x, float y, float rotation) {
        collision = 0.0f;
        x1 = (float)Math.cos(rotation + offset) * whiskerLength + x;
        y1 = (float)Math.sin(rotation + offset) * whiskerLength + y;
        if(x1 > 640 || y1 > 323 || x < 0 || y < 0) {
            collision = 1f;
            return;
        }
        if(Map.tiles[0] != null) {
            Line line = new Line(x, y, x1, y1);
            for (Rectangle rect : Map.tiles[0]) {
                if (rect.intersects(line)) {
                    collision += 0.3f;
                }
            }
        }
    }

    public float getValue(){
        return collision;
    }
}
