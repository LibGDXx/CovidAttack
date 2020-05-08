package com.mygdx.game;

import javax.swing.*;

public class Die {
    public static void dieMessage(){
        int selection = JOptionPane.showConfirmDialog(null, "You died! Thanks for playing!",
                "Game Over", JOptionPane.PLAIN_MESSAGE);
        boolean isYes = (selection == JOptionPane.YES_OPTION);
        if(isYes == true){
            System.exit(0);
        }
    }
}
