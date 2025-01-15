package com.gamestudio.elements;

import com.gamestudio.state.GameState;
import com.gamestudio.effect.Animation;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public abstract class ParticularObject extends GameElement {

    public static final int ALLY_TEAM = 0;
    public static final int ENEMY_TEAM = 1;
    public static final int LEFT = 0;
    public static final int RIGHT = 1;
    public static final int ALIVE = 0;
    public static final int DEATH = 1;
    public static final int BEHURT = 2;

    private int currentState = 0; //Vivo ou morto
    private float width;
    private float height;
    private float mass;
    private int speedX;
    private int speedY;
    private int amountLife; //Quantidade de vida do objeto
    private int damage; //Quantidade de dado que o objeto da
    private int direction; //Direita ou esquerda
    private int teamType;
    protected Animation behurtAnim;

    public ParticularObject(int x, int y, float width, float height, float mass, int amountLife, GameState gameState) {
        super(x, y, gameState);
        this.setWidth(width);
        this.setHeight(height);
        this.setMass(mass);
        this.setAmountLife(amountLife);
        this.direction = RIGHT;
    }

    public int getCurrentState() {
        return this.currentState;
    }

    public void setCurrentState(int currentState) {
        this.currentState = currentState;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getMass() {
        return mass;
    }

    public void setMass(float mass) {
        this.mass = mass;
    }

    public int getSpeedX() {
        return speedX;
    }

    public void setSpeedX(int speedX) {
        this.speedX = speedX;
    }

    public int getSpeedY() {
        return speedY;
    }

    public void setSpeedY(int speedY) {
        this.speedY = speedY;
    }

    public int getAmountLife() {
        return amountLife;
    }

    public void setAmountLife(int amountLife) {
        if (amountLife >= 0) {
            this.amountLife = amountLife;
        } else {
            this.amountLife = 0;
        }
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

    public abstract void attack();

    /*public boolean isObjectOutOfCameraView() {
        return this.getPosX() - this.getState().camera.getPosX() > this.getState().camera.getWidthView() || this.getPosX() - this.getState().camera.getPosX() < -50.0F || this.getPosY() - this.getState().camera.getPosY() > this.getState().camera.getHeightView() || this.getPosY() - this.getState().camera.getPosY() < -50.0F;
    }
    */

    public Rectangle getBoundForCollisionWithMap() { //Essa função
        Rectangle bound = new Rectangle();
        bound.x = (int)(this.getPosX() - this.getWidth() / 2.0F);
        bound.y = (int)(this.getPosY() - this.getHeight() / 2.0F);
        bound.width = (int)this.getWidth();
        bound.height = (int)this.getHeight();
        return bound;
    }

    public void beHurt(int damgeEat) {
        this.setAmountLife(this.getAmountLife() - damgeEat);
        this.currentState = BEHURT;
        this.hurtingCallback();
    }

    public void Update() {
        switch (this.currentState) {
            case ALIVE:
                // verifica se colidiu com projetiu de inimigo ou não
                ParticularObject object = this.getGameWorld().particularObjectManager.getCollisionWidthEnemyObject(this);
                if (object != null && object.getDamage() > 0) {
                    System.out.println("Take damage...");
                    this.beHurt(object.getDamage()); // Aplica dano ao personagem
                }
                break;

            case BEHURT:
                // O personagem tomou dano então a animação de dano ocorre
                if (this.behurtAnim == null) {
                    this.currentState = ALIVE;
                    if (this.getAmountLife() <= 0) {
                        this.currentState = DEATH;
                    }
                } else {
                    // Executa a animação de ferimento
                    this.behurtAnim.Update(System.nanoTime());
                    if (this.behurtAnim.isLastFrame()) {
                        this.behurtAnim.reset();
                        this.currentState = ALIVE;
                        if (this.getAmountLife() <= 0) {
                            this.currentState = DEATH;
                        }
                    }
                }
                break;

            case DEATH:
                System.out.println("Mega-Man is Dead.");
                break;

            default:
                this.currentState = ALIVE;
                break;
        }
    }


    public void drawBoundForCollisionWithMap(Graphics2D g2) {
            Rectangle rect = this.getBoundForCollisionWithMap();
            g2.setColor(Color.BLUE);
            g2.drawRect(rect.x - (int)this.getGameWorld().camera.getPosX(), rect.y - (int)this.getGameWorld().camera.getPosY(), rect.width, rect.height);
        }

        public void drawBoundForCollisionWithEnemy(Graphics2D g2) {
            Rectangle rect = this.getBoundForCollisionWithEnemy();
            g2.setColor(Color.RED);
            g2.drawRect(rect.x - (int)this.getGameWorld().camera.getPosX(), rect.y - (int)this.getGameWorld().camera.getPosY(), rect.width, rect.height);
        }

        public abstract Rectangle getBoundForCollisionWithEnemy();

        public abstract void draw(Graphics2D var1);

        public void hurtingCallback() {
        }

    }

}


