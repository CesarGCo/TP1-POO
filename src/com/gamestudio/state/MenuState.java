package com.gamestudio.state;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import com.gamestudio.effect.FrameImage;
import com.gamestudio.elements.ButtonMenu;
import com.gamestudio.interfaces.GameFrame;
import com.gamestudio.manager.DataLoader;
import com.gamestudio.manager.StateManager;

public class MenuState extends State {
    private FrameImage backgroundImage;
    private ButtonMenu[] buttons;

    public MenuState(StateManager stateManager) {
        super(stateManager, new BufferedImage(GameFrame.width, GameFrame.height, BufferedImage.TYPE_INT_ARGB));

        buttons = new ButtonMenu[2];
        buttons[0] = new ButtonMenu(220, 385);
        buttons[0].update();
        buttons[1] = new ButtonMenu(220, 430);

        backgroundImage = DataLoader.getInstance().getFrameImage("menu");
    }

    public void update() {}

    public void render() {
        Graphics g = getBufferedImage().getGraphics();
        g.drawImage(backgroundImage.getImage(), 0, 0, GameFrame.width, GameFrame.height, null);
        for (ButtonMenu bt : buttons) {
            bt.draw(g);
        }
    }

    public void setPressedButton(int keyCode) {
        if(keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_DOWN) {
            for (ButtonMenu bt : buttons) {
                bt.update();
            }
            update();
            
        } else if(keyCode == KeyEvent.VK_ENTER) {
            if(buttons[0].isEnabled()) {
                getStateManager().setCurrentState(StateManager.GAME);;
            } else {
                System.exit(0);
            }
        }
    }

    public void setReleasedButton(int keyCode) {}
}
