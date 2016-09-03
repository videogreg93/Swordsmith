package com.mygdx.game.Upgrades;

import com.mygdx.game.Forge;
import com.mygdx.game.Player;
import com.mygdx.game.Upgrade;

/**
 * Created by Gregory on 9/3/2016.
 */
public class Build1 extends Upgrade {

    public Build1() {
        super("Build+", UPGRADE.BUILD1);
    }

    @Override
    public void onObtain(Player player) {
        for (Forge forge:player.getAllForges()
             ) {
            forge.setProgressSpeed(forge.getProgressSpeed() + 0.2f);
        }
    }
}
