package com.gamestudio.elements;

import com.gamestudio.state.GameState;
import java.awt.Graphics2D;

// Representa um projétil dentro do jogo
public abstract class Projectile extends GameEntity {
    // Estado para caso o projétil colidiu com alguma entidade:
    public static final int COLLIDED = 4;

    public Projectile(float x, float y, int width, int height, float mass, int damage, GameState gameState) {
        super(x, y, width, height, mass, 1, gameState);
        setDamage(damage);
    }

    // Desenha o projétil na tela
    public abstract void draw(Graphics2D g2d);

    // Atualiza a posição do objeto e verifica colisões com entidades
    public void update(){
        super.update();
        setPosX(getPosX() + getSpeedX());
        setPosY(getPosY() + getSpeedY());
        GameEntity object = getGameState().gameEntityManager.getCollisionWidthEnemyObject(this);
        if(object!=null && !object.getIsInvencible()) {
            object.beHurt(getDamage());
            this.setCurrentState(COLLIDED);
        }
    }

}