package com.mygdx.game.Upgrades;

import com.mygdx.game.Player;
import com.mygdx.game.Upgrade;

/**
 * Created by Gregory on 9/3/2016.
 */
public class Speed1 extends Upgrade {

    public Speed1() {
        super("Speed+", UPGRADE.SPEED1);
    }

    @Override
    public void onObtain(Player player) {
        System.out.println("Good");
        player.addToMoveSpeed(3);
    }
}
