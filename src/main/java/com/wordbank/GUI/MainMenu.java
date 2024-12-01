package com.wordbank.GUI;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;

public class MainMenu extends javax.swing.JPanel {

    public MainMenu(MainFrame _mainFrame, Dimension minDimension) {
        setPreferredSize(minDimension);
        
        setBackground(Texts.BACKGROUND_COLOR);
        setLayout(null);

        final JLabel logo = new JLabel();
        logo.setBounds(50, 25, 700, 250);
        logo.setBorder(BorderFactory.createLineBorder(Texts.BORDER_COLOR));
        add(logo);

        final JButton classicModeBTN = new JButton("CLASSIC MODE");
        classicModeBTN.setBorder(BorderFactory.createLineBorder(Texts.BORDER_COLOR, 4));
        classicModeBTN.setBackground(Texts.BUTTON_COLOR);
        classicModeBTN.setForeground(Texts.TEXT_COLOR);
        classicModeBTN.setFont(Texts.BOLD_FONT);
        classicModeBTN.setBounds(175, 325, 450, 150);
        classicModeBTN.setFocusable(false);

        classicModeBTN.addActionListener((ActionEvent e) -> {
            _mainFrame.showGame();
        });

        add(classicModeBTN);
    }

}
