package com.mygdx.game;

import javax.swing.*;

public class Die {
    public static void dieMessage(){
        //if the user dies, they are prompted with a pop up window telling them they died, and then the game exits.
        int selection = JOptionPane.showConfirmDialog(null, "You died! Thanks for playing!",
                "Game Over", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE);
        boolean isYes = (selection == JOptionPane.YES_OPTION);
        if(isYes){
            System.exit(0);
        }
    }
}
