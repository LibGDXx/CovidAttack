package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.CovidAttack;

import javax.swing.*;
//import com.mygdx.game.Start;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		//		new LwjglApplication(new Start(), config);
		config.width = 500;
		config.height = 500;
		config.title = "CovidAttack";

		int selection = JOptionPane.showConfirmDialog(null, "Would you like to play CovidAttack?",
				"CovidAttack", JOptionPane.YES_NO_OPTION);
		boolean isYes = (selection == JOptionPane.YES_OPTION);
		boolean isNo = (selection == JOptionPane.NO_OPTION);
		boolean isPlay = (selection == JOptionPane.YES_OPTION);
		if(isYes == true){
			new LwjglApplication(new CovidAttack(), config);
		}
		else if(isNo == true){
			JOptionPane.showMessageDialog(null, "You have no choice.", "CovidAttack", JOptionPane.PLAIN_MESSAGE);
			if(isPlay ==  true){
				new LwjglApplication(new CovidAttack(), config);
			}
		}
	}
}
