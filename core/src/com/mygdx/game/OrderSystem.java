package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Gregory on 8/27/2016.
 */
public class OrderSystem {

    public static ArrayList<Order> allOrders;
    public static boolean canCreateNewOrder;
    private static int timer;
    private static int TIMERDEFAULTVALUE = 600;
    private static Random random;

    private static Sound newOrder;
    private static Sound loseLife;

    private static float difficulty;

    // Timer graphics
    public static Texture timerOutline;
    public static NinePatch timerSprite;


    private OrderSystem() {

    }

    public static void create(float diff) {
        random = new Random();
        allOrders = new ArrayList<Order>();
        canCreateNewOrder = false;
        timer = TIMERDEFAULTVALUE;
        newOrder = Gdx.audio.newSound(Gdx.files.internal("Sounds/New_Order.wav"));
        loseLife = Gdx.audio.newSound(Gdx.files.internal("Sounds/Lose_Life.wav"));
        difficulty = diff;

        timerOutline = new Texture("Tiles/orders/timerOutline.png");
        timerSprite = new NinePatch(new Texture("Tiles/orders/timer.png"));


    }

    public static void createNewOrder() {
       allOrders.add(new OrderSystem().new Order());
        newOrder.play();
    }

    public static void update() {
        timer --;
        if (timer <= 0) {
            createNewOrder();
            timer = TIMERDEFAULTVALUE + (random.nextInt(480) - 240 - (int)(Hud.getElapsedTime()*difficulty)) ;
        }

        for (int i = 0; i < allOrders.size(); i++) {
            Order order = allOrders.get(i);
            order.timeToComplete--;
            if (order.timeToComplete <= 0) {
                allOrders.remove(order);
                System.out.println("Order took too long, life lost");
                Player.removeLife();
                loseLife.play();

            }
        }
    }


    public class Order {
        private ArrayList <Player.Weapon> weapons;
        public float timeToComplete;
        public int timerMaxValue;

        public Order() {
            weapons = new ArrayList<Player.Weapon>();
            Random random = new Random();
            int weaponValue = random.nextInt(9) + 3;
            if (weaponValue % 3 == 2) { // Change katanas to complete katanas
               if (weaponValue == 5)
                   weaponValue = 12;
               else if (weaponValue == 8)
                    weaponValue = 13;
               else if (weaponValue == 11)
                    weaponValue = 14;
            }
            Player.Weapon temp = Player.Weapon.values()[weaponValue];
            weapons.add(temp);

            timeToComplete = (random.nextInt(15) + 20 - (Hud.getElapsedTime()/30)) * 60;
            timerMaxValue = (int)timeToComplete;

        }

        public ArrayList<Player.Weapon> getWeapons() {
            return weapons;
        }

        public void drawTimer(SpriteBatch batch, int x, int y) {
            x = (int)(x + Hud.orderOutline.getWidth() - timerOutline.getWidth() - 10);
            y = y + 0;
            Color tempColor = batch.getColor();
            if (timeToComplete/timerMaxValue < 0.6) {
                batch.setColor(Color.RED);
            }
            timerSprite.draw(batch,x,y,timerOutline.getWidth(),(timeToComplete/timerMaxValue) * 66);
            batch.setColor(tempColor);
            batch.draw(timerOutline,x,y);
            batch.setColor(Color.WHITE);
        }
    }

}

