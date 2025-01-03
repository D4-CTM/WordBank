package com.wordbank.GUI;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.SwingUtilities;

import com.wordbank.Logic.GameGraphics;

public class GameScene extends javax.swing.JPanel implements Runnable {
    //GAME LOOP VARIABLES
    private boolean running;
    private Thread gameLoop;
    private boolean mousePressed;
    private boolean mouseReleased;
    //GAME LOOP VARIABLES - CONSTANTS
    private final GameGraphics gameGraphics;
    private final long NANOSECONDS = 1000000000;
    private final float FPS;
    //CONSTANTS

    public GameScene(MainFrame mainFrame, Dimension minDimension) {
        gameGraphics = new GameGraphics();
        setPreferredSize(minDimension);
        setLayout(null);
        FPS = 60;

        addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent arg0) {
                mousePressed = true;
            }

            @Override
            public void mouseReleased(MouseEvent arg0) {
                mouseReleased = true;
            }
            
        });

        final JButton returnBTN = new JButton();
        returnBTN.setBounds(0,0, 200, 100);

        returnBTN.addActionListener((ActionEvent e) -> {
            mainFrame.showMenu();
        });

        add(returnBTN);
    }

    public void startGame() {
        gameLoop = new Thread(this);
        gameLoop.start();
    }

    public void endGameLoop() 
    { running = false; }

    public boolean isGameRunning() 
    { return running; }

    private void sleepThread(float sleepTime) {
        try {
            if (sleepTime < 0) Thread.sleep((long) sleepTime);
        } catch (InterruptedException e) {}
    }

    @Override
    public void run() {
        running = true;
        float drawInterval = NANOSECONDS / FPS;
        
        while (running) {
            float nextDrawInterval = System.nanoTime() + drawInterval;
            float remainingTime = (nextDrawInterval - System.nanoTime())/NANOSECONDS;
            sleepThread(remainingTime);

            repaint();
        }

        gameLoop = null;
        System.gc();
    }

    private void eventHandler(Graphics2D graphics) {
        if (mousePressed) {
            Point location = MouseInfo.getPointerInfo().getLocation();
            SwingUtilities.convertPointFromScreen(location, this);
            gameGraphics.renderSelectedTiles(graphics, location);
        } 
        if (mouseReleased) {
            gameGraphics.checkWord();
            mouseReleased = false;
            mousePressed = false;
        }
        gameGraphics.showFoundWords(graphics);

        if (!gameGraphics.isJumbleComplete()) return;
        System.out.println("MatrixCompleted");
        try {
            Thread.sleep(2000);
            gameGraphics.generateNewMatrix();
        } catch (Exception e) {}
    }

    private void update(Graphics2D graphics) {
        gameGraphics.renderBackgroundImage(graphics, "./src/main/resources/images/bg.png");
        gameGraphics.renderMatrixBackground(graphics);
        gameGraphics.renderLetterMatrix(graphics);
        gameGraphics.renderWordBox(graphics);
        eventHandler(graphics);

        graphics.dispose();
        System.gc();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        update((Graphics2D) g);
    }

    public void saveData() {
        gameGraphics.saveData();
    }

}
