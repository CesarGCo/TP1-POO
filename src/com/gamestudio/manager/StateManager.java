package com.gamestudio.manager;

import com.gamestudio.interfaces.Screen;
import com.gamestudio.state.GameOverState;
import com.gamestudio.state.GameState;
import com.gamestudio.state.MenuState;

// A classe StateManager tem a função de agrupar todos os estados do jogo, e gerenciar a mudança de estados de forma fluída e eficiente
public class StateManager {
    public static final int MENU = 0;
    public static final int GAME = 1;
    public static final int GAMEOVER = 2;
    private Screen screen;
    private GameState gameState;
    private MenuState menuState;
    private GameOverState gameOverState;
    private int currentState;

    // Inicializa todos os estados presentes no jogo e a classe Scren
    // Além disso seta qual estado o jogo vai iniciar
    public StateManager() {
        this.gameState = new GameState(this);
        this.menuState = new MenuState(this);
        this.gameOverState = new GameOverState(this);
        this.screen = new Screen(menuState);

        this.currentState = MENU;
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

    public int getCurrentState() {
        return currentState;
    }

    // Seta qual será o novo estado atual do jogo:
    public void setCurrentState(int currentState) {
        switch (currentState) {
            case MENU:
                screen.setState(menuState);
                break;

            case GAME:
                screen.setState(gameState);
                break;

            case GAMEOVER:
                screen.setState(gameOverState);
                break;

            default:
                break;
        }
        this.currentState = currentState;
    }

}

