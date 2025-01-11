package com.gamestudio.state;

import java.awt.image.BufferedImage;

import com.gamestudio.interfaces.Screen;

public abstract class State {
    private Screen screen;
    
    public State(Screen screen) {
       this.screen = screen;
    }
    
    public abstract void update();

    public abstract void render();

    public abstract BufferedImage getBufferedImage();
    
    public abstract void setPressedButton(int code);
    
    public abstract void setReleasedButton(int code);

}