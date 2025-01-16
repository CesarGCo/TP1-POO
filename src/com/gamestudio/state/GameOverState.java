package com.gamestudio.state;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import com.gamestudio.effect.FrameImage;
import com.gamestudio.elements.ButtonGameOver;
import com.gamestudio.interfaces.GameFrame;
import com.gamestudio.manager.DataLoader;
import com.gamestudio.manager.StateManager;

public class GameOverState extends State {
    private FrameImage backgroundImage;
    private ButtonGameOver[] buttons;
    
    public GameOverState(StateManager stateManager) {
       super(stateManager, new BufferedImage(GameFrame.width, GameFrame.height, BufferedImage.TYPE_INT_ARGB));
        
       buttons = new ButtonGameOver[3];
       buttons[0] = new ButtonGameOver(358, 280);
       buttons[0].update();
       buttons[1] = new ButtonGameOver(358, 320);
       buttons[2] = new ButtonGameOver(358, 360);
       backgroundImage = DataLoader.getInstance().getFrameImage("game_over");
    }

    public void update() {}

    public void render() {
        Graphics g = getBufferedImage().getGraphics();
        g.setColor(Color.black);
        g.fillRect(0, 0, GameFrame.width, GameFrame.height);
        int width = (int) (0.7 * (GameFrame.width));
        g.drawImage(backgroundImage.getImage(), (GameFrame.width / 2) - (width / 2), 0, width, GameFrame.height, null);
        for (ButtonGameOver bt : buttons) {
            bt.draw(g);
        }
    }
    
    public void setPressedButton(int code) {
        switch (code) {
            case KeyEvent.VK_ENTER:
                if(buttons[0].isEnabled()) {
                    getStateManager().setCurrentState(StateManager.GAME);

                } else if(buttons[1].isEnabled()) {
                    buttons[0].update();
                    buttons[1].update();
                    getStateManager().setCurrentState(StateManager.MENU);

                } else if(buttons[2].isEnabled()) {
                    System.exit(0);
                } 
                break;
            case KeyEvent.VK_UP:
                if(buttons[1].isEnabled()) {
                    buttons[0].update();
                    buttons[1].update();
                } else if(buttons[2].isEnabled()) {
                    buttons[1].update();
                    buttons[2].update();
                } 
                break;
            case KeyEvent.VK_DOWN:
                if(buttons[0].isEnabled()) {
                    buttons[0].update();
                    buttons[1].update();
                } else if(buttons[1].isEnabled()) {
                    buttons[1].update();
                    buttons[2].update();
                } 
                break;
            default:
                break;
        }
    }
    
    public void setReleasedButton(int code) {}
}
