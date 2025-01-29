package com.gamestudio.elements;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.gamestudio.manager.DataLoader;
import com.gamestudio.state.GameState;

// A calsse LifeRegen representa um item que regenera a vida do Mega Man
public class LifeRegen extends Item {
    private int life;
    public LifeRegen(float x, float y, GameState gameState) {
        super(x, y, 8, 8, gameState);
        this.life = 5;
        setItemaAnim(DataLoader.getInstance().getAnimation("life_regen"));
    }

    // Atualiza os parâmetros necessários no item
    @Override
    public void update() {
        super.update();
        // Verifica se o item colisiu com o Mega Man
        GameEntity object = getGameState().gameEntityManager.getCollisionWidthItem(this);
        if(object != null) {
            object.regen(life);
            this.setCurrentState(DEATH);
        }
    }

    // Retorna parâmetros que serão utilizados para verificar a colisão com inimigos
    @Override
    public Rectangle getBoundForCollisionWithEnemy() {
        return new Rectangle(
            (int) (getPosX() - getWidth() / 2), 
            (int) (getPosY() - getHeight() / 2), 
            (int) getWidth(), 
            (int) getHeight()
        );
    }

    // Desenha o item na tela
    @Override
    public void draw(Graphics2D g2) {
        getItemaAnim().Update(System.nanoTime());
        getItemaAnim().draw(
            (int)(getPosX() - getWidth() / 2 - getGameState().camera.getPosX()), 
            (int)(getPosY() - getHeight() / 2 - getGameState().camera.getPosY()), 
            g2
        );
    }
}
