package aoop.asteroids.game_observer;

import java.util.ArrayList;
import java.util.List;

/**
 * An observable game is an object that game update listeners can register to, so that when the game updates, they will
 * be able to react to it.
 * <p>
 * Since Java has deprecated the official Observable and Observer objects, this class serves as a custom implementation
 * of the Observable class that is suited to the uses of this game.
 */
public abstract class ObservableGame {
    /**
     * The list of listeners that will be notified when the game updates.
     */
    private List<GameUpdateListener> listeners;
    /**
     * The list of listeners that will be notified when the game ends
     */
    private List<GameDeathListener> deathListeners;
    /**
     * Constructs a new observable game with initially no listeners.
     */
    protected ObservableGame() {
        this.listeners = new ArrayList<>();
        this.deathListeners=new ArrayList<>();
    }

    /**
     * Adds the given listener to the list of listeners that will get notified when the game updates.
     *
     * @param listener The listener to add.
     */
    public void addListener(GameUpdateListener listener) {
        this.listeners.add(listener);
    }
    public void addDeathListener(GameDeathListener listener) { this.deathListeners.add(listener); }
    /**
     * Removes a specific listener from the game.
     *
     * @param listener The listener to remove.
     */
    public void removeListener(GameUpdateListener listener) {
        this.listeners.remove(listener);
    }

    /**
     * Tells all the registered listeners that their representation of the game should be updated.
     *
     * @param timeSinceLastTick The number of milliseconds that have passed since the last game tick occurred. This is
     *                          used so that things like a display may continue showing an animated model while no
     *                          actual physics update has been done by the game engine.
     */
    public void notifyListeners(long timeSinceLastTick) {
        listeners.forEach(listener -> listener.onGameUpdated(timeSinceLastTick));
    }

    /**
     * Announces the death of a Game to all listeners
     */
    public void announceDeath(){
        deathListeners.forEach(obs -> obs.onGameEnded());
    }

    /**
     * Announces the start of a Game to all listeners
     */
    public void announceStart(){
        deathListeners.forEach(obs -> obs.onGameStart());
    }
}
