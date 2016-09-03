package com.mygdx.game.Upgrades;

import com.mygdx.game.OrderSystem;
import com.mygdx.game.Player;
import com.mygdx.game.Upgrade;

/**
 * Created by Gregory on 9/3/2016.
 */
public class Order1 extends Upgrade {

    public Order1() {
        super("Order+",UPGRADE.ORDER1);
    }

    @Override
    public void onObtain(Player player) {
        OrderSystem.minimumOrderTime += 10;
    }
}
