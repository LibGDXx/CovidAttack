package com.mygdx.game;

import javax.swing.*;

public class Win {
    public static void winMessage() {
        int selection = JOptionPane.showConfirmDialog(null, "Congratulations you win! Thanks for playing!",
                "Game Over", JOptionPane.DEFAULT_OPTION);
        boolean isYes = (selection == JOptionPane.YES_OPTION);
        if (isYes) {
            System.exit(0);
        }
    }
}
