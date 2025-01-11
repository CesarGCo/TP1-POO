package com.gamestudio.state;

import java.awt.image.BufferedImage;
import com.gamestudio.manager.ScreenManager;

public abstract class State {
    private ScreenManager screenManager;
    
    public State(ScreenManager screenManager) {
       this.screenManager = screenManager;
    }
    
    public ScreenManager getScreenManager() {
        return screenManager;
    }

    public abstract void update();

    public abstract void render();

    public abstract BufferedImage getBufferedImage();
    
    public abstract void setPressedButton(int code);
    
    public abstract void setReleasedButton(int code);

}