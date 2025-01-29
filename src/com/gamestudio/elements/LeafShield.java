package com.gamestudio.elements;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.gamestudio.effect.Animation;
import com.gamestudio.manager.DataLoader;
import com.gamestudio.state.GameState;

// A classe LeafShield representa um projétil disparado pelo Boss Final do jogo
public class LeafShield extends Projectile {
    private final Animation forwardAnimation;
    private final Animation backAnimation;

    public LeafShield(float x, float y, GameState gameState) {
        super(x, y, 40, 40, 1.0f, 8, gameState);

        forwardAnimation = DataLoader.getInstance().getAnimation("wood_man_leaf_shield");
        backAnimation = DataLoader.getInstance().getAnimation("wood_man_leaf_shield");
        backAnimation.flipAllImage();
    }

    // Retorna parâmetros que serão utilizados para verificar a colisão com inimigos
    @Override
    public Rectangle getBoundForCollisionWithEnemy() {
        return getBoundForCollisionWithMap();
    }

    // Desenha o projétil na tela
    @Override
    public void draw(Graphics2D g2) {
        if(this.getDirection() == LEFT) {
            forwardAnimation.Update(System.nanoTime());
            forwardAnimation.draw((int) (getPosX() - getGameState().camera.getPosX()), (int) (getPosY() - getGameState().camera.getPosY()), g2);
        } else {
            backAnimation.Update(System.nanoTime());
            backAnimation.draw((int) (getPosX() - getGameState().camera.getPosX()), (int) (getPosY() - getGameState().camera.getPosY()), g2);
        }
    }
}
