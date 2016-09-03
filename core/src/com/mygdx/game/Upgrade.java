package com.mygdx.game;

/**
 * Created by Gregory on 9/3/2016.
 */
public class Upgrade {
    protected String name;
    public enum UPGRADE {
        SPEED1
    }
    public UPGRADE type;



    protected Upgrade(String y, UPGRADE upgrade) {
        name = y;
        type = upgrade;


    }

    public void onObtain(Player player) {
        System.out.println("Bad");
    }

    public void update() {

    }
}
