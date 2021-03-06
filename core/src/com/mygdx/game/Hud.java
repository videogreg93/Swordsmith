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
import com.mygdx.game.Upgrades.Build1;
import com.mygdx.game.Upgrades.Order1;
import com.mygdx.game.Upgrades.Speed1;

import java.util.ArrayList;

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

  // Upgrades
  private static Sprite upgradeScreenSprite;
  private static ArrayList<Upgrade> availableUpgrades;
  private static Sprite upgradeOutline;



  private static Sprite[] upgradeSprites;


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

        // Init the available upgrades
        upgradeScreenSprite = new Sprite(new Texture("Tiles/upgrades/upgradeScreen.png"));
        upgradeOutline = new Sprite(new Texture("Tiles/upgrades/outline.png"));
        upgradeOutline.setX((int) ((Gdx.graphics.getWidth() / 2) - upgradeScreenSprite.getWidth() / 2) + (int) (upgradeScreenSprite.getWidth() * 0.03));
        upgradeOutline.setY((int) ((Gdx.graphics.getHeight() / 2) - upgradeScreenSprite.getHeight() / 2) + (int) (upgradeScreenSprite.getHeight() * 0.58));
        availableUpgrades = new ArrayList<Upgrade>();

        availableUpgrades.add(new Speed1());
        availableUpgrades.add(new Build1());
        availableUpgrades.add(new Order1());

        upgradeSprites = new Sprite[3];
        upgradeSprites[Upgrade.UPGRADE.SPEED1.ordinal()] = new Sprite(new Texture("Tiles/upgrades/speed1.png"));
        upgradeSprites[Upgrade.UPGRADE.BUILD1.ordinal()] = new Sprite(new Texture("Tiles/upgrades/build1.png"));
        upgradeSprites[Upgrade.UPGRADE.ORDER1.ordinal()] = new Sprite(new Texture("Tiles/upgrades/order1.png"));
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

        // Draw the money
        fontTimer.draw(batch, Player.money + "$" , 510,800);

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

    public static void drawUpgrade(SpriteBatch batch) {
        int x = (int)((Gdx.graphics.getWidth()/2) - upgradeScreenSprite.getWidth()/2);
        int y = (int)((Gdx.graphics.getHeight()/2) - upgradeScreenSprite.getHeight()/2);
        batch.draw(upgradeScreenSprite.getTexture(), x, y);
        // draw the upgrades
        x += upgradeScreenSprite.getWidth() *0.03;
        y += upgradeScreenSprite.getHeight() * 0.58;
        for (int i = 0; i < availableUpgrades.size(); i++) {
            int ordinal = availableUpgrades.get(i).type.ordinal();
            batch.draw(upgradeSprites[ordinal].getTexture(), x + (i * (upgradeSprites[0].getWidth() + 15)), y);
        }
        batch.draw(upgradeOutline.getTexture(), upgradeOutline.getX(), upgradeOutline.getY());
    }

    public static void moveUpgradeOutlineHorizontal(boolean moveRight) {
        int x = (int) upgradeSprites[0].getWidth() + 15;
        if (!moveRight)
            x = x * -1;
        // We gotta stay in the right values
        int lowerBound = (int)((Gdx.graphics.getWidth()/2) - upgradeScreenSprite.getWidth()/2) + (int)(upgradeScreenSprite.getWidth() *0.03);
        int higherBound = lowerBound + (int)((availableUpgrades.size()) * upgradeSprites[0].getWidth() + 15 );
        if (upgradeOutline.getX() + x >= lowerBound && upgradeOutline.getX() + x <= higherBound) {
            upgradeOutline.setX(upgradeOutline.getX() + x);
            SwordSmith.chooseMenu.play();
        }
    }

    public static Upgrade chooseUpgrade() {
        int upgradePosition = (int) upgradeOutline.getX() - (int)((Gdx.graphics.getWidth()/2) - upgradeScreenSprite.getWidth()/2) + (int)(upgradeScreenSprite.getWidth() *0.03);
        upgradePosition = (int)(upgradePosition/(upgradeSprites[0].getWidth() + 15));
        return availableUpgrades.remove(upgradePosition);
    }
}
