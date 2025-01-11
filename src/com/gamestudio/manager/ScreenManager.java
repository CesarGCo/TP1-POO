package com.gamestudio.manager;


import javax.swing.JPanel;
import java.awt.CardLayout;
import com.gamestudio.interfaces.StartScreen;
import com.gamestudio.state.MenuState;

public class ScreenManager {
    private CardLayout cardLayout;
    private JPanel panels;
    private StartScreen startScreen;
    private String currentScreen;

    public ScreenManager() {
        cardLayout = new CardLayout();
        panels = new JPanel(cardLayout);

        startScreen = new StartScreen(new MenuState(this));
        panels.add(startScreen, "Menu");

        currentScreen = "Menu";
    }

    public StartScreen getStartScreen() {
        return startScreen;
    }

    public JPanel getPanels() {
        return panels;
    }

    public void setCurrentScreen(String currentScreen) {
        this.currentScreen = currentScreen;
    }

    public void show() {
        cardLayout.show(panels, currentScreen);
    }
}

