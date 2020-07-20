package database;

import aoop.asteroids.Asteroids;
import aoop.asteroids.game_observer.GameDeathListener;
import aoop.asteroids.model.Spaceship;

public class GameListener implements GameDeathListener {

    Player player;
    AsteroidsDAO data;
    Spaceship spaceship;
    public GameListener(Spaceship spaceship, AsteroidsDAO ast){
        this.data = ast;
        this.spaceship=spaceship;
        player = new Player();

    }
    @Override
    public void onGameEnded() {
        player.setUsername(spaceship.getUsername());
        player.setScore(spaceship.getScore());
        data.openDataSource();
        data.addPlayer(player);
        data.closeDataSource();
    }

    @Override
    public void onGameStart() {
        //Do nothing
    }
}
