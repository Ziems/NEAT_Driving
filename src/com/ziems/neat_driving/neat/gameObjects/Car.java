package com.ziems.neat_driving.neat.gameObjects;

import com.ziems.neat_driving.game.states.State;
import com.ziems.neat_driving.game.states.world.Map;
import com.ziems.neat_driving.neat.Neat;
import com.ziems.neat_driving.neat.pool.species.genome.Genome;
import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.*;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;

/**
 * Created by noahziems on 4/3/16.
 */
public class Car {

    final float MAX_SPEED = 2;
    final float ACCELERATION = 0.1f;
    final float FRICTION_ACCELERATION = ACCELERATION/10;
    final float ROTATION_SPEED = 0.06f;
    final float WHISKER_LENGTH = 75f;

    final float WIDTH = 30;
    final float HEIGHT = 15;

    private float x;
    private float y;
    private float speed;
    private float rotation;//RADIANS

    private long startTime;

    private Polygon shape;
    private Whisker whiskerLeft;
    private Whisker whiskerCenterLeft;
    private Whisker whiskerCenter;
    private Whisker whiskerCenterRight;
    private Whisker whiskerRight;

    private float[] controllers;

    public Car(){
        x = 300;
        y = 230;
        speed = 0;
        rotation = 0;
        shape = new Polygon();
        shape.addPoint(x, y);
        shape.addPoint(x + WIDTH, y);
        shape.addPoint(x + WIDTH, y + HEIGHT);
        shape.addPoint(x, y + HEIGHT);
        whiskerLeft = new Whisker(this, -(float)(Math.PI/4), WHISKER_LENGTH);
        whiskerCenterLeft = new Whisker(this, -(float)(Math.PI/8), WHISKER_LENGTH * 1.5f);
        whiskerCenter = new Whisker(this, 0, WHISKER_LENGTH * 2f);
        whiskerCenterRight = new Whisker(this, (float)(Math.PI/8), WHISKER_LENGTH * 1.5f);
        whiskerRight = new Whisker(this, (float)(Math.PI/4), WHISKER_LENGTH);
        startTime = System.currentTimeMillis();
    }

    public float[] getInputs(){
        return new float[]{speed/MAX_SPEED, whiskerLeft.getValue(), whiskerCenterLeft.getValue(), whiskerCenter.getValue(), whiskerCenterRight.getValue(), whiskerRight.getValue()};
    }

    public void update(float[] inputs, int delta){
        controllers = inputs;
        float timeStep = (float)delta/30;
        if(speed > 0) {
            speed += -FRICTION_ACCELERATION * timeStep;
            if(speed < 0)
                speed = 0;
        }
        float anchorX = x + (WIDTH/2);
        float anchorY = y + (HEIGHT/2);
        // A
        shape = (Polygon) shape.transform(Transform.createRotateTransform(ROTATION_SPEED * (inputs[0] - 0.5f) * 2, anchorX, anchorY));
        rotation += ROTATION_SPEED * (inputs[0] - .5f) * 2;
        /*if(inputs[1] > 0){ // D
            if(speed > 0) {
                shape = (Polygon) shape.transform(Transform.createRotateTransform(ROTATION_SPEED * inputs[1], anchorX, anchorY));
                rotation += ROTATION_SPEED * inputs[1];
            }
        }*/
        // W
        if(speed < MAX_SPEED)
            speed += ACCELERATION * timeStep * inputs[1];
        /*if(inputs[3] > .5){ // S
            //Break??
        }
        */
        //Whiskers
        float cX = shape.getCenterX();
        float cY = shape.getCenterY();

        whiskerLeft.update(cX, cY, rotation);
        whiskerCenterLeft.update(cX, cY, rotation);
        whiskerCenter.update(cX, cY, rotation);
        whiskerCenterRight.update(cX, cY, rotation);
        whiskerRight.update(cX, cY, rotation);
        //Car controlling
        float x1 = (float)Math.cos(rotation) * speed;
        float y1 = (float)Math.sin(rotation) * speed;
        shape = (Polygon)shape.transform(Transform.createTranslateTransform(x1, y1));
        x += x1;
        y += y1;
    }


    public void update(StateBasedGame game, int delta){
        Input input = game.getContainer().getInput();
        float timeStep = (float)delta/60;
        if(speed > 0) {
            speed += -FRICTION_ACCELERATION * timeStep;
            if(speed < 0)
                speed = 0f;
        }
        float anchorX = x + (WIDTH/2);
        float anchorY = y + (HEIGHT/2);
        if(input.isKeyDown(Keyboard.KEY_A)){
            if(speed > 0) {
                shape = (Polygon) shape.transform(Transform.createRotateTransform(-ROTATION_SPEED, anchorX, anchorY));
                rotation -= ROTATION_SPEED;
            }
        }
        if(input.isKeyDown(Keyboard.KEY_D)){
            if(speed > 0) {
                shape = (Polygon) shape.transform(Transform.createRotateTransform(ROTATION_SPEED, anchorX, anchorY));
                rotation += ROTATION_SPEED;
            }
        }
        if(input.isKeyDown(Keyboard.KEY_W)){
            if(speed < MAX_SPEED)
                speed += ACCELERATION * timeStep;
        }
        if(input.isKeyDown(Keyboard.KEY_S)){

        }
        //Whiskers
        float cX = shape.getCenterX();
        float cY = shape.getCenterY();

        whiskerLeft.update(cX, cY, rotation);
        whiskerCenterLeft.update(cX, cY, rotation);
        whiskerCenter.update(cX, cY, rotation);
        whiskerCenterRight.update(cX, cY, rotation);
        whiskerRight.update(cX, cY, rotation);
        //Car controlling
        float x1 = (float)Math.cos(rotation) * speed;
        float y1 = (float)Math.sin(rotation) * speed;
        shape = (Polygon)shape.transform(Transform.createTranslateTransform(x1, y1));
        x += x1;
        y += y1;
    }

    public void render(Graphics g, StateBasedGame sbg){
        g.setColor(Color.cyan);
        float x = shape.getCenterX();
        float y = shape.getCenterY();
        g.fill(shape);

        whiskerLeft.render(g, x, y);
        whiskerCenterLeft.render(g, x, y);
        whiskerCenter.render(g, x, y);
        whiskerCenterRight.render(g, x, y);
        whiskerRight.render(g, x, y);

        g.setColor(Color.black);
        float mouseX = sbg.getContainer().getInput().getMouseX();
        float mouseY = sbg.getContainer().getInput().getMouseY();
        g.drawString("(" + mouseX + ", " + mouseY + ")", 10, 10);

        float[] inputs = getInputs();
        g.setColor(Color.white);
        if(inputs != null) {
            g.drawString("Inputs:", 0, 320);
            for (int i = 0; i < inputs.length; i++) {
                g.drawString("[" + i + "]:" + inputs[i], 0, 331 + 12 * i);
            }
        }
        if(controllers != null) {
            g.drawString("Outputs:", 150, 320);
            for (int i = 0; i < controllers.length; i++) {
                g.drawString("[" + i + "]:" + controllers[i], 150, 331 + 12 * i);
            }
        }
    }

    public boolean isCrashed(TiledMap map){
        if(Map.tiles[0] != null) {
            for (Rectangle rect : Map.tiles[0]) {
                if (shape.intersects(rect)) {
                    System.out.println("COLLISION!");
                    return true;
                }
            }
        }
        return System.currentTimeMillis() - startTime > 10 * 1000 || (System.currentTimeMillis()-startTime > 100 && x == 300 && y == 230) || x > 640 || y > 323;
    }

    public int getFitness(TiledMap map) {
        if(Map.tiles[1] != null) {
            for (Rectangle rect : Map.tiles[1]) {
                if (shape.intersects(rect))
                    return (int)Math.sqrt((y - 230) * (y-230) + (x - 300) * (x-300));
            }
        }
        if(System.currentTimeMillis() - startTime > 1000) {
            if (Map.tiles[2] != null) {
                for (Rectangle rect : Map.tiles[2]) {
                    if (shape.intersects(rect))
                        return (int) Math.sqrt((y - 227) * (y - 227) + (x - 577) * (x - 577)) + 280;
                }
            }
        }
        if(System.currentTimeMillis() - startTime > 2000) {
            if (Map.tiles[3] != null) {
                for (Rectangle rect : Map.tiles[3]) {
                    if (shape.intersects(rect))
                        return (int) Math.sqrt((y - 99) * (y - 99) + (x - 577) * (x - 577)) + 280 + 128;
                }
            }
        }
        if(Map.tiles[4] != null) {
            for (Rectangle rect : Map.tiles[4]) {
                if (shape.intersects(rect))
                    return (int)Math.sqrt((y - 99) * (y-99) + (x - 577) * (x-577)) + 280 + 128;
            }
        }
        if(System.currentTimeMillis() - startTime > 5000) {
            if (Map.tiles[5] != null) {
                for (Rectangle rect : Map.tiles[5]) {
                    if (shape.intersects(rect))
                        return (int) Math.sqrt((y - 99) * (y - 99) + (x - 65) * (x - 65)) + 280 + 128 + 512;
                }
            }
        }

        /*if(System.currentTimeMillis() - startTime > 6000) {
            if (Map.tiles[6] != null) {
                for (Rectangle rect : Map.tiles[6]) {
                    if (shape.intersects(rect))
                        return (int) Math.sqrt((y - 225) * (y - 225) + (x - 65) * (x - 65)) + 280 + 128 + 512 + 125;
                }
            }
        }*/

        return 1;
    }
}
