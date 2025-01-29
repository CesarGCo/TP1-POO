package com.gamestudio.elements;

import com.gamestudio.state.GameState;
import com.gamestudio.effect.Animation;
import com.gamestudio.manager.DataLoader;
import com.gamestudio.physical.PhysicalMap;

import java.awt.Graphics2D;
import java.awt.Rectangle;

// Representa um inimigo da fase Wood man, caracterizado como um coelho robô
public class Rabbit extends DumbGameEntity {
    // Ações que o coelho pode assumir:
    private static final int IDLE = 0;
    private static final int JUMPING = 1;

    // Animações:
    private Animation jumpingAnim;
    private Animation idleAnim;

    private boolean isJumping; // caso esteja pulando
    private long stateStartTime;// Tempo de espera entre os pulos (1 segundo)


    public Rabbit(float x, float y, GameState gameWorld) {
        super(x, y, 30, 30, 0.12f, 2, gameWorld);
        jumpingAnim = DataLoader.getInstance().getAnimation("robbit_jumping");
        idleAnim = DataLoader.getInstance().getAnimation("robbit_idle");
        setDeathAnimation(DataLoader.getInstance().getAnimation("explosion_effect"));
        setSpeedX(-0.5f);
        setSpeedY(0.0f);
        isJumping = false;
        stateStartTime = System.currentTimeMillis();
        setCurrentAction(IDLE);
        setDamage(3);
    }

    // Altera a velocidaed vertical do coelho caso possua colisão com o mapa ou não
    private void checkCollisionWithGround() {
        Rectangle currentBound = getBoundForCollisionWithMap();
        PhysicalMap physicalMap = getGameState().physicalMap;

        Rectangle groundCollision = physicalMap.haveCollisionWithLandForDumbGameEntity(currentBound, this);
        if (groundCollision != null) {
            setPosY(groundCollision.y - getHeight() / 2.0f);
            setSpeedY(0);

            if (isJumping) {
                isJumping = false;
            }
        } else {
            setSpeedY(getSpeedY() + getMass());
        }
    }

    // Verifica colisão com paredes
    private void checkCollisionWithWall() {
        Rectangle currentBound = getBoundForCollisionWithMap();
        PhysicalMap physicalMap = getGameState().physicalMap;

        Rectangle rectLeftWall = physicalMap.haveCollisionWithLeftWall(currentBound);
        if (rectLeftWall != null) {
            setPosX(rectLeftWall.x + rectLeftWall.width + getWidth() / 2);
            setSpeedX(Math.abs(getSpeedX()));
            jumpingAnim.flipAllImage();
            idleAnim.flipAllImage();
        }

        Rectangle rectRightWall = physicalMap.haveCollisionWithRightWall(currentBound);
        if (rectRightWall != null) {
            setPosX(rectRightWall.x - getWidth() / 2);
            setSpeedX(-Math.abs(getSpeedX()));
            jumpingAnim.flipAllImage();
            idleAnim.flipAllImage();
        }
    }

    // Execura a açãoi de pulo do coelho
    private void jump() {
        if (!isJumping) {
            isJumping = true;
            setSpeedY(-6.5f);
        }
    }


    // movimenta o coelho com base na direção e massa
    @Override
    public void move() {
        if(getCurrentAction() == JUMPING) {
            setPosX(getPosX() + getSpeedX());
        }
        setPosY(getPosY() + getSpeedY());
        setSpeedY(getSpeedY() + getMass());
        checkCollisionWithGround();
        checkCollisionWithWall();
    }

    // Atualiza todos os parâmetros necessários do coelho
    @Override
    public void update() {
        super.update();
        long elapsedTime = System.currentTimeMillis() - stateStartTime;

        switch (getCurrentState()) {
            case ALIVE:
                if(getCurrentAction() == IDLE) {
                    jumpingAnim.Update(System.nanoTime());
                    if (elapsedTime > 1000) {
                        setCurrentAction(JUMPING);
                        stateStartTime = System.currentTimeMillis();
                        jump();
                    }
                } else {
                    jumpingAnim.Update(System.nanoTime());
                    if (!isJumping) {
                        setCurrentAction(IDLE);
                        stateStartTime = System.currentTimeMillis();
                    }
                }
                break;
            case DEATH:
            
                break;
        }
        move();
    }

    // Verifica se houve colisão com alguma entidade inimiga
    @Override
    public Rectangle getBoundForCollisionWithEnemy() {
        return new Rectangle((int) (getPosX() - getWidth() / 2),
                (int) (getPosY() - getHeight() / 2),
                (int) getWidth(), (int) getHeight());
    }

    // Desenha o coelho na tela
    @Override
    public void draw(Graphics2D g2) {
        if (!isObjectOutOfCameraView()) {
                if(getIsExploding()) {
                    drawDeathAnimation(g2);
                } else {
                    Animation currentAnim = isJumping ? jumpingAnim : idleAnim;
                    currentAnim.draw(
                    (int) (getPosX() - getGameState().camera.getPosX()),
                    (int) (getPosY() - getGameState().camera.getPosY()), g2);
                }
            
        }
    }
}
