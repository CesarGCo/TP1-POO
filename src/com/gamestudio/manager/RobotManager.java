package com.gamestudio.manager;

import java.awt.Graphics2D;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.gamestudio.elements.Robot;
import com.gamestudio.state.GameState;

public class RobotManager {

    protected List<Robot> robots;

    private GameState gameState;
    
    public RobotManager(GameState gameState) {
        robots = Collections.synchronizedList(new LinkedList<Robot>());
        this.gameState = gameState;
    }
    
    public GameState getGameWorld(){
        return gameState;
    }
    
    public void addObject(Robot robot){
        
        synchronized(robots){
            robots.add(robot);
        }
        
    }
    
    public void RemoveObject(Robot robot){
        synchronized(robots){
        
            for(int id = 0; id < robots.size(); id++){
                
                Robot object = robots.get(id);
                if(object == robot)
                    robots.remove(id);

            }
        }
    }
    
    public Robot getCollisionWidthEnemyObject(Robot object){
        synchronized(robots){
            for(int id = 0; id < robots.size(); id++){
                
                Robot objectInList = robots.get(id);

                if(object.getTeamType() != objectInList.getTeamType() && 
                        object.getBoundForCollisionWithEnemy().intersects(objectInList.getBoundForCollisionWithEnemy())){
                    return objectInList;
                }
            }
        }
        return null;
    }
    
    /*public void UpdateObjects(){
        
        synchronized(robots){
            for(int id = 0; id < robots.size(); id++){
                
                Robot object = robots.get(id);
                
                
                if(!object.isObjectOutOfCameraView()) object.Update();
                
                if(object.getCurrentState() == Robot.DEATH){
                    robots.remove(id);
                }
            }
        }
        
    }*/
    
    /*public void draw(Graphics2D g2){
        synchronized(robots){
            for(Robot object: robots)
                if(!object.isObjectOutOfCameraView()) object.draw(g2);
        }
    } */
	
}