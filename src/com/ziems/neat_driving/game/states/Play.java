package com.ziems.neat_driving.game.states;

import com.ziems.neat_driving.game.Window;
import com.ziems.neat_driving.game.entities.Entity;
import com.ziems.neat_driving.game.entities.Hero;
import com.ziems.neat_driving.game.states.world.Map;
import com.ziems.neat_driving.game.states.world.World;
import com.ziems.neat_driving.neat.Neat;
import com.ziems.neat_driving.neat.gameObjects.Car;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;

import java.util.ArrayList;

/**
 * Created by noahziems on 3/30/16.
 */
public class Play extends BasicGameState {

    private ArrayList<Entity> entities;
    Car car;
    TiledMap map;
    Neat neat;

    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        entities = new ArrayList<>();
        car = new Car();
        map = Map.init("/res/map.tmx");
        neat = new Neat(6, 2);
    }

    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException{
        g.resetFont();
        int horizontalMapTiles = Window.WIDTH/map.getTileWidth() - 1;
        int verticalMapTiles = Window.HEIGHT/map.getTileHeight() - 1;
        /*
        //Player x must be inverted for scrolling
        int xTile = player.getMapX(map) - horizontalMapTiles/2;
        int yTile = player.getMapY(map) - verticalMapTiles/2;
        int offSetX = -(int)(player.x % map.getTileWidth());
        int offsetY = -(int)(player.y % map.getTileHeight());
        System.out.println("Map Dimensions: (" + horizontalMapTiles + ", " + verticalMapTiles + ")");
        */
        map.render(0, 0, 0, 0, horizontalMapTiles, verticalMapTiles);
        car.render(g, sbg);
        //g.drawString("Player Pos: (" + car. + ", " + player.y + ")", 20, 20);
    }

    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException{
        /*for(Entity e: entities)
            e.update(gc, delta);*/
        ///car.update(sbg, delta);
        if(!car.isCrashed(map)) {
            float[] inputs = car.getInputs();
            float[] outputs = neat.runCurrentNetwork(inputs);
            /*for (int i = 0; i < outputs.length; i++) {
                System.out.println("Outputs[" + i + "]: " + outputs[i]);
            }*/
            car.update(outputs, delta);
        } else {
            System.out.println("Cycling car");
            System.out.println("\tFitness[" + car.getFitness(map) + "]");
            neat.cycleGenome(car.getFitness(map));
            car = new Car();
        }
    }

    public int getID(){
        return State.PLAY;
    }
}
