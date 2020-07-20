package aoop.asteroids.multiplayer;

import aoop.asteroids.control.MultiplayerGameUpdater;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;

public class PacketUnpacker extends Thread {

    private boolean isHosting;
    private final MultiplayerGameUpdater multiplayerGameUpdater;
    /**
     * The Stream for Object Input and Byte Array Input, it will be re-used alot so defining
     * as a global variable will not require us to repeatdly create them
     */
    private ObjectInputStream iStream;
    private ByteArrayInputStream byteInputStream;

    public PacketUnpacker(MultiplayerGameUpdater multiplayerGameUpdater) {
        this.multiplayerGameUpdater = multiplayerGameUpdater;
    }

    @Override
    public void run() {
        super.run();
    }
    /**
     * Everytime a Packet is recieved, this method will Cast the received packet as DataPacket
     * If the Data Packet is sucessfully cast, it is sent to the Multiplayer GameUpdater Thread
     * for further handling and processing (We do not want to modify the game from different threads
     * for concurrency's sake)
     *
     * @param packet DataPacket Object as a DatagramPacket
     */
    public void castPacket(DatagramPacket packet) {
        synchronized (multiplayerGameUpdater) {
            try {
                byteInputStream = new ByteArrayInputStream(packet.getData());
                iStream = new ObjectInputStream(byteInputStream);
                if (isHosting) { //Recieve commands
                    DataPacket_Client recievedInfo = (DataPacket_Client) iStream.readObject();
                    if (recievedInfo != null) {
                        multiplayerGameUpdater.addClient(recievedInfo.player.getCurrentGameMode(), packet.getAddress());
                        multiplayerGameUpdater.acceptClientInfo(recievedInfo);
                    }
                } else {
                    //Recieve game snapshot
                    DataPacket_Game gameSnapshot = (DataPacket_Game) iStream.readObject();
                    if (gameSnapshot != null) {
                        multiplayerGameUpdater.updateGameSnapshot(gameSnapshot);
                    }
                }
                iStream.close();
            }
            catch (IOException e) {
                System.err.println("Exception:  " + e);
                e.printStackTrace();
            }
            catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            catch (NullPointerException nulle) {
                System.out.println("NULL PACKET RECIEVED\n");
                nulle.printStackTrace();
            }
        }
    }

    public void setHosting(boolean x) {
        this.isHosting = x;
    }
    /**
     * @param dataPacket Reads the command in the data Packet to determine what actions must be taken
     */

}
