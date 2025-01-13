package com.gamestudio.state;

import java.awt.image.BufferedImage;

import com.gamestudio.interfaces.Screen;
import com.gamestudio.manager.StateManager;

public abstract class State {
    private StateManager stateManager;
    private BufferedImage bufferedImage;

    public State(StateManager stateManager, BufferedImage bufferedImage) {
       this.stateManager = stateManager;
       this.bufferedImage = bufferedImage;
    }
    
    public StateManager getStateManager() {
        return stateManager;
    }

    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }

    public abstract void update();

    public abstract void render();
    
    public abstract void setPressedButton(int code);
    
    public abstract void setReleasedButton(int code);

}