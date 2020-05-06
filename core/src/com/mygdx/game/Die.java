package com.mygdx.game;

import com.badlogic.gdx.ApplicationListener;

import javax.swing.*;

public class Die {
    public static void dieMessage(){

        int selection = JOptionPane.showConfirmDialog(null, "You died! Would you like to play again?",
                "Game Over", JOptionPane.INFORMATION_MESSAGE);
        boolean isYes = (selection == JOptionPane.YES_OPTION);
        boolean isNo = (selection == JOptionPane.NO_OPTION);
        if(isYes == true){
            //need to bring game back to title screen. not sure how yet.

        }
        else if(isNo){
            System.exit(0);
            //JOptionPane.showMessageDialog(null, "Bye");
        }
        else{
            //cancel button
        }
    }
}
