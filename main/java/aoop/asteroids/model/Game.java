package aoop.asteroids.model;

import aoop.asteroids.control.GameUpdater;
import aoop.asteroids.control.MultiplayerGameUpdater;
import aoop.asteroids.game_observer.GameDeathListener;
import aoop.asteroids.game_observer.GameModeInterface;
import aoop.asteroids.game_observer.ObservableGame;

import java.awt.*;
import java.io.Serializable;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * This class is the main model for the Asteroids game. It contains all game objects, and has methods to start and stop
 * the game.
 * <p>
 * This is strictly a model class, containing only the state of the game. Updates to the game are done in
 * {@link GameUpdater}, which runs in its own thread, and manages the main game loop and physics updates.
 */
public class Game extends ObservableGame implements Serializable, GameModeInterface {
    /**
     * The spaceship object that the player is in control of.
     */
    protected Spaceship ship;

    /**
     * The list of all bullets currently active in the game.
     */
    private Collection<Bullet> bullets;

    /**
     * The list of all asteroids in the game.
     */
    protected Collection<Asteroid> asteroids;

    /**
     * Indicates whether or not the game is running. Setting this to false causes the game to exit its loop and quit.
     */
    transient protected volatile boolean running = false;

    /**
     * The game updater thread, which is responsible for updating the game's state as time goes on.
     */
    transient Thread gameUpdaterThread;
    private ArrayList<InetAddress> clientList = new ArrayList<>();

    /**
     * Constructs a new game, with a new spaceship and all other model data in its default starting state.
     */
    public Game() {
        this.ship = new Spaceship();
        this.initializeGameData();
    }

    /**
     * Initializes all of the model objects used by the game. Can also be used to reset the game's state back to a
     * default starting state before beginning a new game.
     */
    public void initializeGameData() {
        this.bullets = new ArrayList<>();
        this.asteroids = new ArrayList<>();
        this.ship.reset();
        this.ship.setGameMode(currentGameMode);
        this.spaceships = new ArrayList<>();
    }

    public void initializeGameData(boolean isMultiplayer, boolean isHost, GameMode gameMode) {
        this.currentGameMode = gameMode;
        this.isMultiplayer = isMultiplayer;
        this.hosting = isHost;
        initializeGameData();
    }

    transient private boolean hosting;
    private GameMode currentGameMode;

    /**
     * @return The Current Game Mode Type
     */
    public GameMode getCurrentGameMode() {
        return this.currentGameMode;
    }

    /**
     * @param mode Set the current Game Mode type
     */
    public void setCurrentGameMode(GameMode mode) {
        this.currentGameMode = mode;
    }

    /**
     * @return Get Host Address
     */
    public InetAddress getHostAddress() {
        return this.host;
    }

    /**
     * @param address Set Host Address (Input String -> InetAddress)
     */
    public void setHostAddress(String address) {
        try {
            this.host = InetAddress.getByName(address);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * HOST PORT NUMBER
     */
    transient private int hostPort;
    /**
     * HOST IP ADDRESS
     */
    transient public InetAddress host;

    public void setHostPort(int port) {
        this.hostPort = port;
    }

    /**
     * @return The game's spaceship.
     */
    public Spaceship getSpaceship() {
        return this.ship;
    }

    /**
     * @return The collection of asteroids in the game.
     */
    public Collection<Asteroid> getAsteroids() {
        return this.asteroids;
    }

    /**
     * @return The collection of bullets in the game.
     */
    public Collection<Bullet> getBullets() {
        return this.bullets;
    }

    /**
     * @return Whether or not the game is running.
     */
    public synchronized boolean isRunning() {
        return this.running;
    }

    /**
     * @return True if the player's ship has been destroyed, or false otherwise.
     */
    public boolean isGameOver() {
        boolean result = this.ship.isDestroyed();
        if (currentGameMode == GameMode.Singleplayer && result) {
            quit();
        }
        return result;
    }

    /**
     * Using this game's current model, spools up a new game updater thread to begin a game loop and start processing
     * * user input and physics updates. Only if the game isn't currently running, that is. If the game is multiplayer,
     * the multiplayer game updater is started.
     */
    public void start() {
        if (!this.running) {
            this.running = true;
            this.gameUpdaterThread = new Thread(new GameUpdater(this));
            this.gameUpdaterThread.start();
            announceStart();
            if (isMultiplayer) {
                MultiplayerGameUpdater multiplayerGameUpdater = new MultiplayerGameUpdater(this, this.gameUpdaterThread);
                this.multiplayerGameUpdater = new Thread(multiplayerGameUpdater);
                this.multiplayerGameUpdater.start();
            }
        }
    }

    /**
     * The Thread used for all Game Updates which are non-physics and multiplayer related
     */
    private transient Thread multiplayerGameUpdater;

    /**
     * Tries to quit the game, if it is running.
     */
    public void quit() {
        if (this.running) {
            try { // Attempt to wait for the game updater to exit its game loop.
                this.gameUpdaterThread.join(100);
                if(isMultiplayer) {
                    this.multiplayerGameUpdater.join(100);
                }
                announceDeath();
            }
            catch (InterruptedException exception) {
                System.err.println("Interrupted while waiting for the game updater thread to finish execution.");
            } finally {
                this.running = false;
                this.gameUpdaterThread = null; // Throw away the game updater thread and let the GC remove it.
            }
        }
    }

    /**
     * The List of Spaceships in the Game
     */
    private Collection<Spaceship> spaceships;

    /**
     * @return Get the list of spaceships currently in the game
     */
    public Collection<Spaceship> getSpaceships() {
        return this.spaceships;
    }

    public void addSpaceship(Spaceship spaceship) {
        this.spaceships.add(spaceship);
    }

    /**
     * @param list Recieved list of Spaceships via UDP [CLIENT]
     *             Clear the spaceship list, add new list.
     */
    public void setSpaceships(Collection<Spaceship> list) {
        this.spaceships.clear();
        this.spaceships = list;
    }

    public boolean isHosting() {
        return hosting;
    }

    public int getHostPort() {
        return this.hostPort;
    }

    private boolean isMultiplayer = false;

    public void setMultiplayer(boolean x) {
        isMultiplayer = x;
    }

    public boolean isMultiplayer() {
        return this.isMultiplayer;
    }

    /**
     * @param bullet Bullet recieved via UDP
     *               Update the existing objects physics,
     *               otherwise add.
     */
    public void addBullet(Bullet bullet) {
        Iterator<Bullet> bulletIterator = bullets.iterator();
        Bullet temp;
        boolean exists = false;
        while (bulletIterator.hasNext()) {
            temp = bulletIterator.next();
            if (temp.getMultiplayerID() == bullet.getMultiplayerID()) {
                temp.setVelocity(bullet.velocity.x, bullet.velocity.y);
                temp.setLocation(bullet.location.x, bullet.location.y);
                exists = true;
            }
        }
        if (!exists) {
            bullets.add(bullet);
        }
    }

    /**
     * @param list Recieves a list of Asteroids
     *             Clears current asteroids list, replaces it with the recieved one
     *             Utilised in Networking on CLIENT side
     */
    public void setAsteroids(Collection<Asteroid> list) {
        this.asteroids.clear();
        this.asteroids = list;
    }

    /**
     * @param ip Recieved IP Address (Extracted from incoming packets)
     *           Check if IP is already connected or not, add appropiately
     */
    public void addClient(InetAddress ip) {
        if (!clientList.contains(ip)) {
            clientList.add(ip);
        }
    }

    /**
     * @return The list of connected clients.
     * Utilised by Packet Sender whilst Relaying Packets to all connected clients [HOSTING]
     */
    public ArrayList<InetAddress> getClients() {
        return this.clientList;
    }
    public void setColour(int i){
        switch (i){
                case 1:
                    this.ship.setColour(Color.RED);
                    break;
                case 2:
                    this.ship.setColour(Color.GREEN);
                    break;
                case 3:
                    this.ship.setColour(Color.MAGENTA);
                    break;
                case 4:
                    this.ship.setColour(Color.CYAN);
                    break;
        }
    }

    public String getUsername() {
        return this.ship.getUsername();
    }

    public void setUsername(String username) {
        this.ship.setUsername(username);
    }

}
