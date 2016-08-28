package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

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

        }

        public ArrayList<Player.Weapon> getWeapons() {
            return weapons;
        }
    }

}

