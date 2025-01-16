package com.gamestudio.elements;

import com.gamestudio.state.GameState;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public abstract class DumbRobot extends Robot {

    public DumbRobot(int x, int y, float width, float height, float mass, int amountLife, GameState gameState) {
        super(x, y, width, height, mass, amountLife, gameState);
        this.setTeamType(ENEMY_TEAM);
    }

    // Define a área de colisão do inimigo com outros objetos
    @Override
    public abstract Rectangle getBoundForCollisionWithEnemy();

    // Desenha o inimigo na tela
    @Override
    public abstract void draw(Graphics2D g2);

    // Define o movimento do inimigo
    public abstract void move();
}