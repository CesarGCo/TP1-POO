package com.gamestudio.elements;

import com.gamestudio.state.GameState;
import com.gamestudio.effect.Animation;
import com.gamestudio.manager.DataLoader;
import com.gamestudio.physical.PhysicalMap;

import java.awt.Graphics2D;
import java.awt.Rectangle;

// Representa o inimigo Goomba do jogo do Mario
public class Goomba extends DumbGameEntity {

    private Animation walkingAnim;

    public Goomba(float x, float y, GameState gameWorld) {
        super(x, y, 18, 18, 0.12f, 2, gameWorld);
        walkingAnim = DataLoader.getInstance().getAnimation("goomba_walking");
        setDeathAnimation(DataLoader.getInstance().getAnimation("explosion_effect"));
        walkingAnim.flipAllImage();
        setSpeedX(-0.5f);
        setSpeedY(0.0f);
        setDamage(1);
    }

    // Muda a velocidade vertical se houver colisão com o mapa
    private void checkCollisionWithGround() {
        Rectangle currentBound = getBoundForCollisionWithMap();
        PhysicalMap physicalMap = getGameState().physicalMap;

        Rectangle groundCollision = physicalMap.haveCollisionWithLandForDumbGameEntity(currentBound, this);
        if (groundCollision != null) {
            setPosY(groundCollision.y - getHeight() / 2.0f);
            setSpeedY(0);
        } else {
            setSpeedY(getPosY() + getMass());
        }
    }

    // Verifica se o Goomba colidiu com a parede
    // Se ele colidir irá mudar de direção
    private void checkCollisionWithWall() {
        Rectangle currentBound = getBoundForCollisionWithMap();
        PhysicalMap physicalMap = getGameState().physicalMap;

        Rectangle rectLeftWall = physicalMap.haveCollisionWithLeftWall(currentBound);
        if (rectLeftWall != null) {
            setPosX(rectLeftWall.x + rectLeftWall.width + getWidth() / 2);
            setSpeedX(Math.abs(getSpeedX()));
            walkingAnim.flipAllImage();
        }

        Rectangle rectRightWall = physicalMap.haveCollisionWithRightWall(currentBound);
        if (rectRightWall != null) {
            setPosX(rectRightWall.x - getWidth() / 2);
            setSpeedX(-Math.abs(getSpeedX()));
            walkingAnim.flipAllImage();
        }
    }

    // Move o Goomba e verifica colisões com o mapa
    @Override
    public void move() {
        setPosX(getPosX() + getSpeedX());
        setPosY(getPosY() + getSpeedY());
        checkCollisionWithGround();
        checkCollisionWithWall();
    }

    // Atualiza toda a lógica de funcionamento do inimigo
    @Override
    public void update() {
        super.update();
        walkingAnim.Update(System.nanoTime());
        move();
    }

    // Retorna parâmetros que serão utilizados para verificar a colisão com inimigos
    @Override
    public Rectangle getBoundForCollisionWithEnemy() {
        return new Rectangle((int) (getPosX() - getWidth() / 2),
                (int) (getPosY() - getHeight() / 2),
                (int) getWidth(), (int) getHeight());
    }

    // Desnha o Goomba na tela
    @Override
    public void draw(Graphics2D g2) {
        if (!isObjectOutOfCameraView()) {
            if (getIsExploding()) {
                drawDeathAnimation(g2);
            } else {
                walkingAnim.draw(
                        (int) (getPosX() - getGameState().camera.getPosX()),
                        (int) (getPosY() - getGameState().camera.getPosY()), g2);
            }
        }
    }
}
