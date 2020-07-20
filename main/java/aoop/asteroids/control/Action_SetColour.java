package aoop.asteroids.control;

import aoop.asteroids.model.Game;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class Action_SetColour extends AbstractAction {
    private Game game;
    public Action_SetColour(Game game){
        super("Set Colour");
        this.game=game;
    }
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        try {
            this.game.setColour(Integer.parseInt(JOptionPane.showInputDialog("1: RED 2: GREEN 3: MAGENTA 4: CYAN")));
        }catch(NumberFormatException ex){
            System.err.println("Incorrect input given.");
        }
    }
}
