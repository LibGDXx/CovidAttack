package com.mygdx.game;

import javax.swing.*;

public class Start {
    public static void restart(){


        int selection = JOptionPane.showConfirmDialog(null, "You died! Would you like to play again?",
                "Game Over", JOptionPane.INFORMATION_MESSAGE);
        boolean isYes = (selection == JOptionPane.YES_OPTION);
        boolean isNo = (selection == JOptionPane.NO_OPTION);
        if(isYes == true){
           //Restart game. don't know how yet.
            //Maybe I can eventually call the title screen class and that will lead into the CovidAttack class somehow...
//            CovidAttack c = new CovidAttack();
//            c.create();

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
