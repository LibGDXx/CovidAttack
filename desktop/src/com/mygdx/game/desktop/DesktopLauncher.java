package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.CovidAttack;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = CovidAttack.WIDTH;
		config.height = CovidAttack.HEIGHT;
		config.title = CovidAttack.TITLE;
		new LwjglApplication(new CovidAttack(), config);
	}
}
