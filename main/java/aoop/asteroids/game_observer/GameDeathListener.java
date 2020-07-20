package aoop.asteroids.game_observer;

public interface GameDeathListener {
    /**
     * This method is called so all listeners are notified of the game ended
     *
     */
    public void onGameEnded();
    public void onGameStart();
}
