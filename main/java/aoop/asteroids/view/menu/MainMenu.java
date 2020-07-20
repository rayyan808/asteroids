package aoop.asteroids.view.menu;

import aoop.asteroids.control.*;
import aoop.asteroids.game_observer.GameDeathListener;
import aoop.asteroids.model.Game;
import aoop.asteroids.view.AsteroidsFrame;
import database.AsteroidsDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.InetAddress;

public class MainMenu extends JFrame implements GameDeathListener {

    private JFrame frame = this;
    public Game game;
    private JLayeredPane layer = new JLayeredPane();
    private JPanel spPanel = new JPanel();
    private JPanel joinCOOPPanel = new JPanel();
    private JPanel hostCOOPPanel = new JPanel();
    private JPanel joinDMPanel = new JPanel();
    private JPanel hostDMPanel = new JPanel();
    private JPanel spDataPanel = new JPanel();
    private JPanel coopDataPanel = new JPanel();
    private JPanel dmDataPanel = new JPanel();
    private AsteroidsDAO dataSP;
    private AsteroidsDAO dataMP;

/*    public void newBackground(){
        JLabel background = new JLabel();
        background.setBackground(Color.black);
        background.setSize(800,600);
        background.setBounds(0,0,800,600);
        background.setVisible(true);
        layer.add(background,JLayeredPane.DEFAULT_LAYER);
    }*/

    private void setTitle() {
        JPanel titlePanel = new JPanel();
        titlePanel.setBounds(200, 50, 400, 100);
        titlePanel.setSize(400, 100);
        titlePanel.setOpaque(false);
        titlePanel.setLayout(new FlowLayout());

        JLabel titleName = new JLabel("Asteroids");
        titleName.setForeground(Color.lightGray);
        titleName.setFont(new Font("Helvetica", Font.BOLD, 70));
        titlePanel.add(titleName);
        layer.add(titlePanel, JLayeredPane.PALETTE_LAYER);
    }

    private void singlePlayer() {

        MenuItem(spPanel, 200);
        spPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                AsteroidsFrame frm = new AsteroidsFrame(game);
                new NewGameAction(game).actionPerformed(
                        // Just use a dummy action; NewGameAction doesn't care about the action event's properties.
                        new ActionEvent(frm, ActionEvent.ACTION_PERFORMED, null));
                frm.setVisible(true);
                frame.setVisible(false);
            }
        });

        JLabel spName = textLabel("SinglePlayer");
        spPanel.add(spName);
        layer.add(spPanel, 0);
    }

    private void hostCOOPGame() {

        MenuItem(hostCOOPPanel, 240);
        hostCOOPPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                AsteroidsFrame frm = new AsteroidsFrame(game);
                new Action_HostGameCOOP(game).actionPerformed(new ActionEvent(frm, ActionEvent.ACTION_PERFORMED, null));
                frm.setVisible(true);
                frame.setVisible(false);
            }
        });
        JLabel hostCOOPName = textLabel("Host Game [COOP]");
        hostCOOPPanel.add(hostCOOPName);
        layer.add(hostCOOPPanel, JLayeredPane.PALETTE_LAYER);
    }

    private void joinCOOPGame() {

        MenuItem(joinCOOPPanel, 280);
        joinCOOPPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                AsteroidsFrame frm = new AsteroidsFrame(game);
                String ip = JOptionPane.showInputDialog("Enter host ip");
                String port = JOptionPane.showInputDialog("Enter desired port");
                if(checkInput(ip,port)) {
                    new Action_JoinGameCOOP(game, ip, port).actionPerformed(new ActionEvent(frm, ActionEvent.ACTION_PERFORMED, null));
                    frm.setVisible(true);
                    frame.setVisible(false);
                }
            }
        });
        JLabel joinName = textLabel("Join Game [COOP]");
        joinCOOPPanel.add(joinName);
        layer.add(joinCOOPPanel, JLayeredPane.PALETTE_LAYER);
    }

    private void hostDMGame() {

        MenuItem(hostDMPanel, 320);
        hostDMPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                AsteroidsFrame frm = new AsteroidsFrame(game);
                new Action_HostGameDeathMatch(game).actionPerformed(new ActionEvent(frm, ActionEvent.ACTION_PERFORMED, null));
                frm.setVisible(true);
                frame.setVisible(false);
            }
        });
        JLabel hostDMName = textLabel("Host Game [DeathMatch]");
        hostDMPanel.add(hostDMName);
        layer.add(hostDMPanel, JLayeredPane.PALETTE_LAYER);
    }

    private void joinDMGame() {

        MenuItem(joinDMPanel, 360);
        joinDMPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                AsteroidsFrame frm = new AsteroidsFrame(game);
                String ip = JOptionPane.showInputDialog("Enter host ip");
                String port = JOptionPane.showInputDialog("Enter desired port");
                if(checkInput(ip,port)) {
                    new Action_JoinGameDeathMatch(game, ip, port).actionPerformed(new ActionEvent(frm, ActionEvent.ACTION_PERFORMED, null));
                    frm.setVisible(true);
                    frame.setVisible(false);
                }
            }
        });
        JLabel joinDMName = textLabel("Join Game [DeathMatch]");
        joinDMPanel.add(joinDMName);
        layer.add(joinDMPanel, JLayeredPane.PALETTE_LAYER);
    }
   private boolean checkInput(String ip, String port){
        if((ip != null) && (port != null)){
            InetAddress testIP=null;
            try {
                testIP = InetAddress.getByName(ip);
                Integer.parseInt(port);
            }catch(Exception e){
                System.err.println("Invalid input. " + testIP.toString() + " \n");
                return false;
            }
            return true;
        }
        System.err.println("User Cancelled input.\n");
        return false;
   }
    private void coopDataGame() {

        MenuItem(coopDataPanel, 440);
        coopDataPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dataMP.openDataSource();
                dataMP.getAllPlayers();
                dataMP.closeDataSource();

            }
        });
        JLabel coopDataName = textLabel("COOP Leaderboard");
        coopDataPanel.add(coopDataName);
        layer.add(coopDataPanel, JLayeredPane.PALETTE_LAYER);
    }

    private void spectateGame() {
        MenuItem(dmDataPanel, 480);
        dmDataPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                AsteroidsFrame frm = new AsteroidsFrame(game);
                String ip = JOptionPane.showInputDialog("Enter host ip");
                String port = JOptionPane.showInputDialog("Enter desired port");
                if(checkInput(ip,port)) {
                    new Action_Spectate(game, ip, port).actionPerformed(new ActionEvent(frm, ActionEvent.ACTION_PERFORMED, null));
                    frm.setVisible(true);
                    frame.setVisible(false);
                }

            }
        });
        JLabel dmDataName = textLabel("Spectate");
        dmDataPanel.add(dmDataName);
        layer.add(dmDataPanel, JLayeredPane.PALETTE_LAYER);
    }

    private void spDataGame() {

        MenuItem(spDataPanel, 400);
        spDataPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dataSP.openDataSource();
                dataSP.getAllPlayers();
                dataSP.closeDataSource();
            }
        });
        JLabel spDataName = textLabel("SinglePlayer Leaderboard");
        spDataPanel.add(spDataName);
        layer.add(spDataPanel, JLayeredPane.PALETTE_LAYER);
    }


    private void MenuItem(JPanel upPanel, int posY) {
        upPanel.setBounds(200, posY, 400, 30);
        upPanel.setSize(400, 30);
        upPanel.setOpaque(false);
        upPanel.setLayout(new FlowLayout());
    }


    private JLabel textLabel(String s) {
        JLabel label = new JLabel(s);
        label.setForeground(Color.lightGray);
        label.setFont(new Font("Helvetica", Font.BOLD, 20));
        return label;
    }


    public MainMenu(Game game, AsteroidsDAO dataSP, AsteroidsDAO dataMP) {
        this.dataMP = dataMP;
        this.dataSP = dataSP;
        this.game = game;
        this.game.addDeathListener(this);
        this.setBackground(Color.black);
        this.setTitle();
        // Add a menu bar with some simple actions.
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Game");
        menuBar.add(menu);
        menu.add(new Action_SetColour(this.game));
        menu.add(new Action_SetUsername(this.game));
        this.setJMenuBar(menuBar);
        this.singlePlayer();
        this.joinCOOPGame();
        this.hostCOOPGame();
        this.joinDMGame();
        this.hostDMGame();
        this.spDataGame();
        this.coopDataGame();
        this.spectateGame();
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setContentPane(layer);
        this.setLayout(new BorderLayout());
        this.pack();
        this.setSize(800, 600);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    /**
     * On Game End -> Menu Visible
     */
    @Override
    public void onGameEnded() {
        this.setVisible(true);
    }

    /**
     * On Game start -> Menu invisible
     */
    @Override
    public void onGameStart(){
        this.setVisible(false);
    }
}
