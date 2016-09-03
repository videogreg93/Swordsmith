package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class SwordSmith extends ApplicationAdapter implements InputProcessor {
	SpriteBatch batch;
	TiledMap tiledMap;
	OrthographicCamera camera;
	TiledMapRenderer tiledMapRenderer;

	// Hud
	Hud hud;


	// Player Character
	Player player;

	// Forges
	Forge copper;
	Forge iron;
	Forge gold;

	// Anvil
	Anvil anvil;

	// The game states
	enum gameState {
		MAINMENU,
		HELPMENU,
		DIFFICULTYMENU,
		GAMEPLAY,
		GAMEOVER,
		UPGRADE
	}

	gameState currentGameState;
	boolean isPaused;

	// Main Menu Stuff
	Sprite titleScreen;
	Sprite startButton;
	Sprite helpButton;
	Sprite quitButton;

	// Gameover stuff

	Sprite gameOverBG;
	Sprite yeahButton;
	Sprite nahButton;
	Sprite buttonOutline;
	int outlineX, outlineY;
	Sound chooseMenu;

	// Help Menu
	Sprite helpMenuBG;

	// Difficulty Menu
	Sprite difficultyMenuBG;
	Sprite easyButton;
	Sprite mediumButton;
	Sprite hardButton;

	// BGM
	Music BGM;
	Music titleScreenMusic;
	Music gameOverMusic;

	// Difficulty
	float difficulty = 3f;

	// Final time obtained
	public String finalTime = "";
	
	@Override
	public void create () {
		batch = new SpriteBatch();

		// Hud
		Hud.create();


		// Init the forges
		copper = new Forge(Forge.Metal.COPPER, 523, 86 );
		iron = new Forge(Forge.Metal.IRON, 806, 86 );
		gold = new Forge(Forge.Metal.GOLD, 1089, 86 );

		// Init the anvil
		anvil = new Anvil();


		// Init Player
		player = new Player(copper, iron, gold, anvil);

		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, w, h);
		camera.update();

		tiledMap = new TmxMapLoader().load("Tiles/map1.tmx");
		tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
		Gdx.input.setInputProcessor(this);



		// Game State
		currentGameState = gameState.MAINMENU;
		isPaused = false;

		// Game Over stuff
		gameOverBG = new Sprite(new Texture("Tiles/GameOver.png"));
		yeahButton = new Sprite(new Texture("Tiles/yeah.png"));
		nahButton = new Sprite(new Texture("Tiles/nah.png"));
		buttonOutline = new Sprite(new Texture("Tiles/buttonOutline.png"));
		outlineX = 170;
		outlineY = 100;
		chooseMenu =  Gdx.audio.newSound(Gdx.files.internal("Sounds/Choose_Menu.wav"));

		// Main Menu stuff
		titleScreen = new Sprite(new Texture("Tiles/titleScreen.png"));
		startButton = new Sprite(new Texture("Tiles/start.png"));
		helpButton = new Sprite(new Texture("Tiles/help.png"));
		quitButton = new Sprite(new Texture("Tiles/quit.png"));
		outlineX = 505;
		outlineY = 361;

		// Help Menu
		helpMenuBG = new Sprite(new Texture("Tiles/helpMenu.png"));

		// Difficulty menu
		difficultyMenuBG = new Sprite(new Texture("Tiles/difficultyMenu.png"));
		easyButton = new Sprite(new Texture("Tiles/easy.png"));
		mediumButton = new Sprite(new Texture("Tiles/medium.png"));
		hardButton = new Sprite(new Texture("Tiles/hard.png"));

		// BGM
		BGM = Gdx.audio.newMusic(Gdx.files.internal("Sounds/swordSmith.mp3"));
		if (titleScreenMusic == null)
			titleScreenMusic = Gdx.audio.newMusic(Gdx.files.internal("Sounds/titleScreenSwordSmith.mp3"));
		if (gameOverMusic == null)
			gameOverMusic = Gdx.audio.newMusic(Gdx.files.internal("Sounds/gameOverSwordSmith.mp3"));


	}

	public void start(float diff) {
		create();
		// Order System
		OrderSystem.create(diff);
		difficulty = diff;
		currentGameState = gameState.GAMEPLAY;
		BGM.play();
		BGM.setLooping(true);
		titleScreenMusic.stop();
		gameOverMusic.stop();
	}

	@Override
	public void render () {
		if (!isPaused && currentGameState == gameState.GAMEPLAY) {
			Gdx.gl.glClearColor(1, 0, 0, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			camera.update();
			tiledMapRenderer.setView(camera);
			tiledMapRenderer.render();

			// Update player position
			player.update();

			// Update the forges
			copper.update();
			iron.update();
			gold.update();

			// update the anvil
			anvil.update();

			// update HUD
			Hud.update();

			// Create a new order if it has been atleast 10 seconds
			OrderSystem.update();

			batch.begin();
			//batch.draw(player.getTexture(), player.getPositionX(), player.getPositionY());
			player.draw(batch);
			// draw the forges
			copper.draw(batch);
			iron.draw(batch);
			gold.draw(batch);

			// Draw the anvil
			anvil.draw(batch);

			// Draw the hud at the end
			Hud.draw(batch);
			batch.end();

			// Lets check if the player is dead
			if (player.isDead()) {
				currentGameState = gameState.GAMEOVER;
				outlineX = 170;
				outlineY = 100;
				BGM.stop();
				gameOverMusic.play();
				finalTime = Hud.getFormatedTime();
			}
		}
		else if (isPaused) {
			batch.begin();
				Hud.drawPaused(batch);
			batch.end();
		}
		else if (currentGameState == gameState.UPGRADE) {
			batch.begin();
				Hud.drawUpgrade(batch);
			batch.end();
		}
		else if (currentGameState == gameState.GAMEOVER) {
			Gdx.gl.glClearColor(1, 0, 0, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			batch.begin();
			drawGameOver(batch);
			Hud.drawFinalTime(batch, finalTime);
			batch.end();
		}
		else if (currentGameState == gameState.MAINMENU) {
			if (!titleScreenMusic.isPlaying()) {
				titleScreenMusic.play();
				titleScreenMusic.setLooping(true);
			}
			Gdx.gl.glClearColor(1, 0, 0, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			batch.begin();
			drawTitleScreen(batch);
			batch.end();
		}
		else if (currentGameState == gameState.HELPMENU) {
			Gdx.gl.glClearColor(1, 0, 0, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			batch.begin();
			drawHelpMenu(batch);
			batch.end();
		}
		else if (currentGameState == gameState.DIFFICULTYMENU) {
			Gdx.gl.glClearColor(1, 0, 0, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			batch.begin();
			drawDifficultyMenu(batch);
			batch.end();
		}



	}

	@Override
	public boolean keyDown(int keycode) {
		// Movement
		if (currentGameState == gameState.GAMEPLAY) {
			if (keycode == Input.Keys.LEFT)
				player.moveLeft();
			if (keycode == Input.Keys.RIGHT)
				player.moveRight();
			if (keycode == Input.Keys.UP)
				player.moveUp();
			if (keycode == Input.Keys.DOWN)
				player.moveDown();
			if (keycode == Input.Keys.ESCAPE)
				Gdx.app.exit();

			// Interaction

			if (keycode == Input.Keys.Z) {
				player.interact();
			}
			if (keycode == Input.Keys.P) {
				isPaused = !isPaused;
			}
			if (keycode == Input.Keys.Q) {
				currentGameState = gameState.UPGRADE;

			}
		}
		else if (currentGameState == gameState.GAMEOVER) {

			if (keycode == Input.Keys.LEFT && outlineX != 170) {
				outlineX = 170;
				chooseMenu.play();
			}
			if (keycode == Input.Keys.RIGHT && outlineX != (170 + yeahButton.getWidth() + 50) ) {
				outlineX = (int) (170 + yeahButton.getWidth() + 50);
				chooseMenu.play();
			}
			if (keycode == Input.Keys.ENTER) {
				if (outlineX == 170) { // play again
					start(difficulty);
				}
				else {
					Gdx.app.exit();
				}
			}
		}

		else if (currentGameState == gameState.MAINMENU) {
			if (keycode == Input.Keys.UP && outlineY != 361) {
				outlineY += startButton.getHeight() + 50;
				chooseMenu.play();
			}
			if (keycode == Input.Keys.DOWN && outlineY != 361 - (2 * startButton.getHeight()) - 100) {
				outlineY -= (startButton.getHeight() + 50);
				chooseMenu.play();
			}
			if (keycode == Input.Keys.ENTER) {
				if (outlineY == 361) { // play again
					//start();
					currentGameState = gameState.DIFFICULTYMENU;
					outlineX = 920;
					outlineY = 51;
				}
				else if (outlineY == 361 - (1 * startButton.getHeight()) - 50) {
					currentGameState = gameState.HELPMENU;
				}
				else {
					Gdx.app.exit();
				}
			}
		}
		else if (currentGameState == gameState.DIFFICULTYMENU) {
			if (keycode == Input.Keys.UP && outlineY != 51 + 2 * (hardButton.getHeight() + 50)) {
				outlineY += hardButton.getHeight() + 50;
				chooseMenu.play();
			}
			if (keycode == Input.Keys.DOWN && outlineY != 51) {
				outlineY -= (hardButton.getHeight() + 50);
				chooseMenu.play();
			}
			if (keycode == Input.Keys.ENTER) {
				if (outlineY == 51) { // hard
					start(4f);
				}
				else if (outlineY == 51 + (1 * hardButton.getHeight()) + 50) {
					start(3f);
				}
				else {
					start(2f);
				}
			}
		}

		else if (currentGameState == gameState.HELPMENU) {
			if (keycode == Input.Keys.ESCAPE) {
				currentGameState = gameState.MAINMENU;
			}
		}

		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		if(keycode == Input.Keys.LEFT)
			player.stopMoveLeft();
		if(keycode == Input.Keys.RIGHT)
			player.stopMoveRight();
		if(keycode == Input.Keys.UP)
			player.stopMoveUp();
		if(keycode == Input.Keys.DOWN)
			player.stopMoveDown();
		return false;
	}

	public void drawGameOver(SpriteBatch batch) {
		batch.draw(gameOverBG.getTexture(), 0,0);
		batch.draw(yeahButton.getTexture(), 170,100);
		batch.draw(nahButton.getTexture(), 170 + yeahButton.getWidth() + 50,100);
		batch.draw(buttonOutline.getTexture(), outlineX, outlineY);
	}

	public void drawTitleScreen(SpriteBatch batch) {
		batch.draw(titleScreen.getTexture(),0,0);
		batch.draw(startButton.getTexture(), 505, 361);
		batch.draw(helpButton.getTexture(), 505, 361 - startButton.getHeight() - 50);
		batch.draw(quitButton.getTexture(), 505, 361 - (2 * startButton.getHeight()) - 100);
		batch.draw(buttonOutline.getTexture(), outlineX, outlineY);
	}

	public void drawDifficultyMenu(SpriteBatch batch) {
		batch.draw(difficultyMenuBG.getTexture(),0,0);
		batch.draw(hardButton.getTexture(), 920, 51);
		batch.draw(mediumButton.getTexture(), 920, 51 + hardButton.getHeight() + 50);
		batch.draw(easyButton.getTexture(), 920, 51 + 2 * (hardButton.getHeight() + 50));
		batch.draw(buttonOutline.getTexture(), outlineX, outlineY);
	}

	public void drawHelpMenu(SpriteBatch batch) {
		batch.draw(helpMenuBG,0,0);
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		System.out.println("(" + screenX + "," + screenY + ")");
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}
