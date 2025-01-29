package com.gamestudio.elements;

import com.gamestudio.manager.DataLoader;
import com.gamestudio.state.GameState;
import java.awt.Graphics2D;
import java.awt.Rectangle;

// Esta classe representa uma entidade "Burra" dentro do jogo
// isto é, entidade sque não possuem lógica complexa, como os inimigos
public abstract class DumbGameEntity extends GameEntity {
    public DumbGameEntity(float x, float y, int width, int height, float mass, int amountLife, GameState gameWorld) {
        super(x, y, width, height, mass, amountLife, gameWorld);
        setDeathSound(DataLoader.getInstance().getSound("enemy_death"));
        setDeathTime(400);
        setTeamType(ENEMY_TEAM);
    }

    // Move a entidade de uma forma específica
    public abstract void move();

    // Atualiza a entidade
    @Override
    public void update() {
        super.update();
        move();
    }

    // Verifica se houve colisão com alguma entidade inimiga
    @Override
    public abstract Rectangle getBoundForCollisionWithEnemy();

    // Desenha a entidade na tela
    @Override
    public abstract void draw(Graphics2D g2);
}
