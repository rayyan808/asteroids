package aoop.asteroids.multiplayer;

import aoop.asteroids.model.Game;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.Iterator;

/**
 * Resposible for sending a given Game Snapshot object to specified location(s)
 */
public class PacketSender extends Thread {

    private InetAddress hostAddress;
    private int hostPort;
    private boolean isHosting;
    private DatagramSocket dSock;
    private DataPacket_Game gameSnapshot;
    private DataPacket_Client clientSnapshot;
    private final Game game;

    private ByteArrayOutputStream byteStream;

    /**
     * Constructor called by Host
     * @param game : The Game
     */
    public PacketSender(Game game) {
        this.game = game;
        this.hostPort=game.getHostPort();
        try {
            dSock = new DatagramSocket();
            byteStream = new ByteArrayOutputStream();
        }
        catch (SocketException socks) {
            socks.printStackTrace();
        }
        isHosting = true;
    }

    /**
     * Constructor called by Clients joining a Game,
     * @param game: The Game
     * @param hostIP : HOST IP ADDRESS
     * @param port : HOST PORT
     */
    public PacketSender(Game game, InetAddress hostIP, int port) {
        this.game = game;
        try {
            dSock = new DatagramSocket();
            this.hostAddress = hostIP;
            this.hostPort=port;
            byteStream = new ByteArrayOutputStream();
        }
        catch (SocketException socks) {
            socks.printStackTrace();
        }
        isHosting = false;
    }

    public void sendPacket(InetAddress address, int desPort) {
        synchronized (game) {
            try {
                ObjectOutputStream os = new ObjectOutputStream(byteStream);
                if (isHosting) {
                    os.writeObject(gameSnapshot);
                } else {
                    os.writeObject(clientSnapshot);
                }
                byte[] sendBuf = byteStream.toByteArray();
                //retrieves byte array
                DatagramPacket packet;
                if (address == null) {
                    packet = new DatagramPacket(sendBuf, sendBuf.length, hostAddress, hostPort);
                } else {
                    packet = new DatagramPacket(sendBuf, sendBuf.length, address, hostPort);
                }
                int byteCount = packet.getLength();
                if (byteCount > 64000) {
                    System.err.println("UDP Packet Overload. \n");
                } else {
                    dSock.send(packet);
                //    System.out.println("PACKET SENT TO:" + address + hostPort);
                }
                byteStream.reset();
                os.flush();
                os.close();
            }
            catch (UnknownHostException e) {
                System.err.println("Exception:  " + e);
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Iterates through ALL connected clients and sends them the currently assigned Game Snapshot
     */
    public void relayPacket() {
        Iterator<InetAddress> iterator = game.getClients().iterator();
        while (iterator.hasNext()) {
            InetAddress destIP = iterator.next();
            sendPacket(destIP, hostPort);
        }
    }

    /** Called by the Client to send it's new DataPacket_Client object
     * @param snapshot The game snapshot taken by the client
     */
    public void setClientSnapshot(DataPacket_Client snapshot) {
        this.clientSnapshot = snapshot;
    }

    /** Called by the Host to assign the Packet Sender a new DataPacket_Game
     * @param dPacket The Data Packet to be assigned
     *
     */
    public void setGameSnapshot(DataPacket_Game dPacket) {
        this.gameSnapshot = dPacket;
    }

    @Override
    public void run() {
        while(this.game.isRunning()) {
            if (isHosting && gameSnapshot != null) {
                relayPacket();
            } else if (gameSnapshot != null) {
                sendPacket(hostAddress, hostPort);
            }
            try {
                sleep(100);
            }
            catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }
    }
}

