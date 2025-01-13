package com.gamestudio.state;

import java.awt.image.BufferedImage;

import com.gamestudio.interfaces.GameFrame;
import com.gamestudio.manager.StateManager;

public class GameState extends State {
    
    public GameState(StateManager stateManager) {
       super(stateManager, new BufferedImage(GameFrame.width, GameFrame.height, BufferedImage.TYPE_INT_ARGB));
       
    }
    
    public StateManager getStateManager() {
        return getStateManager();
    }

    public void update() {}

    public void render() {}
    
    public void setPressedButton(int code) {}
    
    public void setReleasedButton(int code) {}
}
