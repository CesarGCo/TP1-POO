package com.gamestudio.elements;

import com.gamestudio.state.GameState;
import com.gamestudio.effect.Animation;
import com.gamestudio.manager.DataLoader;
import java.awt.Graphics2D;
import java.awt.Rectangle;

// Classe que represenat um inimifo da fase Wood man, caracterizado por ser um morcego
public class Bat extends DumbGameEntity {
    // Ações que o morcego possui
    private static final int ACTIVE = 0;
    private static final int ACTIVANTING = 2;
    private static final int NOACTIVE = 3;

    // Animações
    private Animation idleAnim;
    private Animation openingWingsAnim;
    private Animation flyingAnim;

    // Velocidade
    private float speed;

    public Bat(int x, int y, GameState gameWorld) {
        super(x, y, 20, 20, 0, 2, gameWorld);
        idleAnim = DataLoader.getInstance().getAnimation("batton_idle");
        openingWingsAnim = DataLoader.getInstance().getAnimation("batton_rising");
        flyingAnim = DataLoader.getInstance().getAnimation("batton_flying");
        setDeathAnimation(DataLoader.getInstance().getAnimation("explosion_effect"));
        speed = 0.3f;
        setDamage(1);
        setCurrentAction(NOACTIVE);
    }

    // Verifica a ditância que o morcego está do mega man, para tomar alguma ação
    private boolean isMegaManInRange() {
        float deltaX = getGameState().megaMan.getPosX() - getPosX();
        float deltaY = getGameState().megaMan.getPosY() - getPosY();
        float distance = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        return distance <= 100; // Distância de ativação do morcego
    }

    // Verifica se a animação de abrir asas já foi completa
    private boolean hasOpeningWingsFinished() {
        return openingWingsAnim.isLastFrame();
    }

    // Movimenta o morcego com base na sua ação atual
    @Override
    public void move() {
        if (getCurrentAction() == ACTIVE && !getIsExploding()) {
            float deltaX = getGameState().megaMan.getPosX() - getPosX();
            float deltaY = getGameState().megaMan.getPosY() - getPosY();
            float distance = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);

            if (distance > 0) {
                setSpeedX((float) (deltaX / distance * speed));
                setSpeedY((float) (deltaY / distance * speed));
            }

            setPosX(getPosX() + getSpeedX());
            setPosY(getPosY() + getSpeedY());
        } else {
            setPosX(getPosX());
            setPosY(getPosY());
        }
    }

    // Atualiza as ações do
    @Override
    public void update() {
        super.update();
        if (getCurrentAction() == NOACTIVE && isMegaManInRange()) {
            setCurrentAction(ACTIVANTING);
            openingWingsAnim.reset();
        }

        if (getCurrentAction() == ACTIVANTING && hasOpeningWingsFinished()) {
            setCurrentAction(ACTIVE);
        }
        move();
    }

    // Verifica se houve colisão com alguma entidade inimiga
    @Override
    public Rectangle getBoundForCollisionWithEnemy() {
        return new Rectangle(
            (int) (getPosX() - getWidth() / 2), 
            (int) (getPosY() - getHeight() / 2), 
            (int) getWidth(), 
            (int) getHeight());
    }

    // Desenha o morcego na tela com base na ação atual
    @Override
    public void draw(Graphics2D g2) {
        if (!isObjectOutOfCameraView()) {
            if(getIsExploding()) {
                drawDeathAnimation(g2);
            } else if (getCurrentAction() == NOACTIVE) {
                idleAnim.Update(System.nanoTime());
                idleAnim.draw((int) (getPosX() - getGameState().camera.getPosX()),
                        (int) (getPosY() - getGameState().camera.getPosY()), g2);
            } else if (getCurrentAction() == ACTIVANTING) {
                openingWingsAnim.Update(System.nanoTime());
                openingWingsAnim.draw((int) (getPosX() - getGameState().camera.getPosX()),
                        (int) (getPosY() - getGameState().camera.getPosY()), g2);
            } else {
                flyingAnim.Update(System.nanoTime());
                flyingAnim.draw((int) (getPosX() - getGameState().camera.getPosX()),
                        (int) (getPosY() - getGameState().camera.getPosY()), g2);
            }
        }
    }
}
