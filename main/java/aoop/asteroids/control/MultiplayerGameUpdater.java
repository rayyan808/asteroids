package aoop.asteroids.control;

import aoop.asteroids.game_observer.GameModeInterface;
import aoop.asteroids.model.Bullet;
import aoop.asteroids.model.Game;
import aoop.asteroids.model.Spaceship;
import aoop.asteroids.multiplayer.DataPacket_Client;
import aoop.asteroids.multiplayer.DataPacket_Game;
import aoop.asteroids.multiplayer.PacketSender;
import aoop.asteroids.multiplayer.ListenerThread;

import javax.swing.*;
import javax.xml.crypto.Data;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * A specialized updater for Multiplayer games. Depending on HOST/CLIENT variable, it will transmit it's relevant data after game actions
 * This classes intention is to add multiplayer functionality on top of the Game Updater
 */
public class MultiplayerGameUpdater extends Thread implements GameModeInterface {
    private boolean isHosting;
    private final Game currentGame;
    private final PacketSender packetSender;
    private final ListenerThread recieverThread;
    private final Thread gameUpdater;

    public MultiplayerGameUpdater(Game game, Thread gameUpdater) {
        ;
        this.currentGame = game;
        this.gameUpdater = gameUpdater;
        this.isHosting = game.isHosting();
        this.hostAddress = game.getHostAddress();
        this.destPort = game.getHostPort();

        if (!isHosting) {
            //Not a Host means Packet Sender  host IP and Port
            this.packetSender = new PacketSender(game, game.getHostAddress(), game.getHostPort());
            this.packetSender.start();
        } else {
            //Hosting means Packet Sender requires just game
            this.packetSender = new PacketSender(game);
            this.packetSender.start();
        }
        this.recieverThread = new ListenerThread(game.isHosting(), this);
        this.recieverThread.start();
    }

    /**
     * Called on every game update in synchronization with the Game Updater Thread
     * and the Game Thread
     * SENDS GAME INFO
     */
    public void onGameUpdate() {
        //Access all GameObjects, Retrieve Required Information and send to Packet Sender
        synchronized (this.currentGame) {
            if (this.currentGame.getCurrentGameMode() != GameMode.Spectate) {
                if (isHosting) {
                    packetSender.setGameSnapshot(new DataPacket_Game(currentGame));
                    sendData();
                } else {
                    DataPacket_Client clientSnapshot = new DataPacket_Client();
                    clientSnapshot.player = this.currentGame.getSpaceship();
                    clientSnapshot.bulletList = this.currentGame.getBullets();
                    packetSender.setClientSnapshot(clientSnapshot);
                    sendData();
                }
            }
            else{
                DataPacket_Client emptySnapshot = new DataPacket_Client();
                emptySnapshot.player=this.currentGame.getSpaceship();
                packetSender.setClientSnapshot(emptySnapshot);
                sendData();
            }
            this.currentGame.notify();
        }
    }

    /**
     * @param gameSnapshot CLIENT CALLS THIS METHOD TO UPDATE ITS GAME
     *                     DEATHMATCH: Will not need to recieve Asteroids
     */
    public synchronized void updateGameSnapshot(DataPacket_Game gameSnapshot) {
        synchronized (this.currentGame) {
            if (this.currentGame.getCurrentGameMode() != GameMode.Deathmatch) {
                this.currentGame.setAsteroids(gameSnapshot.gameSnapshot.getAsteroids());
            }
            Iterator<Bullet> iterator = gameSnapshot.gameSnapshot.getBullets().iterator();
            Bullet newBullet;
            while (iterator.hasNext()) {
                newBullet = iterator.next();
                this.currentGame.addBullet(newBullet);
            }
            Collection<Spaceship> emptyList = new ArrayList<>();
            this.currentGame.setSpaceships(emptyList);
            Iterator<Spaceship> connectedShips = gameSnapshot.gameSnapshot.getSpaceships().iterator();
            while (connectedShips.hasNext()) {
                Spaceship nextShip = connectedShips.next();
                if (nextShip.getMultiplayerID() != currentGame.getSpaceship().getMultiplayerID()) {
                    //As long as the recieve spaceship is NOT our spaceship, add it to the list
                    this.currentGame.addSpaceship(nextShip);
                }
            }
            Spaceship hostSpaceship = gameSnapshot.gameSnapshot.getSpaceship();
            this.currentGame.addSpaceship(hostSpaceship);
            //Set Client COOP Score to Host's score (Host increments its score with clients score and returns it
            if (this.currentGame.getCurrentGameMode() == GameMode.COOP) {
                this.currentGame.getSpaceship().setCOOPScore(hostSpaceship.getCOOPScore());
            }
            // this.currentGame.getSpaceship().increaseCOOPScore(hostSpaceship.getCOOPScore());
        }
    }

    @Override
    public synchronized void run() {
        while (currentGame.isRunning()) {
            synchronized (this.gameUpdater) {
                try {
                    wait(100);
                    onGameUpdate();
                }
                catch (InterruptedException ex) {
                    ex.printStackTrace();
                    System.err.println("MultiplayerGame Thread was interrupted while waiting for GameUpdater");
                }
            }
            try {
                sleep(10);
            }
            catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        int score = 0;
        switch (currentGame.getCurrentGameMode()) {
            case Singleplayer:
                score = currentGame.getSpaceship().getScore();
            case COOP:
                score = currentGame.getSpaceship().getCOOPScore();
            case Deathmatch: //TODO: Add Kill counter
        }
        JOptionPane.showConfirmDialog(null, "You died! Final score:" + score);
    }

    private InetAddress hostAddress;
    private int destPort;

    private void sendData() {
        if (isHosting) {
            packetSender.relayPacket();
        } else {
            packetSender.sendPacket(hostAddress, destPort);
        }
    }

    /** Passes the recieved client only if it's in the correct game mode OR
     * it is spectating
     * @param clientGameMode The current game mode of the client
     * @param ip Client IP
     */
    public void addClient(GameMode clientGameMode, InetAddress ip) {
        if (clientGameMode == GameMode.Spectate || clientGameMode == this.currentGame.getCurrentGameMode()) {
            this.currentGame.addClient(ip);
        } else {
            System.err.println("Client attempting to connect with incorrect GameMode type. (MGU(165))\n");
        }
    }

    /** Handles the recieved packet
     * SPECTATOR: Host does not need to handle this packet
     * COOP: Host needs to increment the score as it is shared
     * @param newData The recieved DataPacket (assured to be not null)
     */
    public synchronized void acceptClientInfo(DataPacket_Client newData) {
        synchronized (this.currentGame) {
                if (newData.player != null) {
                    Spaceship newShip = newData.player;
                    GameMode newShipGameMode = newShip.getCurrentGameMode();
                    boolean newShipexists = false;
                    if (newShip.getCurrentGameMode() != GameMode.Spectate) {
                    for (Spaceship s : this.currentGame.getSpaceships()) {
                        if(s.isDestroyed()){ //Player Died
                            handleClientDeath(s);
                        }
                        if (s.getMultiplayerID() == newShip.getMultiplayerID()) {
                            s.setLocation(newShip.getLocation().x, newShip.getLocation().y);
                            s.setVelocity(newShip.getVelocity().x, newShip.getVelocity().y);
                            s.setDirection(newShip.getDirection());
                            newShipexists = true;
                            if (this.currentGame.getCurrentGameMode() == GameMode.COOP) {
                                //Set the Master Game COOP Score to the sum of Host + Client Score
                                this.currentGame.getSpaceship().setCOOPScore(currentGame.getSpaceship().getScore() + s.getScore());
                                // s.setCOOPScore(0);
                            }
                            break;
                        }
                    }
                    if (!newShipexists) {
                        this.currentGame.addSpaceship(newShip);
                    }
                }
                if (newData.bulletList != null) {
                    Iterator<Bullet> iterator = newData.bulletList.iterator();
                    Bullet newBullet;
                    while (iterator.hasNext()) {
                        newBullet = iterator.next();
                        this.currentGame.addBullet(newBullet);
                    }
                }
            }
        }
    }
    private void handleClientDeath(Spaceship deadClient){
        GameMode newShipGameMode=deadClient.getCurrentGameMode();
            //Player has died
            switch (newShipGameMode){
                case COOP: //Game continues, player is set to spectate
                    deadClient.setGameMode(GameMode.Spectate);
                case Deathmatch : //Game ends for this player
                  //TODO  this.currentGame.removeSpaceship(deadClient);
            }
    }
}
