package com.gamestudio.interfaces;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

public class Screen extends JFrame {
    public static final int width = 1000;
    public static final int height = 600;
    private CardLayout cardLayout;
    private JPanel panels;

    public Screen() {
        super("Mega man");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Toolkit toolkit = this.getToolkit();
        Dimension desktopSolution = toolkit.getScreenSize();

        cardLayout = new CardLayout();
        panels = new JPanel(cardLayout);
        panels.add(new StartScreen(), "Menu");

        this.add(panels);
        this.setBounds(
                (desktopSolution.width - width) / 2,
                (desktopSolution.height - height) / 2,
                width,
                height
        );
    }

    public void open() {
        super.setVisible(true);
    }

    public void close() {
        super.setVisible(false);
    }

    public void show(String namePanel) {
        cardLayout.show(panels, namePanel);
    }

    public static void main(String arg[]){
        Screen gameFrame = new Screen();
        gameFrame.open();
        gameFrame.show("Menu");
    }
}
