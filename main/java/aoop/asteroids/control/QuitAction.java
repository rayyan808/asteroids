package aoop.asteroids.control;

import aoop.asteroids.model.Game;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * An action that represents when a user indicates that they wish to quit the application.
 */
public class QuitAction extends AbstractAction {
    private Game game;
    /**
     * Construct a new quit action. This calls the parent constructor to give the action a name.
     */
    public QuitAction(Game game) {
        super("Quit");
        this.game=game;
    }

    /**
     * Invoked when an action occurs.
     *
     * @param event The event to be processed.
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        this.game.quit();
    }
}
