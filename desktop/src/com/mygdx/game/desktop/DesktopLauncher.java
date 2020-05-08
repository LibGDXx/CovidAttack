package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.CovidAttack;

import javax.swing.ImageIcon;
import javax.swing.*;

public class DesktopLauncher {
	public static void main (String[] arg) {
		ImageIcon title = new ImageIcon("TitleScreen.png"); //change this path to whatever the absolute path of TitleScreen.png is on your computer
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 500;
		config.height = 500;
		config.title = "CovidAttack";

		int selection = JOptionPane.showConfirmDialog(null, title,
				"CovidAttack",   JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
		boolean isYes = (selection == JOptionPane.YES_OPTION);
		boolean isNo = (selection == JOptionPane.NO_OPTION);
		boolean isPlay = (selection == JOptionPane.YES_OPTION);
		if(isYes == true){
			new LwjglApplication(new CovidAttack(), config);
		}
		else if(isNo == true){
			JOptionPane.showMessageDialog(null, "Bye!", "CovidAttack", JOptionPane.PLAIN_MESSAGE);
			if(isPlay ==  true){
				new LwjglApplication(new CovidAttack(), config);
			}
		}
	}
}
