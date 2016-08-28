package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by Gregory on 8/27/2016.
 */
public class Anvil {
    private Player.Weapon weaponInside;
    private boolean inProgress;
    int progress;
    int progressSpeed = 1;

    private NinePatch patchNormal;
    private NinePatch patchDone;
    // Different possible patchNormal colors
    private Texture normal = new Texture("Tiles/buildBar.png");
    private Texture done = new Texture("Tiles/buildBarDone.png");
    private Sprite outline = new Sprite(new Texture("Tiles/buildBarOutline.png"));;

    Anvil() {
        progress = 0;
        inProgress = false;
        weaponInside = Player.Weapon.NONE;

        patchNormal = new NinePatch(normal);
        patchDone = new NinePatch(done);
    }

    public boolean insertWeapon(Player.Weapon weapon) {
        if ((weapon == Player.Weapon.COPPERKATANA || weapon == Player.Weapon.IRONKATANA || weapon == Player.Weapon.GOLDKATANA) && weaponInside == Player.Weapon.NONE) {
            inProgress = true;
            progress = 0;
            weaponInside = weapon;

            return true;
        }

        return false;
    }

    public void update() {

        if (inProgress) {
            if (progress <= 0) {
                progress = 0;
            }
            else if (progress >= 128) {
                progress = 128;
            }
            else
                progress -= progressSpeed;

        }
    }

    public void increaseProgress() {
        progress += 15;
    }

    public boolean getInProgress() {
        return inProgress;
    }

    public boolean weaponCompleted() {
        return (progress >= 128);
    }

    public Player.Weapon giveWeapon() {
        inProgress = false;
        progress = 0;
        if (weaponInside == Player.Weapon.COPPERKATANA) {
            weaponInside = Player.Weapon.NONE;
            return (Player.Weapon.COPPERKATANACOMPLETED);
        }
        if (weaponInside == Player.Weapon.IRONKATANA) {
            weaponInside = Player.Weapon.NONE;
            return (Player.Weapon.IRONKATANACOMPLETED);
        }
        if (weaponInside == Player.Weapon.GOLDKATANA) {
            weaponInside = Player.Weapon.NONE;
            return (Player.Weapon.GOLDKATANACOMPLETED);
        }


        return Player.Weapon.NONE;
    }

    public void draw(SpriteBatch batch) {
        if (inProgress) {

            if (progress == 128)
                patchDone.draw(batch, 1100, 411, progress, 24);
            else
                patchNormal.draw(batch, 1100, 411, progress, 24);
            batch.draw(outline,1100,411);
        }
    }

}
