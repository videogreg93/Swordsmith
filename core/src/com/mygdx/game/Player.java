package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Upgrades.Speed1;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Gregory on 8/27/2016.
 */
public class Player {
    // Different Sprites
    private Sprite[] playerSprites;
    private Sprite interactIcon;

    private int positionX;
    private int positionY;
    private int speed;
    private boolean moveUp;
    private boolean moveDown;
    private boolean moveRight;
    private boolean moveLeft;
    private Weapon currentWeaponCollision;
    private Weapon currentlyHolding;
    private Forge.Metal currentForgeCollision;

    // Sounds
    private Sound pickupCase;
    private Sound giveOrder;
    private Sound hitAnvil;


    // The forges
    Forge copper;
    Forge iron;
    Forge gold;

    // Anvil
    Anvil anvil;

    // The dropped off weapons for the next customer
    ArrayList<Player.Weapon> deliveredWeapons;
    boolean withinDropZone = false;

    // Number of lives;
    public static int LIVESDEFAULT;
    public static int LIVES;

    // Money
    public static int money;

    // Current upgrades
    private ArrayList<Upgrade> allUpgrades;

    enum Weapon {
        SWORDCASE,
        SHIELDCASE,
        KATANACASE,
        COPPERSWORD,
        COPPERSHIELD,
        COPPERKATANA,
        IRONSWORD,
        IRONSHIELD,
        IRONKATANA,
        GOLDSWORD,
        GOLDSHIELD,
        GOLDKATANA,
        COPPERKATANACOMPLETED,
        IRONKATANACOMPLETED,
        GOLDKATANACOMPLETED,
        NONE
    }






    Player(Forge COPPER, Forge IRON, Forge GOLD, Anvil ANVIL) {



        playerSprites = new Sprite[16];
        // Init the Sprites
        playerSprites[0] = new Sprite(new Texture("Tiles/playerWithCases/PlayerSword.png"));
        playerSprites[1] = new Sprite(new Texture("Tiles/playerWithCases/PlayerShield.png"));
        playerSprites[2] = new Sprite(new Texture("Tiles/playerWithCases/PlayerKatana.png"));
        playerSprites[3] = new Sprite(new Texture("Tiles/playerWeaponComplete/sword/playerSwordCopper.png"));
        playerSprites[4] = new Sprite(new Texture("Tiles/playerWeaponComplete/shield/playerShieldCopper.png"));
        playerSprites[5] = new Sprite(new Texture("Tiles/playerWeaponComplete/katana/playerKatanaCopper.png"));
        playerSprites[6] = new Sprite(new Texture("Tiles/playerWeaponComplete/sword/playerSwordIron.png"));
        playerSprites[7] = new Sprite(new Texture("Tiles/playerWeaponComplete/shield/playerShieldIron.png"));
        playerSprites[8] = new Sprite(new Texture("Tiles/playerWeaponComplete/katana/playerKatanaIron.png"));
        playerSprites[9] = new Sprite(new Texture("Tiles/playerWeaponComplete/sword/playerSwordGold.png"));
        playerSprites[10] = new Sprite(new Texture("Tiles/playerWeaponComplete/shield/playerShieldGold.png"));
        playerSprites[11] = new Sprite(new Texture("Tiles/playerWeaponComplete/katana/playerKatanaGold.png"));
        playerSprites[12] = new Sprite(new Texture("Tiles/playerWeaponComplete/katana/playerKatanaCopper.png"));
        playerSprites[13] = new Sprite(new Texture("Tiles/playerWeaponComplete/katana/playerKatanaIron.png"));
        playerSprites[14] = new Sprite(new Texture("Tiles/playerWeaponComplete/katana/playerKatanaGold.png"));
        playerSprites[15] = new Sprite(new Texture("Tiles/player.png"));

        interactIcon = new Sprite(new Texture("Tiles/exclamationMark.png"));

        positionX = 550;
        positionY = 400;
        speed = 8;

        copper = COPPER;
        iron = IRON;
        gold = GOLD;
        anvil = ANVIL;

        currentlyHolding = Weapon.NONE;
        deliveredWeapons = new ArrayList<Weapon>();

        // Number of lives
        LIVESDEFAULT = 3;
        LIVES = LIVESDEFAULT;
        money = 0;

        // Init the sounds;
        pickupCase = Gdx.audio.newSound(Gdx.files.internal("Sounds/Pickup_Case.wav"));
        hitAnvil = Gdx.audio.newSound(Gdx.files.internal("Sounds/Hit_Anvil.wav"));
        giveOrder = Gdx.audio.newSound(Gdx.files.internal("Sounds/Give_Weapon.wav"));

        allUpgrades = new ArrayList<Upgrade>();



    }

    // Getters

    int getPositionX() {
        return positionX;
    }

    int getPositionY() {
        return positionY;
    }

    // Movement

    void moveLeft() {
        moveLeft = true;
        moveRight = false;
    }

    void moveRight() {
        moveRight = true;
        moveLeft = false;

    }

    void moveUp() {
        moveUp = true;
        moveDown = false;
    }

    void moveDown() {
        moveDown = true;
        moveUp = false;
    }

    void stopMoveLeft() {
        moveLeft = false;
    }

    void stopMoveRight() {
        moveRight = false;
    }

    void stopMoveUp() {
        moveUp = false;
    }

    void stopMoveDown() {
        moveDown = false;
    }

    // Interaction functions

    void interact() {
        // If colliding with a casing, we grab the right case.
        if (currentWeaponCollision == Weapon.SWORDCASE) {
            currentlyHolding = Weapon.SWORDCASE;
            pickupCase.play();
        }
        else if (currentWeaponCollision == Weapon.SHIELDCASE) {
            currentlyHolding = Weapon.SHIELDCASE;
            pickupCase.play();
        }
        else if (currentWeaponCollision == Weapon.KATANACASE) {
            currentlyHolding = Weapon.KATANACASE;
            pickupCase.play();
        }
        else if (currentForgeCollision == Forge.Metal.COPPER) {
            if (copper.insertWeapon(currentlyHolding))
                currentlyHolding = Weapon.NONE;
            if (copper.weaponCompleted() && currentlyHolding == Weapon.NONE)
                currentlyHolding = copper.giveWeapon();
        }
        else if (currentForgeCollision == Forge.Metal.IRON) {
            if (iron.insertWeapon(currentlyHolding))
                currentlyHolding = Weapon.NONE;
            else if (iron.weaponCompleted() && currentlyHolding == Weapon.NONE)
                currentlyHolding = iron.giveWeapon();
        }
        else if (currentForgeCollision == Forge.Metal.GOLD) {
            if (gold.insertWeapon(currentlyHolding))
                currentlyHolding = Weapon.NONE;
            else if (gold.weaponCompleted() && currentlyHolding == Weapon.NONE)
                currentlyHolding = gold.giveWeapon();
        }
        else if (currentForgeCollision == Forge.Metal.ANVIL) {
            if (anvil.insertWeapon(currentlyHolding))
                currentlyHolding = Weapon.NONE;
            else if (anvil.getInProgress()) {
                if (anvil.weaponCompleted()) {
                    currentlyHolding = anvil.giveWeapon();

                }
                else {
                    anvil.increaseProgress();
                    hitAnvil.play();
                }
            }
        }
        else if (withinDropZone) {
            boolean saleCompleted = false;
                for (int z = 0; z < OrderSystem.allOrders.size(); z++) {
                    boolean removedWeapon =  OrderSystem.allOrders.get(z).getWeapons().remove(currentlyHolding);
                    if (removedWeapon && OrderSystem.allOrders.get(z).getWeapons().isEmpty()) {
                        System.out.println("SALE!");
                        saleCompleted = true;
                        giveOrder.play();
                        OrderSystem.allOrders.remove(OrderSystem.allOrders.get(z));
                        OrderSystem.orderCompleted();
                        break;
                    }
                    if (saleCompleted)
                        break;
                }


            if (!saleCompleted) {
                System.out.println("WRONG WEAPON, NO SALE");
            }
            currentlyHolding = Weapon.NONE;

        }
    }

    // Update functions

    void update() {
        if (moveRight)
            positionX += speed;
        else if (moveLeft)
            positionX -= speed;
        if (moveUp)
            positionY += speed;
        else if (moveDown)
            positionY -= speed;

        // Make sure we don't go over the HUD OR THE BAR
        if (positionY + playerSprites[15].getHeight() >= 736)
            positionY = (int)(736 - playerSprites[15].getHeight() );
        if (positionY <= 0)
            positionY = 0;
        if (positionX <= 481)
            positionX = 481;
        if (positionX + playerSprites[15].getWidth() >= Gdx.graphics.getWidth()) {
            positionX = (int)(Gdx.graphics.getWidth() - playerSprites[15].getWidth());
        }

        // Check to see if colliding with Sword

        float x = positionX + (playerSprites[15].getWidth()/2);
        float y = positionY + (playerSprites[15].getHeight()/2);

        if (x > 600 && x < 675 && y > 500 && y < 800) {
                if (currentWeaponCollision != Weapon.SWORDCASE) {
                    currentWeaponCollision = Weapon.SWORDCASE;
                    System.out.println("SWORD");
                }
        }
        // Check to see if colliding with Shield
        else if (x > 900 && x < 1000 && y > 500 && y < 800) {
                if (currentWeaponCollision != Weapon.SHIELDCASE) {
                    currentWeaponCollision = Weapon.SHIELDCASE;
                    System.out.println("SHIELD");
                }
        }
        else if (x > 1180 && x < 1280 && y > 500 && y < 800) {
            if (currentWeaponCollision != Weapon.KATANACASE) {
                currentWeaponCollision = Weapon.KATANACASE;
                System.out.println("KATANA");
            }
        }
        else if (currentWeaponCollision != Weapon.NONE) {

            currentWeaponCollision = Weapon.NONE;
            System.out.println("NONE");
        }

        // Same principal but for the forges now

        // Copper Forge
        if (copper.isColliding(positionX,positionY,(int)playerSprites[15].getWidth(),(int)playerSprites[15].getHeight())) {
            if (currentForgeCollision != Forge.Metal.COPPER) {
                currentForgeCollision = Forge.Metal.COPPER;
                System.out.println("COPPER");
            }
        }
        else if (iron.isColliding(positionX,positionY,(int)playerSprites[15].getWidth(),(int)playerSprites[15].getHeight())) {
            if (currentForgeCollision != Forge.Metal.IRON) {
                currentForgeCollision = Forge.Metal.IRON;
                System.out.println("IRON");
            }
        }
        else if (gold.isColliding(positionX,positionY,(int)playerSprites[15].getWidth(),(int)playerSprites[15].getHeight())) {
            if (currentForgeCollision != Forge.Metal.GOLD) {
                currentForgeCollision = Forge.Metal.GOLD;
                System.out.println("GOLD");
            }
        }
        else if (x > 1017 && x < 1238 && y > 359 && y < 438) {
            if (currentForgeCollision != Forge.Metal.ANVIL) {
                currentForgeCollision = Forge.Metal.ANVIL;
                System.out.println("ANVIL");
            }
        }

        else if (currentForgeCollision != Forge.Metal.NONE) {
            currentForgeCollision = Forge.Metal.NONE;
            System.out.println("NO FORGE");
        }

        // We check if we're in the drop off zone
        if (x > 474 && x < 611 && y > 489 && y < 564 ) {
            withinDropZone = true;
        }
        else {
            withinDropZone = false;
        }

    }

    // Drawing

    Texture getTexture() {

        return playerSprites[currentlyHolding.ordinal()].getTexture();
    }

    public void draw(SpriteBatch batch) {
        batch.draw(playerSprites[currentlyHolding.ordinal()].getTexture(),positionX,positionY);
        if (currentForgeCollision != Forge.Metal.NONE || currentWeaponCollision != Weapon.NONE) {
            batch.draw(interactIcon.getTexture(), positionX + (playerSprites[currentlyHolding.ordinal()].getWidth()/2) - interactIcon.getWidth()/2 , positionY+ playerSprites[currentlyHolding.ordinal()].getHeight() + 10 );
        }
    }

    // Game State

    public static void removeLife() {
        LIVES--;
        System.out.println("Current lives: " + LIVES);

    }

    public boolean isDead() {
        return (LIVES <= 0);
    }

    public static void makeMoneyFromSale() {
        money += 5; // TODO magic number
    }

    public void addToMoveSpeed(int add) {
        speed += add;
    }

    public void addUpgrade(Upgrade upgrade) {
        upgrade.onObtain(this);
        allUpgrades.add(upgrade);
    }

}
