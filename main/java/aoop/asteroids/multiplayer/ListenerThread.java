package aoop.asteroids.multiplayer;

import aoop.asteroids.control.MultiplayerGameUpdater;

import java.io.ByteArrayInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * The thread responsible for sending/receiving the packet data via networking. It must have access to a Packet Sender, Packet Controller
 * and the 'Game' object itself. It runs constantly accepting client data, handling the packetData (
 */
public class ListenerThread extends Thread {

    private boolean isRunning = true;
    private PacketUnpacker packetUnpacker;
    /**
     * Constructor. If Player is host, it passes this information to the unpacker so it can relay this information
     *
     */
    public ListenerThread(boolean isHost, MultiplayerGameUpdater multiplayerGameUpdater) {
        boolean host;
        host = isHost;
        this.packetUnpacker = new PacketUnpacker(multiplayerGameUpdater);
        packetUnpacker.setHosting(host);
        this.packetUnpacker.start();
    }

    /**
     * Main Thread for Accepting Client Data Packets, it is runs independantly to allow more open socket time
     */
    public void run() {
        DatagramSocket wSocket = null;
        DatagramPacket wPacket = null;
        byte[] wBuffer;
        ByteArrayInputStream xBuffer = null;
        try {

            int listenPort = 25665;
            wSocket = new DatagramSocket(listenPort);
            wBuffer = new byte[3000];
            wPacket = new DatagramPacket(wBuffer, wBuffer.length);
            System.out.println("Listener Thread (PORT)" + listenPort);
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        while (isRunning) {
            try {
                wSocket.receive(wPacket);
                packetUnpacker.castPacket(wPacket);
                sleep(10);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        wSocket.close();
    }


}