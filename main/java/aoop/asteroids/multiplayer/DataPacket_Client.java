package aoop.asteroids.multiplayer;

import aoop.asteroids.model.Bullet;
import aoop.asteroids.model.Spaceship;

import java.io.Serializable;
import java.util.Collection;

public class DataPacket_Client implements Serializable {
    public Collection<Bullet> bulletList;
    public Spaceship player;
}
