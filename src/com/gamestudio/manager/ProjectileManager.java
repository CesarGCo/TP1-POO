package com.gamestudio.manager;

import java.awt.Color;
import java.awt.Graphics2D;

import com.gamestudio.elements.Camera;
import com.gamestudio.elements.Projectile;
import com.gamestudio.elements.Robot;
import com.gamestudio.state.GameState;

public class ProjectileManager extends RobotManager {

    public ProjectileManager(GameState gameWorld) {
        super(gameWorld);
    }

    @Override
    public void updateObjects() {
        super.updateObjects();
        synchronized(this.robots){
            for(int id = 0; id < this.robots.size(); id++){
                Robot object = this.robots.get(id);
                if(object.isObjectOutOfCameraView() || object.getCurrentState() == Projectile.COLLIDED){
                    this.robots.remove(id);
                }
            }
        }
    }

    public void drawAllHitBox(Graphics2D g2d) {
        Camera camera = getGameState().camera;
        synchronized(this.robots){
            synchronized (robots) {
                for (Robot object : robots){
                    g2d.setColor(Color.red);
                    g2d.drawRect(
                        (int)(object.getPosX() - camera.getPosX() - object.getWidth() / 2), 
                        (int)(object.getPosY() - camera.getPosY() - object.getHeight() / 2), 
                        object.getWidth(), 
                        object.getHeight()
                    );
                }
            }
        }
    }
}