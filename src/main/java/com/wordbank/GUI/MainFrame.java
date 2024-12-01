package com.wordbank.GUI;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainFrame extends JFrame {
    //STATIC CONSTANTS
    public static final String GAME_PNL = "GamePNL";
    public static final String MENU_PNL = "MenuPNL";
    //STATIC CONSTANTS - Constants
    private final Dimension minDimension = new java.awt.Dimension(800,900);
    private final CardLayout crdLayout = new java.awt.CardLayout();
    //Constants

    public MainFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Word bank");
        setResizable(false);

        addWindowListener(new WindowAdapter() {
            
            @Override
            public void windowClosing(WindowEvent e) {
                gameScene.endGameLoop();
            }
            
        });

        crdPnl = new JPanel(crdLayout);
        gameScene = new GameScene(this, minDimension);
        Menu = new MainMenu(this, minDimension);
        initComponents();
    }

    private void initComponents() {
        crdPnl.setPreferredSize(minDimension);

        crdPnl.add(Menu, MENU_PNL);
        crdPnl.add(gameScene, GAME_PNL);

        crdLayout.show(crdPnl, MENU_PNL);
        add(crdPnl);
        pack();
        setLocationRelativeTo(null);
    }

    public void showGame() {
        crdLayout.show(crdPnl, GAME_PNL);
        gameScene.startGame();
    }   
    
    public void showMenu() {
        crdLayout.show(crdPnl, MENU_PNL);
        gameScene.endGameLoop();
    }

    //Swing components
    private final javax.swing.JPanel crdPnl;
    private final GameScene gameScene;
    private final MainMenu Menu;
    //Swing components
}
