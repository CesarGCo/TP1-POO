package com.gamestudio.manager;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.gamestudio.elements.Camera;
import com.gamestudio.elements.Robot;
import com.gamestudio.state.GameState;

public class RobotManager {

    protected final List<Robot> robots;

    private final GameState gameState;

    public RobotManager(GameState gameState) {
        robots = Collections.synchronizedList(new LinkedList<>());
        this.gameState = gameState;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void addObject(Robot robot) {

        synchronized (robots) {
            robots.add(robot);
        }

    }

    public void RemoveObject(Robot robot) {
        synchronized (robots) {

            for (int id = 0; id < robots.size(); id++) {

                Robot object = robots.get(id);
                if (object == robot)
                    robots.remove(id);

            }
        }
    }

    public Robot getCollisionWidthEnemyObject(Robot object) {
        synchronized (robots) {
            for (Robot objectInList : robots) {

                if (object.getTeamType() != objectInList.getTeamType() &&
                        object.getBoundForCollisionWithEnemy().intersects(objectInList.getBoundForCollisionWithEnemy())) {
                    return objectInList;
                }
            }
        }
        return null;
    }

    public void updateObjects() {

        synchronized (robots) {
            for (int id = 0; id < robots.size(); id++) {
                Robot object = robots.get(id);

                if (!object.isObjectOutOfCameraView()) object.update();

                if (object.getCurrentState() == Robot.DEATH && !object.getIsExploding()) {
                    robots.remove(id);
                }
            }
        }

    }

    public void draw(Graphics2D g2) {
        synchronized (robots) {
            for (Robot object : robots)
                if (!object.isObjectOutOfCameraView()) object.draw(g2);
        }
    }

    public void drawAllHitBox(Graphics2D g2d) {
        Camera camera = getGameState().camera;
        synchronized(this.robots){
            synchronized (robots) {
                for (Robot object : robots){
                    g2d.setColor(Color.blue);
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