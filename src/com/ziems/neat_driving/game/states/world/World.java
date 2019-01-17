package com.ziems.neat_driving.game.states.world;

import com.ziems.neat_driving.game.Resources;
import com.ziems.neat_driving.game.Window;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 * Created by noahziems on 5/7/16.
 */
public class World {

    public static String MAP_TILESET_KEY = "tileset";

    public static int WIDTH;
    public static int HEIGHT;
    public static int MAP_WIDTH = 100;//in tiles
    public static int MAP_HEIGHT = 100;

    private static byte[][] tileTypes;
    private static Tile[] tiles;

    public static void load(String csvFile) throws Exception {
        System.out.println("\n----------LOADING TILE SET----------");
        parseSpriteSheet();
        System.out.println("SUCCESSFULLY LOADED TILE SET\n");
        System.out.println("----------LOADING CSV MAP----------");
       loadCsvMap(csvFile);
    }

    public static void loadCsvMap(String csvFile) {
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        try {
            br = new BufferedReader(new FileReader(csvFile));
            int lineNumber = 0;
            tileTypes = new byte[MAP_WIDTH][MAP_HEIGHT];
            while ((line = br.readLine()) != null) {
                // use comma as separator
                String[] tiles = line.split(cvsSplitBy);
                for (int i = 0; i < tiles.length; i++) {
                    MAP_WIDTH = tiles.length;
                    byte tileType = Byte.decode(tiles[i]);
                    tileTypes[i][lineNumber] = tileType;
                }
                lineNumber++;
                MAP_HEIGHT = lineNumber;
            }
        } catch (Exception e) {
            System.out.println("CSV MAP COULD NOT BE FOUND: " + csvFile);
        }
        System.out.println("MAP WIDTH: " + MAP_WIDTH);
        System.out.println("MAP HEIGHT: " + MAP_HEIGHT);
    }

    public static void parseSpriteSheet(){
        boolean isSolid = true;//TODO: NEED TO CHANGE THIS
        SpriteSheet sheet = Resources.getSprite(MAP_TILESET_KEY);
        tiles = new Tile[sheet.getHorizontalCount() * sheet.getVerticalCount()];
        for(int x = 0; x < sheet.getHorizontalCount(); x++) {
            for (int y = 0; y < sheet.getVerticalCount(); y++) {
                byte index = (byte)(y * sheet.getHorizontalCount() + x);
                tiles[index] = new Tile(index, isSolid, getSpriteImage(index));
            }
        }
    }

    public static void render(float xRender, float yRender, Graphics g){
        int offset = 2;
        int xStart = (int)(xRender / Tile.SMALL_SIZE) - offset;
        int yStart = (int)(yRender / Tile.SMALL_SIZE) - offset;
        int xEnd = (Window.WIDTH / Tile.SMALL_SIZE) + xStart + (offset * 2);
        int yEnd = (Window.HEIGHT / Tile.SMALL_SIZE) + yStart + (offset * 2);
        for(int x = xStart; x < xEnd; x++){
            for(int y = yStart; y < yEnd; y++){
                if(x % 3 == 0)
                    g.drawString(x + "," + y, x * Tile.SIZE, y * Tile.SIZE);
                if(inBounds(x, y)) {
                    byte tileType = tileTypes[x][y];
                    if (tileType != -1) {
                        Image tile = tiles[tileType].getImage();
                        tile.draw(x * Tile.SIZE, y * Tile.SIZE, Tile.SIZE, Tile.SIZE);
                    }
                }
            }
        }
    }

    private static Image getSpriteImage(int index){
        if(index == -1) return null;

        SpriteSheet sheet = Resources.getSprite(MAP_TILESET_KEY);
        int horizontal = sheet.getHorizontalCount();

        int y = index / horizontal;
        int x = index % horizontal;

        return sheet.getSubImage(x, y);

    }

    private static boolean inBounds(int x, int y){
        return x >= 0 && y>= 0 && x <= WIDTH && y <= HEIGHT;
    }

    private static boolean solidTile(int x, int y){
        return(inBounds(x, y) && tileTypes[x][y] != -1);
    }

    public static boolean hitTest(float x, float y){
        int xPoint = (int)((x/Tile.SCALE) %  Tile.SMALL_SIZE);
        int yPoint = (int)((y/Tile.SCALE) % Tile.SMALL_SIZE);
        int xTile = (int)(x/Tile.SIZE);
        int yTile = (int)(y/Tile.SIZE);

        if (solidTile(xTile, yTile))
            return tiles[tileTypes[xTile][yTile]].getImage().getColor(xPoint, yPoint).a > 0;
        return false;
    }
}
