package aoop.asteroids.control;

import aoop.asteroids.model.Spaceship;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * This class is responsible for handling keyboard input for a single player that is bound to a ship.
 */
public class PlayerKeyListener implements KeyListener {
    /**
     * The key that, when pressed, causes the ship to accelerate.
     */
    private static final int ACCELERATION_KEY = KeyEvent.VK_W;

    /**
     * The key that turns the ship left, or counter-clockwise.
     */
    private static final int LEFT_KEY = KeyEvent.VK_A;

    /**
     * The key that turns the ship right, or clockwise.
     */
    private static final int RIGHT_KEY = KeyEvent.VK_D;

    /**
     * The key that causes the ship to fire its weapon.
     */
    private static final int FIRE_WEAPON_KEY = KeyEvent.VK_SPACE;

    /**
     * The spaceship that will respond to key events caught by this listener.
     */
    private Spaceship ship;

    /**
     * Constructs a new player key listener to control the given ship.
     *
     * @param ship The ship that this key listener will control.
     */
    public PlayerKeyListener(Spaceship ship) {
        this.ship = ship;
    }

    /**
     * This method is invoked when a key is pressed and sets the corresponding fields in the spaceship to true.
     *
     * @param event Key event that triggered the method.
     */
    @Override
    public void keyPressed(KeyEvent event) {
        switch (event.getKeyCode()) {
            case ACCELERATION_KEY:
                this.ship.setAccelerateKeyPressed(true);
                break;
            case LEFT_KEY:
                this.ship.setTurnLeftKeyPressed(true);
                break;
            case RIGHT_KEY:
                this.ship.setTurnRightKeyPressed(true);
                break;
            case FIRE_WEAPON_KEY:
                this.ship.setIsFiring(true);
        }
    }

    /**
     * This method is invoked when a key is released and sets the corresponding fields in the spaceship to false.
     *
     * @param event Key event that triggered the method.
     */
    @Override
    public void keyReleased(KeyEvent event) {
        switch (event.getKeyCode()) {
            case ACCELERATION_KEY:
                this.ship.setAccelerateKeyPressed(false);
                break;
            case LEFT_KEY:
                this.ship.setTurnLeftKeyPressed(false);
                break;
            case RIGHT_KEY:
                this.ship.setTurnRightKeyPressed(false);
                break;
            case FIRE_WEAPON_KEY:
                this.ship.setIsFiring(false);
        }
    }

    /**
     * This method doesn't do anything, but we must provide an empty implementation to satisfy the contract of the
     * KeyListener interface.
     *
     * @param event Key event that triggered the method.
     */
    @Override
    public void keyTyped(KeyEvent event) {
    }
}
