package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.physics.box2d.*;

import javax.swing.*;

public class CovidAttack extends ApplicationAdapter {
	private static final float SCALE = 2.0f;
	public static final float PIXEL_PER_METER = 32f;
	private static final float TIME_STEP = 1 / 60f;
	private static final int VELOCITY_ITERATIONS = 6;
	private static final int POSITION_ITERATIONS = 2;
	private static final float VELOCITY_Y = -9.85f;
	private static final float VELOCITY_X = 0f;
	private final String[] MAP_PATH= {"map/Hospital.tmx", "map/Neighborhood.tmx", "map/City.tmx"};
	private OrthographicCamera orthographicCamera;
	private Box2DDebugRenderer box2DDebugRenderer;
	private World world;
	private Player player;
	private Enemy enemy;
	private Enemy2 enemy2;
	private Enemy3 enemy3;
	private SpriteBatch batch;
	private OrthogonalTiledMapRenderer tiledMapRenderer;
	private TiledMap tiledMap;
	private static final int FRAME_COLS = 2;
	private static final int FRAME_ROWS = 2;
	private static final int FRAME_COLS1 = 2;
	private static final int FRAME_ROWS1 = 2;
	Animation<TextureRegion> jumpAnimation;
	Animation<TextureRegion> idleAnimation;
	float stateTime;
	public static int state;
	private Texture playerJump;
	private Texture playerIdle;
	private Texture enemy1Texture;
	private Texture enemy2Texture;
	private Texture enemy3Texture;
	private double enemyRadius = 2;
	private int levelNum = 1;
	private Texture door;

	@Override
	public void create() {
		orthographicCamera = new OrthographicCamera();
		orthographicCamera.setToOrtho(false, Gdx.graphics.getWidth() / SCALE, Gdx.graphics.getHeight() / SCALE);
		world = new World(new Vector2(VELOCITY_X, VELOCITY_Y), false);
		world.setContactListener(new WorldContactListener());
		batch = new SpriteBatch();
		enemy1Texture = new Texture(Enemy.ENEMY_IMG_PATH);
		enemy2Texture = new Texture(Enemy2.ENEMY_IMG_PATH2);
		enemy3Texture = new Texture(Enemy3.ENEMY_IMG_PATH3);
		door = new Texture(Gdx.files.internal("Door.png"));

		playerJump = new Texture(Gdx.files.internal("Jumping.png")); //the sprite sheet used for jumping animation

		TextureRegion[][] tmp2 = TextureRegion.split(playerJump, playerJump.getWidth() / FRAME_COLS, playerJump.getHeight() / FRAME_ROWS);

		TextureRegion[] jumpFrame = new TextureRegion[FRAME_COLS * FRAME_ROWS]; //create texture region for jumping animation
		int index2 = 0;
		//accounts for every frame in the player jumping animation
		for(int i = 0; i < FRAME_ROWS; i++) {
			for(int j = 0; j < FRAME_COLS; j++) {
				jumpFrame[index2++] = tmp2[i][j];
			}
		}

		jumpAnimation = new Animation<TextureRegion>(.10f, jumpFrame); //controls how many frames per second the jumping animation moves at

		playerIdle = new Texture(Gdx.files.internal("Character-idle.png")); //the sprite sheet used for idle animation

		TextureRegion[][] tmp3 = TextureRegion.split(playerIdle, playerIdle.getWidth() / FRAME_COLS1, playerIdle.getHeight() / FRAME_ROWS1);

		TextureRegion[] idleFrame = new TextureRegion[FRAME_COLS1 * FRAME_ROWS1]; //create texture region for idle animation
		int index3 = 0;
		//accounts for every frame in the player idle animation
		for(int i = 0; i < FRAME_ROWS1; i++) {
			for(int j = 0; j < FRAME_COLS1; j++) {
				idleFrame[index3++] = tmp3[i][j];
			}
		}

		idleAnimation = new Animation<TextureRegion>(.10f, idleFrame); //controls how many frames per second the idle animation moves at

		box2DDebugRenderer = new Box2DDebugRenderer();
		tiledMap = new TmxMapLoader().load(MAP_PATH[0]);
		tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
		MapParser.parseMapLayers(world, tiledMap);
		player = new Player(world); //calls the body from Player class, contains physics
		enemy = new Enemy(world); //calls the body from Enemy class, contains physics
		enemy2 = new Enemy2(world);
		enemy3 = new Enemy3(world);
	}

	@Override
	public void render () {
		update();
		Gdx.gl.glClearColor(0.5f, 0.8f, 1f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		tiledMapRenderer.render();
		stateTime += Gdx.graphics.getDeltaTime();
		TextureRegion currentFrame;
		batch.begin();
		batch.draw(door, 1500, 80, 50f, 70f);
		enemyDraw();
		enemy1Radius();
		enemy2Radius();
		enemy3Radius();
    
		//checks for the appropriate animation for character depending on its position, which is represented by the state variable.
		switch(state) {
			default:
				//if the player is not jumping, the default case is called and the idle animation plays
				currentFrame = idleAnimation.getKeyFrame(stateTime, true);
				batch.draw(currentFrame, player.getBody().getPosition().x * PIXEL_PER_METER - (playerIdle.getWidth() / 2f) + 20,
						player.getBody().getPosition().y * PIXEL_PER_METER - (playerIdle.getHeight() / 2f) + 50);
				break;
				//if the player is jumping, case 1 is called and the jumping animation plays
			case 1:
				currentFrame = jumpAnimation.getKeyFrame(stateTime, true);
				batch.draw(currentFrame, player.getBody().getPosition().x * PIXEL_PER_METER - (playerJump.getWidth() / 2f) + 20,
						player.getBody().getPosition().y * PIXEL_PER_METER - (playerJump.getHeight() / 2f) + 50);
				break;
		}
		batch.end();
	}

	@Override
	public void dispose() {
		batch.dispose();
		playerJump.dispose();
		playerIdle.dispose();
		enemy1Texture.dispose();
		box2DDebugRenderer.dispose();
		world.dispose();
		tiledMapRenderer.dispose();
		tiledMap.dispose();
	}

	public void update() {
		world.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
		inputUpdate();
		cameraUpdate();
		levelUpdate();
		tiledMapRenderer.setView(orthographicCamera);
		batch.setProjectionMatrix(orthographicCamera.combined);
	}

	public void levelUpdate() {
		if (levelNum == 1) {
			if (player.getBody().getPosition().x >= 49 && player.getBody().getPosition().x <= 50 && player.getBody().getPosition().y >= 3.5 && player.getBody().getPosition().y <= 5) {
				world = new World(new Vector2(VELOCITY_X, VELOCITY_Y), false);
				world.setContactListener(new WorldContactListener());
				tiledMap = new TmxMapLoader().load(MAP_PATH[1]);
				tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
				player = new Player(world); //calls the body from Player class, contains physics
				enemy = new Enemy(world); //calls the body from Enemy class, contains physics
				enemy2 = new Enemy2(world);
				enemy3 = new Enemy3(world);
				MapParser.parseMapLayers(world, tiledMap);
				levelNum++;
			}
		}
		else if (levelNum == 2) {
			if (player.getBody().getPosition().x >= 49 && player.getBody().getPosition().x <= 50 && player.getBody().getPosition().y >= 3.5 && player.getBody().getPosition().y <= 5) {
				world = new World(new Vector2(VELOCITY_X, VELOCITY_Y), false);
				world.setContactListener(new WorldContactListener());
				tiledMap = new TmxMapLoader().load(MAP_PATH[2]);
				tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
				player = new Player(world); //calls the body from Player class, contains physics
				MapParser.parseMapLayers(world, tiledMap);
				enemy = new Enemy(world); //calls the body from Enemy class, contains physics
				enemy2 = new Enemy2(world);
				enemy3 = new Enemy3(world);
				levelNum++;
			}
		}
		else if (levelNum == 3) {
			if (player.getBody().getPosition().x >= 49 && player.getBody().getPosition().x <= 50 && player.getBody().getPosition().y >= 3.5 && player.getBody().getPosition().y <= 5) {
				Win.winMessage();
			}
		}
		System.out.println(player.getBody().getPosition().x + " " + player.getBody().getPosition().y);
	}

	//updates the camera to the correct position when player moves
	public void cameraUpdate() {
		Vector3 position = orthographicCamera.position;
		position.x = player.getBody().getPosition().x * PIXEL_PER_METER;
		position.y = player.getBody().getPosition().y * PIXEL_PER_METER;
		orthographicCamera.position.set(position);
		orthographicCamera.update();
	}

	@Override
	public void resize(int width, int height) {
		orthographicCamera.setToOrtho(false, width / SCALE, height / SCALE);
	}

	//controls the movement of the player depending on user clicks of the mouse
	public void inputUpdate() {
		int horizontalForce = 0;
		boolean isJumping = false;
		if (Gdx.input.isTouched()) {
			Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
			touchPos = orthographicCamera.unproject(touchPos);
			if (touchPos.x / PIXEL_PER_METER > player.getBody().getPosition().x) {
				horizontalForce += 1;
			}
			if (touchPos.x / PIXEL_PER_METER < player.getBody().getPosition().x) {
				horizontalForce -= 1;
			}
			if (touchPos.y / PIXEL_PER_METER > player.getBody().getPosition().y && !player.isJumping()) {
				player.getBody().applyForceToCenter(0, Player.JUMP_FORCE, false);
				state = 1; //if the user isn't touching the ground, it must be jumping, so state = 1 which allows for jumping animation
			}
		}
		player.getBody().setLinearVelocity(horizontalForce * Player.RUN_FORCE, player.getBody().getLinearVelocity().y);
	}

	//draws the sprite batch for all enemies
	public void enemyDraw() {
		batch.draw(enemy1Texture, enemy.getBody1().getPosition().x * PIXEL_PER_METER - (enemy1Texture.getWidth() / 2f),
				enemy.getBody1().getPosition().y * PIXEL_PER_METER - (enemy1Texture.getHeight() / 2f));
		batch.draw(enemy2Texture, enemy2.getBody2().getPosition().x * PIXEL_PER_METER - (enemy2Texture.getWidth() / 2f),
				enemy2.getBody2().getPosition().y * PIXEL_PER_METER - (enemy2Texture.getHeight() / 2f));
		batch.draw(enemy3Texture, enemy3.getBody3().getPosition().x * PIXEL_PER_METER - (enemy3Texture.getWidth() / 2f),
				enemy3.getBody3().getPosition().y * PIXEL_PER_METER - (enemy3Texture.getHeight() / 2f));
	}

	//controls the player-enemy collision. if the player gets within a 2 block radius of an enemy, the player will die.
	public void enemy1Radius(){
		if((player.getBody().getPosition().x) >= (enemy.getBody1().getPosition().x - enemyRadius) && (player.getBody().getPosition().x)
				<= (enemy.getBody1().getPosition().x + enemyRadius)){
			if((player.getBody().getPosition().y) >= (enemy.getBody1().getPosition().y - enemyRadius) && (player.getBody().getPosition().y)
					<= (enemy.getBody1().getPosition().y + enemyRadius)) {
					Die.dieMessage(); //calls the method which controls the JOptionPanes for player death
			}
		}
	}

	//controls the player-enemy collision. if the player gets within a 2 block radius of an enemy, the player will die.
	public void enemy2Radius(){
		if((player.getBody().getPosition().x) >= (enemy2.getBody2().getPosition().x - enemyRadius) && (player.getBody().getPosition().x)
				<= (enemy2.getBody2().getPosition().x + enemyRadius)){
			if((player.getBody().getPosition().y) >= (enemy2.getBody2().getPosition().y - enemyRadius) && (player.getBody().getPosition().y)
					<= (enemy2.getBody2().getPosition().y + enemyRadius)) {
					Die.dieMessage(); //calls the method which controls the JOptionPanes for player death
			}
		}
	}

	//controls the player-enemy collision. if the player gets within a 2 block radius of an enemy, the player will die.
	public void enemy3Radius(){
		if((player.getBody().getPosition().x) >= (enemy3.getBody3().getPosition().x - enemyRadius) && (player.getBody().getPosition().x)
				<= (enemy3.getBody3().getPosition().x + enemyRadius)){
			if((player.getBody().getPosition().y) >= (enemy3.getBody3().getPosition().y - enemyRadius) && (player.getBody().getPosition().y)
					<= (enemy3.getBody3().getPosition().y + enemyRadius)) {
					Die.dieMessage(); //calls the method which controls the JOptionPanes for player death
			}
		}
	}
}
