package com.gamestudio.state;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.gamestudio.interfaces.Screen;
import com.gamestudio.interfaces.StartScreen;

public class MenuState {
    private BufferedImage bufferedImage;
    private StartScreen startScreen;

    public MenuState(StartScreen startScreen) {
        this.startScreen = startScreen;
        bufferedImage = new BufferedImage(Screen.width, Screen.width, BufferedImage.TYPE_INT_ARGB);
    }

    public void render() {
        Graphics g = bufferedImage.getGraphics();
        g.setColor(Color.red);
        g.fillRect(100, 100, 100, 100);
    }

    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }
}
