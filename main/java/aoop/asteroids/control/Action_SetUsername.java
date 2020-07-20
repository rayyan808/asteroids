package aoop.asteroids.control;

import aoop.asteroids.model.Game;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class Action_SetUsername extends AbstractAction {
        private Game game;
        public Action_SetUsername(Game game){
            super("Set Username");
            this.game=game;
        }
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                String input = JOptionPane.showInputDialog("Enter Username (You may only change it once per session)");
                if(input != null) {
                    this.game.setUsername(input);
                }
            }catch(NumberFormatException ex){
                System.err.println("Incorrect input given.");
            }
        }
}