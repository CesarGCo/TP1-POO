package com.gamestudio.manager;

import com.gamestudio.interfaces.Screen;
import com.gamestudio.state.GameState;
import com.gamestudio.state.MenuState;

public class StateManager {
    private Screen screen;
    private GameState gameState;
    private MenuState menuState;
    private int currentState;

    public StateManager() {
        this.gameState = new GameState(this);
        this.menuState = new MenuState(this);
        
        this.screen = new Screen(menuState);
    }

    public GameState getGameState() {
        return gameState;
    }

    public MenuState getMenuState() {
        return menuState;
    }

    public Screen getScreen() {
        return screen;
    }

    public void setCurrentState(int currentState) {
        switch (currentState) {
            case 0:
                screen.setState(menuState);
                break;

            case 1:
                screen.setState(gameState);
                break;

            case 2:
                //screen.setState(gameOverState);
                break;

            default:
                break;
        }
    }

    public void setCurrentState(String currentScreen) {
    }
}

