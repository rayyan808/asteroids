package aoop.asteroids.control;

import aoop.asteroids.game_observer.GameModeInterface;
import aoop.asteroids.model.Game;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * This is an Action Listener for the 'Host Multiplayer Game' option in the Main Menu
 */
public class Action_HostGameCOOP extends AbstractAction implements GameModeInterface {
    /**
     * A reference to the game that should be reset/initialized when the user does this action.
     */
    private Game game;

    /**
     * Constructs the action. Calls the parent constructor to set the name of this action.
     *
     * @param game The game model that will be used.
     */
    public Action_HostGameCOOP(Game game) {
        super("Host Game [COOP]");
        this.game = game;
    }

    /**
     * @param actionEvent When Host Game is clicked, the game should be initiliazed with isHosting = true, isMultiplayer = True
     *                    Host Thread should begin running to accept connections.
     */
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        this.game.quit(); // Try to stop the game if it's currently running.
        this.game.initializeGameData(true, true, GameMode.COOP); // Resets the game's objects to their default state.
        this.game.setHostPort(25665);
        this.game.start(); // Spools up the game's engine and starts the main game loop.
    }
}
