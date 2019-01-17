package com.ziems.neat_driving.game.entities;

import com.ziems.neat_driving.game.states.world.World;
import com.ziems.neat_driving.game.util.Box;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

/**
 * Created by noahziems on 5/7/16.
 */
public abstract class Entity extends Box {

    Image image;
    Color color;

    public Entity(){
        init();
    }

    public abstract void init();

    public void render(GameContainer gc, Graphics g){
        if(image != null)
            image.draw(x, y, width, height, color);
    }

    public abstract void update(GameContainer gc, int delta);

    public boolean testLeft(){
        return World.hitTest(x, getCenterY());
    }

    public boolean testRight(){
        return World.hitTest(getEndX(), getCenterY());
    }

    public boolean testUp(){
        return World.hitTest(x, y);
    }

    public boolean testDown(){
        return World.hitTest(getCenterX(), getEndY());
    }

}
