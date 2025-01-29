package com.gamestudio.manager;

import java.awt.Color;
import java.awt.Graphics2D;

import com.gamestudio.elements.Camera;
import com.gamestudio.elements.Projectile;
import com.gamestudio.elements.GameEntity;
import com.gamestudio.state.GameState;

// A classe itemManager tem a função de gerenciar todos os projéteis presentes no estado que foi instanciada
// como por exemplo atualizar, desenhar ou remover projéteis, além de verificar colisões com entidades
public class ProjectileManager extends GameEntityManager {

    public ProjectileManager(GameState gameWorld) {
        super(gameWorld);
    }

    // Atualiza todos os projéteis 
    @Override
    public void updateObjects() {
        super.updateObjects();
        synchronized(this.gameEntities){
            for(int id = 0; id < this.gameEntities.size(); id++){
                GameEntity object = this.gameEntities.get(id);
                if(object.isObjectOutOfCameraView() || object.getCurrentState() == Projectile.COLLIDED){
                    this.gameEntities.remove(id);
                }
            }
        }
    }

    // Desenha a hitbox de todos os itens
    public void drawAllHitBox(Graphics2D g2d) {
        Camera camera = getGameState().camera;
        synchronized(this.gameEntities){
            synchronized (gameEntities) {
                for (GameEntity object : gameEntities){
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