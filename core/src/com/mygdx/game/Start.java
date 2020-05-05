package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;

import javax.swing.*;

public class Start {

    public static void restart(){

        int selection;
        boolean isYes;
        selection = JOptionPane.showConfirmDialog(null, "You died! Would you like to play again?");
        isYes = (selection == JOptionPane.YES_OPTION);
        boolean isNo = (selection == JOptionPane.NO_OPTION);
        if(isYes == true){
           //Restart game. don't know how yet.
            CovidAttack c = new CovidAttack();
            c.create();
        }
        else if(isNo){
            System.exit(0);
//            JOptionPane.showConfirmDialog(null, "Bye");
        }
    }
}
