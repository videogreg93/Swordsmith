package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeType;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.Timer;

/**
 * Created by Gregory on 8/27/2016.
 */
public class Hud {
  static private Sprite sprite;
  static private BitmapFont fontTimer;
  static private BitmapFont fontPaused;
  static float elapsedTime;
  static int minutes;
  static int seconds;
  static String time;
  public static Sprite[] weaponSprites;
  public static Sprite orderOutline;
  private static Sprite redCross;

  private static boolean showWaveText;
  private static BitmapFont fontWave;


    private Hud() {


    }

    public static void create() {
        weaponSprites = new Sprite[15];

        // Init the weapon sprites

        weaponSprites[3] = new Sprite(new Texture("Tiles/orders/copperSword.png"));
        weaponSprites[4] = new Sprite(new Texture("Tiles/orders/copperShield.png"));
        weaponSprites[5] = new Sprite(new Texture("Tiles/orders/copperKatana.png"));
        weaponSprites[6] = new Sprite(new Texture("Tiles/orders/ironSword.png"));
        weaponSprites[7] = new Sprite(new Texture("Tiles/orders/ironShield.png"));
        weaponSprites[8] = new Sprite(new Texture("Tiles/orders/ironKatana.png"));
        weaponSprites[9] = new Sprite(new Texture("Tiles/orders/goldSword.png"));
        weaponSprites[10] = new Sprite(new Texture("Tiles/orders/goldShield.png"));
        weaponSprites[11] = new Sprite(new Texture("Tiles/orders/goldKatana.png"));
        weaponSprites[12] = new Sprite(new Texture("Tiles/orders/copperKatana.png"));
        weaponSprites[13] = new Sprite(new Texture("Tiles/orders/ironKatana.png"));
        weaponSprites[14] = new Sprite(new Texture("Tiles/orders/goldKatana.png"));
        orderOutline = new Sprite(new Texture("Tiles/orders/blankOrder.png"));
        redCross = new Sprite(new Texture("Tiles/cross.png"));

        sprite = new Sprite(new Texture("Tiles/Hud2.png"));
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Fonts/Montserrat-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 25;
        fontTimer = generator.generateFont(parameter);
        parameter.size = 100;
        parameter.color = Color.BLACK;
        fontPaused = generator.generateFont(parameter);

        parameter.color = Color.WHITE;

        fontWave = generator.generateFont(parameter);


        elapsedTime = 0;
        minutes = 0;
        seconds = 0;

        showWaveText = true;
    }

    public static void update() {
        // make time advance
        time = getFormatedTime();
    }

    public static void draw(SpriteBatch batch) {
        batch.draw(sprite.getTexture(), 0, 736);

        // Draw the time
        fontTimer.draw(batch, time, 60, 796);

        // Draw the current wave text, if need be
        if (showWaveText) {
            fontWave.draw(batch, "Wave " + OrderSystem.getCurrentWave() + " begin", (Gdx.graphics.getWidth() / 2) - 250, Gdx.graphics.getHeight() / 2);
        }

        // draw the orders
        for (int i = 0; i < OrderSystem.allOrders.size(); i++) { // 1168 - 100 * i
            batch.draw(orderOutline.getTexture(), (1168 - 100 * i), 745 );
           for (int x = 0; x < OrderSystem.allOrders.get(i).getWeapons().size(); x++) {
               // We draw each weapon of the order, starting from the top left
               int weaponOrdinal = OrderSystem.allOrders.get(i).getWeapons().get(x).ordinal();
               batch.draw(weaponSprites[weaponOrdinal].getTexture(), (1170 - 100 * i), 750 );


           }

            OrderSystem.allOrders.get(i).drawTimer(batch, (1168 - 100 * i),750);

        }

        for (int i = 0; i < Player.LIVESDEFAULT - Player.LIVES; i++)
        batch.draw(redCross,209 + (i*95), 746);

    }

    public static void drawPaused(SpriteBatch batch) {
        float x = (Gdx.graphics.getWidth()/2) - 150;
        float y = (Gdx.graphics.getHeight()/2) + 100;
        fontPaused.draw(batch,"PAUSED", x ,y);
    }

    public static String getFormatedTime() {
        elapsedTime += Gdx.graphics.getDeltaTime();
        minutes = ((int)elapsedTime) / 60;
        seconds = ((int)elapsedTime) % 60;

        String minutesString;
        String secondsString;

        if (minutes < 10)
            minutesString = "0" + minutes;
        else
            minutesString = minutes + "";
        if (seconds < 10)
            secondsString = "0" + seconds;
        else
            secondsString = seconds + "";



        return minutesString + ":" + secondsString;
    }

    public static float getElapsedTime() {
        return elapsedTime;
    }

    public static void resetElapsedTime() {

        elapsedTime = ((int)(OrderSystem.getCurrentWave() / 5)) * 5;
    }

    public static int getSeconds() {
        return seconds;
    }

    public static void drawFinalTime(SpriteBatch batch, String finalTime) {
        fontTimer.draw(batch, "Time: \n" + finalTime,1000,700 ); // TODO so many magic numbers
        fontTimer.draw(batch, "Wave " + OrderSystem.getCurrentWave(),800,700 );
    }

    public static void drawWaveText() {
        showWaveText = true;
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {

                               showWaveText = false;
                           }
                       }
                , 3
                , 0, 0
        );
    }
}
