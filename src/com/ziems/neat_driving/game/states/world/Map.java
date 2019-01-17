package com.ziems.neat_driving.game.states.world;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.tiled.TiledMap;

import java.util.ArrayList;

/**
 * Created by noahziems on 5/8/16.
 */
public class Map {

    public static TiledMap tiledMap;

    // For collision detection, we have a list of Rectangles you can use to test against
    public static ArrayList<Rectangle>[] tiles;

    public static TiledMap init(String tmxFile){
        try {
            tiledMap = new TiledMap(tmxFile);
            tiles = new ArrayList[7];

            for(int z = 0; z < tiles.length; z++) {
                tiles[z] = new ArrayList<>();
                for (int i = 0; i < tiledMap.getWidth(); i++) {
                    for (int j = 0; j < tiledMap.getHeight(); j++) {
                        System.out.println("(" + z + ", " + i + ", " + j + ")");
                        // Read a Tile
                        int tileID = tiledMap.getTileId(i, j, z);
                        // If the value of the Property is "true"...
                        if (tileID != 0) {
                            // And create the collision Rectangle
                            tiles[z].add(new Rectangle(i * 32, j * 32, 32, 32));
                        }
                    }
                }
            }
        }catch(Exception e){System.out.println("MAP COULD NOT BE LOCATED AT: " + tmxFile);
        e.printStackTrace();}
        return tiledMap;
    }

    public static TiledMap getTiledMap(){
        return tiledMap;
    }
}
