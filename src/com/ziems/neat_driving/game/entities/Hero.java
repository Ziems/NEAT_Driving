package com.ziems.neat_driving.game.entities;

import com.ziems.neat_driving.game.Resources;
import com.ziems.neat_driving.game.Window;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.tiled.TiledMap;

/**
 * Created by noahziems on 5/7/16.
 */
public class Hero extends Entity {

    float speed = 0.3f;

    @Override
    public void init() {
        x = 0;
        y = 0;
        height = 16 * 3;
        width = 16 * 3;
        try {
            image = Resources.getImage("hero");
        }catch(Exception e){}
    }

    @Override
    public void update(GameContainer gc, int delta) {
        Input input = gc.getInput();
        if(input.isKeyDown(Input.KEY_W))
            y -= speed * delta;
        if(input.isKeyDown(Input.KEY_S))
            y += speed * delta;
        if(input.isKeyDown(Input.KEY_A))
            x -= speed * delta;
        if(input.isKeyDown(Input.KEY_D))
            x += speed * delta;
    }

    public void render(GameContainer gc, Graphics g){
        if(image != null)
            image.draw(Window.WIDTH/2 - getHalfWidth(), Window.HEIGHT/2 - getHalfHeight(), width, height, color);
    }

    public int getMapX(TiledMap tiledMap){
        return (int)(x / tiledMap.getTileWidth());
    }

    public int getMapY(TiledMap tiledMap){
        return (int)(y / tiledMap.getTileHeight());
    }
}
