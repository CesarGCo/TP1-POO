package com.gamestudio.manager;

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
                if(object.isObjectOutOfCameraView() || object.getCurrentState() == Robot.DEATH){
                    this.robots.remove(id);
                }
            }
        }
    }
}