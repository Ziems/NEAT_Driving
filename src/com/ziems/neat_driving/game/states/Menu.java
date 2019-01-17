package com.ziems.neat_driving.game.states;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/**
 * Created by noahziems on 3/30/16.
 */
public class Menu extends BasicGameState {

    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException{

    }

    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException{

    }

    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException{
        sbg.enterState(State.PLAY);
    }

    public int getID(){
        return State.MENU;
    }

}
