package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.states.GameStateManager;
import com.mygdx.game.states.MenuState;

public class CovidAttack extends ApplicationAdapter {
	public static final int WIDTH = 960;
	public static final int HEIGHT = 640;

	public static final String TITLE = "Covid Attack";

	private GameStateManager gsm;
	private SpriteBatch batch;
	Texture player;
	Texture background;
	Texture ground;

	@Override
	public void create () {
		batch = new SpriteBatch();
		player = new Texture("Character1.png");
		background = new Texture("Hospital.png");
		ground = new Texture("Hospital Floor.png");
		gsm = new GameStateManager();
		Gdx.gl.glClearColor(1, 0, 0, 1);
		gsm.push(new MenuState(gsm));
	}

	@Override
	public void render () {
		boolean moveRight = (Gdx.input.isKeyPressed(Input.Keys.RIGHT));
		boolean moveLeft = (Gdx.input.isKeyPressed(Input.Keys.LEFT));

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		gsm.update(Gdx.graphics.getDeltaTime());
		batch.begin();
		batch.draw(background, 0, 0, 980, 640);
		batch.draw(ground, 0, 0, 960, 60);
		batch.draw(player, 0, 0, 90, 138);
		batch.end();

		if (moveRight && player.) {

		}
	}

	@Override
	public void dispose () {
		batch.dispose();
		player.dispose();
	}
}

