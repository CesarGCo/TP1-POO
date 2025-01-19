package com.gamestudio.elements;

import com.gamestudio.state.GameState;
import com.gamestudio.effect.Animation;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;

import javax.sound.sampled.Clip;
import javax.swing.Timer;

public abstract class Robot extends GameElement {
    public static final int ALLY_TEAM = 0;
    public static final int ENEMY_TEAM = 1;
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
    private int amountLife; //Quantidade de vida do objeto
    private int damage; //Quantidade de dado que o objeto da
    private int direction; //Direita ou esquerda
    private int teamType;
    private boolean isExplosion = false;
    private boolean isInvencible = false;
    private Animation deathAnimation;
    private Clip deathClip;

    public Robot(float x, float y, int width, int height, float mass, int amountLife, GameState gameState) {
        super(x, y, gameState);
        this.setWidth(width);
        this.setHeight(height);
        this.setMass(mass);
        this.setAmountLife(amountLife);
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

    public boolean getIsExplosion() {
        return this.isExplosion;
    }

    public void setIsExplosion(boolean isExplosion) {
        this.isExplosion = isExplosion;
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

    public void update() {
        switch (this.currentState) {
            case ALIVE:
                // verifica se colidiu com projetiu de inimigo ou não
                Robot object1 = this.getGameState().robotManager.getCollisionWidthEnemyObject(this);
                Robot object2 = this.getGameState().projectileManager.getCollisionWidthEnemyObject(this);
                if (object1 != null && object1.getDamage() > 0 && !isInvencible) {
                    this.setAmountLife(this.getAmountLife() - object1.getDamage());
                    this.currentState = BEHURT;
                } else if(object2 != null && object2.getDamage() > 0 && !isInvencible){
                    this.setAmountLife(this.getAmountLife() - object2.getDamage());
                    this.currentState = BEHURT;
                }
                break;

            case BEHURT:
                this.currentState = ALIVE;
                if (this.getAmountLife() <= 0) {
                    this.isExplosion = true;
                    Timer timer = new Timer(1000, (ActionEvent e) -> { 
                        isExplosion = false;
                        ((Timer) e.getSource()).stop();
                    });
                    timer.setRepeats(false);
                    timer.start();
                    this.currentState = DEATH;
                }
                break;

            case DEATH:
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


