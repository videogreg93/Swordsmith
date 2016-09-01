package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sun.media.jfxmedia.events.PlayerEvent;

import java.util.ArrayList;

/**
 * Created by Gregory on 8/27/2016.
 */
public class Forge {
    public Player.Weapon weaponInside;
    public ArrayList<Player.Weapon> weaponQueue;
    private Metal metalOutput;
    private float progress;
    private boolean inProgress;
    private float progressSpeed = 0.4f;
    private int overload;

    private int positionX;
    private int positionY;

    private NinePatch patchNormal;
    private NinePatch patchDone;
    private NinePatch patchDanger;
    // Different possible patchNormal colors
    private Texture normal = new Texture("Tiles/buildBar.png");
    private Texture done = new Texture("Tiles/buildBarDone.png");
    private Texture danger = new Texture("Tiles/buildBarDanger.png");
    private Sprite outline;

    // Sounds
    private Sound pickupWeaponComplete;
    private Sound forgeDone;
    private Sound forgeFail;

    Forge(Metal metal, int x, int y) {
        weaponQueue = new ArrayList<Player.Weapon>();
        metalOutput = metal;
        positionX = x;
        positionY = y;
        weaponInside = Player.Weapon.NONE;
        progress = 0;
        overload = 0;
        inProgress = false;

        patchNormal = new NinePatch(normal);
        patchDone = new NinePatch(done);
        patchDanger = new NinePatch(danger);
        outline = new Sprite(new Texture("Tiles/buildBarOutline.png"));
        // Sounds
        pickupWeaponComplete = Gdx.audio.newSound(Gdx.files.internal("Sounds/Pickup_Weapon_Complete.wav"));
        forgeDone = Gdx.audio.newSound(Gdx.files.internal("Sounds/Forge_Done.wav"));
        forgeFail =  Gdx.audio.newSound(Gdx.files.internal("Sounds/Forge_Fail.wav"));
    }

    boolean insertWeapon(Player.Weapon weapon) {
        if (weapon.ordinal() <= 2) {
            if (weaponInside == Player.Weapon.NONE) {
                weaponInside = weapon;
                progress = 0;
                overload = 0;
                inProgress = true;
                return true;
            }
            else {
                weaponQueue.add(weapon);
                return true;
            }
        }

        return false;


    }

    void update() {
        if (inProgress)
            progress += progressSpeed;
        if (progress >= 128) {
            progress = 128;
            overload++;
            transformWeapon();
        }
        if (overload == 360)
            forgeFail.play();


    }

    void draw(SpriteBatch batch) {
        if (inProgress) {
            if (overload >= 360)
                patchDanger.draw(batch, positionX, positionY, progress, 24);
            else if (progress == 128)
                 patchDone.draw(batch, positionX, positionY, progress, 24);
            else
                patchNormal.draw(batch, positionX, positionY, progress, 24);
            batch.draw(outline, positionX, positionY);

            // Draw the weapon queue

            // Draw the weapon inside
            if (weaponInside.ordinal() <= 2 ) {
                int ordinal =  weaponInside.ordinal();
                ordinal = ordinal + ((metalOutput.ordinal() + 1) * 3);
                batch.draw(Hud.weaponSprites[ordinal], positionX, positionY + 30, 16,16);
            }
            for (int i = 0; i < weaponQueue.size(); i++) {
                int ordinal = weaponQueue.get(i).ordinal();
                ordinal = ordinal + ((metalOutput.ordinal() + 1) * 3);
                batch.draw(Hud.weaponSprites[ordinal], positionX + ((i + 1) * 16), positionY + 30, 16,16);
            }

        }
    }

    void transformWeapon() {
        if (weaponInside.ordinal() <= 2) {
            weaponInside = Player.Weapon.values()[weaponInside.ordinal() + 3 * (metalOutput.ordinal()+1)];
            System.out.println(weaponInside.toString());
            forgeDone.play();
        }
    }

    boolean weaponCompleted() {
        return (progress >= 128);
    }

    public boolean isColliding(int playerX, int playerY, int playerWidth, int playerHeight) {
        int centerX = playerWidth/2 + playerX;
        int centerY = playerHeight/2 + playerY;

        return (centerX > positionX - 75 && centerX < positionX + 160 &&
                centerY < positionY + 100);
    }

    Player.Weapon giveWeapon() {
        Player.Weapon temp;
        if (overload >= 360) {
          temp = Player.Weapon.NONE;
        }
        else {
            temp = Player.Weapon.valueOf(weaponInside.toString());
            pickupWeaponComplete.play();
        }
        if (weaponQueue.isEmpty()) {
            weaponInside = Player.Weapon.NONE;
            progress = 0;
            inProgress = false;
        }
        else {
            weaponInside = weaponQueue.remove(0);
            progress = 0;
            overload = 0;
        }
        return temp;
    }

    enum Metal {
        COPPER,
        IRON,
        GOLD,
        ANVIL, // Not ideal but meh
        NONE
    }
}
