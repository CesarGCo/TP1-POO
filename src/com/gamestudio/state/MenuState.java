package com.gamestudio.state;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.gamestudio.interfaces.GameFrame;
import com.gamestudio.interfaces.Screen;

public class MenuState extends State {
    private Image backgroundImage;
    private BufferedImage bufferedImage;

    public MenuState(Screen screen) {
        super(screen);
        this.bufferedImage = new BufferedImage(GameFrame.width, GameFrame.height, BufferedImage.TYPE_INT_ARGB);
        try {
            backgroundImage = ImageIO.read(new File("Assets/Menu/menu.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
            backgroundImage = null;
        }
    }

    public void update() {

    }

    public void render() {
        Graphics g = bufferedImage.getGraphics();
        g.drawImage(backgroundImage, 0, 0, GameFrame.width, GameFrame.height, null);
    }

    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }

    int x = 100;
    public void setPressedButton(int keyCode) {
        if(keyCode == KeyEvent.VK_LEFT) {
            x -= 5;
        } else {
            x += 5;
        }
    }

    public void setReleasedButton(int keyCode) {

    }
}
