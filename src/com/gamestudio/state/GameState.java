package com.gamestudio.state;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.gamestudio.interfaces.GameFrame;
import com.gamestudio.manager.StateManager;
import com.gamestudio.physical.PhysicalMap;

public class GameState extends State {
    
    public PhysicalMap physicalMap;

    public GameState(StateManager stateManager) {
       super(stateManager, new BufferedImage(GameFrame.width, GameFrame.height, BufferedImage.TYPE_INT_ARGB));
       this.physicalMap = new PhysicalMap(0, 0, this);
    }
    
    public StateManager getStateManager() {
        return getStateManager();
    }

    public void update() {}

    public void render() {
    }
    
    public void setPressedButton(int code) {}
    
    public void setReleasedButton(int code) {}
}
