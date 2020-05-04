package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

public class CovidAttack extends ApplicationAdapter {
	private static final float SCALE = 2.0f;
	public static final float PIXEL_PER_METER = 32f;
	private static final float TIME_STEP = 1 / 60f;
	private static final int VELOCITY_ITERATIONS = 6;
	private static final int POSITION_ITERATIONS = 2;
	private static final float VELOCITY_Y = -9.85f;
	private static final float VELOCITY_X = 0f;
	private static final String MAP_PATH = "map/GameMap.tmx";
	private OrthographicCamera orthographicCamera;
	private Box2DDebugRenderer box2DDebugRenderer;
	private World world;
	private Player player;
	private Enemy enemy;
	private SpriteBatch batch;
	private OrthogonalTiledMapRenderer tiledMapRenderer;
	private TiledMap tiledMap;
	private static final int FRAME_COLS = 2;
	private static final int FRAME_ROWS = 2;
	private static final int FRAME_ROWS1 = 2;
	private static final int FRAME_COLS1 = 2;
	Animation<TextureRegion> jumpAnimation;
	Animation<TextureRegion> idleAnimation;
	float stateTime;
	public static int state;
	private Texture playerJump;
	private Texture playerIdle;
	private Texture enemy1Texture;
//	private Texture player1;
//	private Rectangle rectPlayer;
//	private Rectangle rectEnemy;
//	private Sprite playerSprite;
//	private Sprite enemy1;
//	private float yPosition = -40;
//	private float player1X;
//	private float player1Y;

	@Override
	public void create () {
		orthographicCamera = new OrthographicCamera();
		orthographicCamera.setToOrtho(false, Gdx.graphics.getWidth() / SCALE, Gdx.graphics.getHeight() / SCALE);
		world = new World(new Vector2(VELOCITY_X, VELOCITY_Y), false);
		world.setContactListener(new WorldContactListener());
		batch = new SpriteBatch();
		enemy1Texture = new Texture(Enemy.ENEMY_IMG_PATH);
		playerJump = new Texture(Gdx.files.internal("Jumping.png"));

		TextureRegion[][] tmp2 = TextureRegion.split(playerJump, playerJump.getWidth() / FRAME_COLS, playerJump.getHeight() / FRAME_ROWS);

		TextureRegion[] jumpFrame1 = new TextureRegion[FRAME_COLS * FRAME_ROWS];
		int index2 = 0;
		for(int i = 0; i < FRAME_ROWS; i++){
			for(int j = 0; j < FRAME_COLS; j++){
				jumpFrame1[index2++] = tmp2[i][j];
			}
		}

		jumpAnimation = new Animation<TextureRegion>(.10f, jumpFrame1);

		playerIdle = new Texture(Gdx.files.internal("Character-idle.png"));

		TextureRegion[][] tmp3 = TextureRegion.split(playerIdle, playerIdle.getWidth() / FRAME_COLS1, playerIdle.getHeight() / FRAME_ROWS1);

		TextureRegion[] idleFrame = new TextureRegion[FRAME_COLS1 * FRAME_ROWS1];
		int index3 = 0;
		for(int i = 0; i < FRAME_ROWS1; i++){
			for(int j = 0; j < FRAME_COLS1; j++){
				idleFrame[index3++] = tmp3[i][j];
			}
		}

		idleAnimation = new Animation<TextureRegion>(.05f, idleFrame);

		box2DDebugRenderer = new Box2DDebugRenderer();
		tiledMap = new TmxMapLoader().load(MAP_PATH);
		tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
		MapParser.parseMapLayers(world, tiledMap);
		player = new Player(world);
		enemy = new Enemy(world);

//		player1 = new Texture(Gdx.files.internal("Character-single.png"));
//		playerSprite = new Sprite(player1);
//		player1X = 300;
//		 player1Y = 0;
//
//		enemy1 = new Sprite(new Texture("enemy1.png"));
//		enemy1.setPosition(260, 580);
//
//		rectEnemy = new Rectangle(enemy1.getX(), enemy1.getY(), enemy1.getWidth(), enemy1.getHeight());
//		rectPlayer = new Rectangle(playerSprite.getX(), playerSprite.getY(), playerSprite.getWidth(), playerSprite.getHeight());

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
		enemy1Draw();

//		batch.draw(player1, player.getBody().getPosition().x * PIXEL_PER_METER - (player1.getWidth() / 2),
//				player.getBody().getPosition().y * PIXEL_PER_METER - (player1.getHeight() / 2));
//		rectPlayer = playerSprite.getBoundingRectangle();
//		rectEnemy = enemy1.getBoundingRectangle();
//
//		boolean isOverlapping = rectPlayer.overlaps(rectEnemy);
//		if(!isOverlapping){
//			System.out.println("not overlap");
//			yPosition = yPosition + (20);
//		}
//		else{
//			world.destroyBody(player.getBody());
//		}

		if((player.getBody().getPosition().x == enemy.getBody1().getPosition().x)){
			world.destroyBody(player.getBody());
//			world.step(0,0,0);
//			player.getBody().setActive(false);
		}
		switch(state){
			default:
				currentFrame = idleAnimation.getKeyFrame(stateTime, true);
				batch.draw(currentFrame, player.getBody().getPosition().x * PIXEL_PER_METER - (playerIdle.getWidth() / 2),
						player.getBody().getPosition().y * PIXEL_PER_METER - (playerIdle.getHeight() / 2));
				break;
			case 1:
				currentFrame = jumpAnimation.getKeyFrame(stateTime, true);
				batch.draw(currentFrame, player.getBody().getPosition().x * PIXEL_PER_METER - (playerJump.getWidth() / 2),
						player.getBody().getPosition().y * PIXEL_PER_METER - (playerJump.getHeight() / 2));
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
		//player1.dispose();
	}
	private void update() {
		world.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
		inputUpdate();
		cameraUpdate();
		tiledMapRenderer.setView(orthographicCamera);
		batch.setProjectionMatrix(orthographicCamera.combined);
	}
	private void cameraUpdate() {
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
	private void inputUpdate() {
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
				state = 1;
			}
		}
		player.getBody().setLinearVelocity(horizontalForce * Player.RUN_FORCE, player.getBody().getLinearVelocity().y);
	}
	private void enemy1Draw(){
		batch.draw(enemy1Texture, enemy.getBody1().getPosition().x * PIXEL_PER_METER - (enemy1Texture.getWidth() / 2),
				enemy.getBody1().getPosition().y * PIXEL_PER_METER - (enemy1Texture.getHeight() / 2));
	}

	private void playerUpdate(int horizontalForce, boolean isJumping) {
		if (player.isDead()) {
			world.destroyBody(player.getBody());
			player = new Player(world);
		}
		if (isJumping)
			player.getBody().applyForceToCenter(0, Player.JUMP_FORCE, false);
		player.getBody().setLinearVelocity(horizontalForce * Player.RUN_FORCE, player.getBody().getLinearVelocity().y);
	}
}

//https://stackoverflow.com/questions/42829931/libgdx-collision-detection-between-sprites