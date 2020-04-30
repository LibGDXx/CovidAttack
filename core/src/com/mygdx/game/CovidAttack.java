package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

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
	Texture jump;
	Texture idle;
	private Texture home;
	private Texture start;
	private Texture stop;
	public BitmapFont font;



	@Override
	public void create () {
		home = new Texture("C:\\Users\\Joey Chalupa\\Desktop\\Java\\CovidAttack\\android\\assets\\TitleScreen.png");
		start = new Texture("C:\\Users\\Joey Chalupa\\Desktop\\Java\\CovidAttack\\android\\assets\\new start.png");

		orthographicCamera = new OrthographicCamera();
		orthographicCamera.setToOrtho(false, Gdx.graphics.getWidth() / SCALE, Gdx.graphics.getHeight() / SCALE);
		world = new World(new Vector2(VELOCITY_X, VELOCITY_Y), false);
		world.setContactListener(new WorldContactListener());
		batch = new SpriteBatch();
		font = new BitmapFont();

		jump = new Texture(Gdx.files.internal("Jumping.png"));

		TextureRegion[][] tmp2 = TextureRegion.split(jump, jump.getWidth() / FRAME_COLS, jump.getHeight() / FRAME_ROWS);

		TextureRegion[] jumpFrame1 = new TextureRegion[FRAME_COLS * FRAME_ROWS];
		int index2 = 0;
		for(int i = 0; i < FRAME_ROWS; i++){
			for(int j = 0; j < FRAME_COLS; j++){
				jumpFrame1[index2++] = tmp2[i][j];
			}
		}

		jumpAnimation = new Animation<TextureRegion>(.10f, jumpFrame1);

		idle = new Texture(Gdx.files.internal("Character-idle.png"));

		TextureRegion[][] tmp3 = TextureRegion.split(idle, idle.getWidth() / FRAME_COLS1, idle.getHeight() / FRAME_ROWS1);

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

	}
	@Override
	public void render (SpriteBatch sb) {
		super.render();
		sb.begin();
		sb.draw(home, 0, 0, 500, 500);
		sb.draw(start,  250-(start.getWidth() / 2), 250);
		sb.end();


		update();
		Gdx.gl.glClearColor(0.5f, 0.8f, 1f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		tiledMapRenderer.render();
		stateTime += Gdx.graphics.getDeltaTime();
		TextureRegion currentFrame;
		batch.begin();
		switch(state){
			default:
				currentFrame = idleAnimation.getKeyFrame(stateTime, true);
				batch.draw(currentFrame, player.getBody().getPosition().x * PIXEL_PER_METER - (idle.getWidth() / 2),
				player.getBody().getPosition().y * PIXEL_PER_METER - (idle.getHeight() / 2));
				break;
			case 1:
				currentFrame = jumpAnimation.getKeyFrame(stateTime, true);
				batch.draw(currentFrame, player.getBody().getPosition().x * PIXEL_PER_METER - (jump.getWidth() / 2),
						player.getBody().getPosition().y * PIXEL_PER_METER - (jump.getHeight() / 2));
				break;
		}
		batch.end();
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		game.batch.setProjectionMatrix(camera.combined);

		game.batch.begin();
		game.font.draw(game.batch, "Welcome to Covid Attack!!! ", 100, 150);
		game.font.draw(game.batch, "Tap anywhere to begin!", 100, 100);
		game.batch.end();

		if (Gdx.input.isTouched()) {
			game.setScreen(new GameScreen(game));
			dispose();
		}
	}
	@Override
	public void dispose() {
		batch.dispose();
		font.dispose();
		jump.dispose();
		idle.dispose();
		box2DDebugRenderer.dispose();
		world.dispose();
		tiledMapRenderer.dispose();
		tiledMap.dispose();
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