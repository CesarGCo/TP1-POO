package com.gamestudio.state;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.gamestudio.elements.ButtonMenu;
import com.gamestudio.interfaces.GameFrame;
import com.gamestudio.interfaces.Screen;

public class MenuState extends State {
    private Image backgroundImage;
    private BufferedImage bufferedImage;
    private ButtonMenu[] buttons;
    private int buttonEnabled;

    public MenuState(Screen screen) {
        super(screen);
        this.buttonEnabled = 0;
        this.bufferedImage = new BufferedImage(GameFrame.width, GameFrame.height, BufferedImage.TYPE_INT_ARGB);

        buttons = new ButtonMenu[2];
        buttons[0] = new ButtonMenu(220, 385);
        buttons[0].update();
        buttons[1] = new ButtonMenu(220, 430);

        try {
            backgroundImage = ImageIO.read(new File("Assets/Menu/menu.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
            backgroundImage = null;
        }
    }

    public void update() {
        if(buttonEnabled == 1) {
            buttonEnabled = 0;
        } else {
            buttonEnabled = 1;
        }
    }

    public void render() {
        Graphics g = bufferedImage.getGraphics();
        g.drawImage(backgroundImage, 0, 0, GameFrame.width, GameFrame.height, null);
        for (ButtonMenu bt : buttons) {
            bt.draw(g);
        }
    }

    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }

    public void setPressedButton(int keyCode) {
        if(keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_DOWN) {
            for (ButtonMenu bt : buttons) {
                bt.update();
            }
            update();
            
        } else if(keyCode == KeyEvent.VK_ENTER) {
            if(buttons[0].isEnabled()) {
                
            } else {
                System.exit(0);
            }
        }
    }

    public void setReleasedButton(int keyCode) {}
}
