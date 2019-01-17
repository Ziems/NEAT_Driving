package com.ziems.neat_driving.game;

import com.ziems.neat_driving.game.states.Menu;
import com.ziems.neat_driving.game.states.Play;
import com.ziems.neat_driving.game.states.State;
import com.ziems.neat_driving.game.states.world.World;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 * Created by noahziems on 3/30/16.
 */
public class Game extends StateBasedGame {

    public Game(String gameName){
        super(gameName);
        this.addState(new Menu());
        this.addState(new Play());
    }

    public void initStatesList(GameContainer gc) throws SlickException{
        new Resources();

        try {
           // World.load("res/test_map.csv");
        }catch(Exception e){
            e.printStackTrace();
        }

        this.getState(State.MENU).init(gc, this);
        this.getState(State.PLAY).init(gc, this);
        this.enterState(State.MENU);

    }

    public static void main(String[] args){
        AppGameContainer appgc;
        try{
            appgc = new AppGameContainer(new Game(Window.NAME));
            appgc.setDisplayMode(Window.WIDTH, Window.HEIGHT, false);
            appgc.setShowFPS(false);
            appgc.setAlwaysRender(false);
            //appgc.setVSync(true);//Less efficent. No more tearing
            appgc.setMaximumLogicUpdateInterval(30);
            appgc.setTargetFrameRate(15);
            appgc.start();
        }catch(SlickException e){
            e.printStackTrace();
        }
    }
}
