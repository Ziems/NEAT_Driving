package com.ziems.neat_driving.game.states.world;

import org.newdawn.slick.Image;

/**
 * Created by noahziems on 5/7/16.
 */
public class Tile {

    public static final int SIZE = 32;
    public static final int SMALL_SIZE = 16;
    public static final float SCALE = (SIZE/SMALL_SIZE);

    byte id;
    boolean solid;
    Image image;

    public Tile(byte id, boolean solid, Image image){
        this.id = id;
        this.solid = solid;
        this.image = image;
    }

    public byte getId(){
        return id;
    }

    public boolean isSolid(){
        return solid;
    }

    public Image getImage(){
        return image;
    }
}
