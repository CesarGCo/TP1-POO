package com.gamestudio.manager;

import java.awt.Color;
import java.awt.Graphics2D;

import com.gamestudio.elements.Camera;
import com.gamestudio.elements.GameEntity;
import com.gamestudio.state.GameState;

// A classe itemManager tem a função de gerenciar todas os itens presentes no estado que foi instanciada
// como por exemplo atualizar, desenhar ou remover itens, além de verificar colisões com entidades
public class ItemManager extends GameEntityManager{
    public ItemManager(GameState gameState) {
        super(gameState);
    }

    // Atualiza todos os itens
    @Override
    public void updateObjects() {
        super.updateObjects();
        synchronized(this.gameEntities){
            for(int id = 0; id < this.gameEntities.size(); id++){
                GameEntity object = this.gameEntities.get(id);
                if(object.isObjectOutOfCameraView() || object.getCurrentState() == GameEntity.DEATH){
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
                    g2d.setColor(Color.green);
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
