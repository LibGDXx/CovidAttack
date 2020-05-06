package com.mygdx.game;

import com.badlogic.gdx.ApplicationListener;

import javax.swing.*;

public class Die {
    public static void dieMessage(){

        int selection = JOptionPane.showConfirmDialog(null, "You died! Would you like to play again?",
                "Game Over", JOptionPane.YES_NO_OPTION);
        boolean isYes = (selection == JOptionPane.YES_OPTION);
        boolean isNo = (selection == JOptionPane.NO_OPTION);
        if(isYes == true){
            //need to bring game back to title screen. not sure how yet.
            //call DesktopLauncher somehow

        }
        else if(isNo){
            boolean isPlay = (selection == JOptionPane.DEFAULT_OPTION);
            JOptionPane.showMessageDialog(null, "Bye", "CovidAttack", JOptionPane.PLAIN_MESSAGE);
            System.exit(0);
        }
    }
}
