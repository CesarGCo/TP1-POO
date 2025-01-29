package com.gamestudio.manager;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.gamestudio.elements.Camera;
import com.gamestudio.elements.LifeRegen;
import com.gamestudio.elements.GameEntity;
import com.gamestudio.state.GameState;

// A classe GameEntityManager tem a função de gerenciar todas as entidades presentes no estado que foi instanciada
// como por exemplo atualizar, desenhar ou remover entidades, além de verificar colisões entre entidades 
public class GameEntityManager {

    protected final List<GameEntity> gameEntities;

    private final GameState gameState;

    public GameEntityManager(GameState gameState) {
        gameEntities = Collections.synchronizedList(new LinkedList<>());
        this.gameState = gameState;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void addObject(GameEntity gameEntity) {

        synchronized (gameEntities) {
            gameEntities.add(gameEntity);
        }

    }

    public void RemoveObject(GameEntity gameEntity) {
        synchronized (gameEntities) {

            for (int id = 0; id < gameEntities.size(); id++) {

                GameEntity object = gameEntities.get(id);
                if (object == gameEntity)
                    gameEntities.remove(id);

            }
        }
    }

    // Verifica se alguma entidade da lista colidiu com alguma entidade inimiga
    public GameEntity getCollisionWidthEnemyObject(GameEntity object) {
        synchronized (gameEntities) {
            for (GameEntity objectInList : gameEntities) {

                if (object.getTeamType() != objectInList.getTeamType() && object.getTeamType() != GameEntity.ITEM_TEAM &&
                        object.getBoundForCollisionWithEnemy().intersects(objectInList.getBoundForCollisionWithEnemy())) {
                    return objectInList;
                }
            }
        }
        return null;
    }

    // Verifica se alguma entidade colidiu com algum item
    public GameEntity getCollisionWidthItem(GameEntity object) {
        synchronized (gameEntities) {
            for (GameEntity objectInList : gameEntities) {
                if (objectInList.getTeamType() == GameEntity.ALLY_TEAM &&
                        object.getBoundForCollisionWithEnemy().intersects(objectInList.getBoundForCollisionWithEnemy())) {
                    return objectInList;
                }
            }
        }
        return null;
    }

    // Atualiza todas as entidades:
    public void updateObjects() {
        synchronized (gameEntities) {
            for (int id = 0; id < gameEntities.size(); id++) {
                GameEntity object = gameEntities.get(id);

                if (!object.isObjectOutOfCameraView()) object.update();

                if (object.getCurrentState() == GameEntity.DEATH && !object.getIsExploding()) {
                    if(object.getTeamType() == GameEntity.ENEMY_TEAM) {
                        Random random = new Random();
                        if(random.nextInt(3) == 1)
                            getGameState().itemManager.addObject(new LifeRegen(object.getPosX(), object.getPosY(), getGameState()));
                    }
                    gameEntities.remove(id);
                }
            }
        }

    }

    // Desenha todas as entidades na tela
    public void draw(Graphics2D g2) {
        synchronized (gameEntities) {
            for (GameEntity object : gameEntities)
                if (!object.isObjectOutOfCameraView()) object.draw(g2);
        }
    }

    // Desenha a hitbox de todas as entidades
    public void drawAllHitBox(Graphics2D g2d) {
        Camera camera = getGameState().camera;
        synchronized(this.gameEntities){
            synchronized (gameEntities) {
                for (GameEntity object : gameEntities){
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