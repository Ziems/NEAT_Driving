package com.ziems.neat_driving.game;

import com.ziems.neat_driving.game.states.world.Tile;
import com.ziems.neat_driving.game.states.world.World;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.SpriteSheet;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by noahziems on 5/7/16.
 */
public class Resources {

    private static Map<String, Image> images;
    private static Map<String, SpriteSheet> sprites;
    private static Map<String, Sound> sounds;

    public Resources(){
        images = new HashMap<String, Image>();
        sprites = new HashMap<String, SpriteSheet>();
        sounds = new HashMap<String, Sound>();

        try {
            //sprites.put(World.MAP_TILESET_KEY, loadSprite("res/16x16Tiles.png", Tile.SMALL_SIZE, Tile.SMALL_SIZE));
            //images.put("hero", loadSprite("res/hero.png", 32, 32));
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static Image loadImage(String path) throws SlickException{
        return new Image(path, false, Image.FILTER_NEAREST);
    }

    public static SpriteSheet loadSprite(String path, int tileWidth, int tileHeight) throws SlickException{
        return new SpriteSheet(loadImage(path), tileWidth, tileHeight);
    }

    public static Image getImage(String key){
        return images.get(key);
    }

    public static Image getSpriteImage(String key, int x, int y){
        return sprites.get(key).getSubImage(x, y);
    }

    public static SpriteSheet getSprite(String key) {
        return sprites.get(key);
    }

    public static Sound getSound(String key){
        return sounds.get(key);
    }
}
