package aoop.asteroids.multiplayer;

import aoop.asteroids.model.Game;

import java.io.Serializable;

public class DataPacket_Game implements Serializable {
    public Game gameSnapshot;

    public DataPacket_Game(Game snapshot) {
        this.gameSnapshot = snapshot;
    }
}
