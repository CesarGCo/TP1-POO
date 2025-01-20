package com.gamestudio.elements;

import com.gamestudio.state.GameState;
import com.gamestudio.effect.Animation;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;

import javax.sound.sampled.Clip;
import javax.swing.Timer;

public abstract class GameEntity extends GameElement {
    public static final int ALLY_TEAM = 0;
    public static final int ENEMY_TEAM = 1;
    public static final int ITEM_TEAM = 2;
    public static final int LEFT = 0;
    public static final int RIGHT = 1;
    public static final int ALIVE = 0;
    public static final int DEATH = 1;
    public static final int BEHURT = 2;
    public static final int NOBEHURT = 3;

    private int currentState = 0;//Vivo ou morto
    private int currentAction; 
    private int width;
    private int height;
    private float mass;
    private float speedX;
    private float speedY;
    private int amountLife; 
    private int maxLife;
    private int damage; //Quantidade de dado que o objeto da
    private int direction; //Direita ou esquerda
    private int teamType;
    private boolean isExploding = false;
    private boolean isInvencible = false;
    private int deathTime;
    private Animation deathAnimation;
    private Clip deathSound;

    public GameEntity(float x, float y, int width, int height, float mass, int amountLife, GameState gameState) {
        super(x, y, gameState);
        this.setWidth(width);
        this.setHeight(height);
        this.setMass(mass);
        this.setAmountLife(amountLife);
        this.maxLife = amountLife;
    }

    public int getCurrentState() {
        return this.currentState;
    }

    public void setCurrentState(int currentState) {
        this.currentState = currentState;
    }

    public void setCurrentAction(int currentAction) {
        this.currentAction = currentAction;
    }

    public int getCurrentAction() {
        return currentAction;
    }

    public boolean getIsInvencible() {
        return this.isInvencible;
    }

    public void setIsInvencible(boolean isInvencible) {
        this.isInvencible = isInvencible;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public float getMass() {
        return mass;
    }

    public void setMass(float mass) {
        this.mass = mass;
    }

    public float getSpeedX() {
        return speedX;
    }

    public void setSpeedX(float speedX) {
        this.speedX = speedX;
    }

    public float getSpeedY() {
        return speedY;
    }

    public void setSpeedY(float speedY) {
        this.speedY = speedY;
    }

    public int getAmountLife() {
        return amountLife;
    }

    public void setAmountLife(int amountLife) {
        this.amountLife = amountLife;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getTeamType() {
        return teamType;
    }

    public void setTeamType(int teamType) {
        this.teamType = teamType;
    }

    public void setDeathAnimation(Animation deathAnimation) {
        this.deathAnimation = deathAnimation;
    }

    public boolean getIsExploding() {
        return this.isExploding;
    }

    public void setIsExploding(boolean isExploding) {
        this.isExploding = isExploding;
    }

    public Clip getDeathSound() {
        return deathSound;
    }

    public void setDeathSound(Clip deathSound) {
        this.deathSound = deathSound;
    }

    public int getDeathTime() {
        return deathTime;
    }

    public void setDeathTime(int deathTime) {
        this.deathTime = deathTime;
    }
    public boolean isObjectOutOfCameraView() {
        return this.getPosX() - this.getGameState().camera.getPosX() > this.getGameState().camera.getWidthView() || this.getPosX() - this.getGameState().camera.getPosX() < -50.0F || this.getPosY() - this.getGameState().camera.getPosY() > this.getGameState().camera.getHeightView() || this.getPosY() - this.getGameState().camera.getPosY() < -50.0F;
    }

    public Rectangle getBoundForCollisionWithMap() { //Essa função
        Rectangle bound = new Rectangle();
        bound.x = (int) (this.getPosX() - this.getWidth() / 2);
        bound.y = (int) (this.getPosY() - this.getHeight() / 2);
        bound.width = (int) this.getWidth();
        bound.height = (int) this.getHeight();
        return bound;
    }

    public void beHurt(int damageEat) {
        this.setAmountLife(this.getAmountLife() - damageEat);
        this.currentState = BEHURT;
    }

    public void regen(int life) {
        this.setAmountLife(Math.min(this.getAmountLife() + life, maxLife));
    }

    public void update() {
        switch (this.currentState) {
            case ALIVE:
                // verifica se colidiu com projetiu de inimigo ou não
                GameEntity object1 = this.getGameState().gameEntityManager.getCollisionWidthEnemyObject(this);
                if (object1 != null && object1.getDamage() > 0 && !isInvencible) {
                    beHurt(object1.getDamage());
                }
                break;

            case BEHURT: 
                this.currentState = ALIVE;
                if (this.getAmountLife() <= 0) {
                    this.isExploding = true;
                    Timer timer = new Timer(deathTime, (ActionEvent e) -> { 
                        isExploding = false;
                        ((Timer) e.getSource()).stop();
                    });
                    timer.setRepeats(false);
                    timer.start();
                    this.currentState = DEATH;
                }
                break;

            case DEATH:
                if(deathSound != null && !deathSound.isRunning()) {
                    deathSound.setFramePosition(0); 
                    deathSound.start();
                }
                break;

            default:
                this.currentState = ALIVE;
                break;
        }
    }

    public void drawBoundForCollisionWithMap(Graphics2D g2) {
        Rectangle rect = this.getBoundForCollisionWithMap();
        g2.setColor(Color.BLUE);
        g2.drawRect((int) (rect.x - this.getGameState().camera.getPosX()), (int) (rect.y - this.getGameState().camera.getPosY()), rect.width, rect.height);
    }

    public void drawBoundForCollisionWithEnemy(Graphics2D g2) {
        Rectangle rect = this.getBoundForCollisionWithEnemy();
        g2.setColor(Color.RED);
        g2.drawRect((int) (rect.x - this.getGameState().camera.getPosX()), (int) (rect.y - this.getGameState().camera.getPosY()), rect.width, rect.height);
    }

    public abstract Rectangle getBoundForCollisionWithEnemy();

    public abstract void draw(Graphics2D var1);
    
    public void drawDeathAnimation(Graphics2D g2d) {
        deathAnimation.Update(System.nanoTime());
        deathAnimation.draw(
            (int)(getPosX() - getGameState().camera.getPosX()), 
            (int) (getPosY() - getGameState().camera.getPosY()),
            g2d
        );
    }
}


