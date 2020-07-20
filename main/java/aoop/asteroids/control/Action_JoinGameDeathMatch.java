package aoop.asteroids.control;

import aoop.asteroids.game_observer.GameModeInterface;
import aoop.asteroids.model.Game;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * This is an Action Listener for the 'Join Multiplayer Game' option in the Main Menu
 */
public class Action_JoinGameDeathMatch extends AbstractAction implements GameModeInterface {
    /**
     * A reference to the game that should be reset/initialized when the user does this action.
     */
    private Game game;
    private String hostIP, port;

    /**
     * Constructs the action. Calls the parent constructor to set the name of this action.
     *
     * @param game The game model that will be used.
     */
    public Action_JoinGameDeathMatch(Game game, String ip, String port) {
        super("Join Game [DEATH MATCH]");
        this.game = game;
        this.port = port;
        this.hostIP = ip;
    }

    /**
     * @param actionEvent When Host Game is clicked, the game should be initiliazed with isHosting = true, isMultiplayer = True
     *                    Host Thread should begin running to accept connections.
     */
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if(hostIP != null && port != null) {
            this.game.quit(); // Try to stop the game if it's currently running.
            this.game.initializeGameData(true, false, GameMode.Deathmatch); // Resets the game's objects to their default state.
            this.game.setHostPort(Integer.parseInt(port));
            this.game.setHostAddress(hostIP);
            this.game.start(); // Spools up the game's engine and starts the main game loop.
        }
    }
}
