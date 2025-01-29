package com.gamestudio.elements;

import com.gamestudio.state.GameState;
import com.gamestudio.effect.Animation;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;

import javax.sound.sampled.Clip;
import javax.swing.Timer;

// A calsse GameEntity representa uma entidade dentro do jogo
// Uma entidade seria algum elemento que possui interação com colisão e possui estados bem definidos
// Como por exemplo estado de morte, vivo, direção e qual time faz parte
public abstract class GameEntity extends GameElement {
    // Estados que a entidade pode possuir
    public static final int ALLY_TEAM = 0;
    public static final int ENEMY_TEAM = 1;
    public static final int ITEM_TEAM = 2;
    public static final int LEFT = 0;
    public static final int RIGHT = 1;
    public static final int ALIVE = 0;
    public static final int DEATH = 1;
    public static final int BEHURT = 2;

    private int currentState = 0;//Vivo ou morto
    private int currentAction; // Qual ação a entidade está fazendo

    // Parâmetros físicos da entidade:
    private final int width; 
    private final int height;
    private final float mass;
    private float speedX;
    private float speedY;

    private int amountLife; // Quantidade de vida
    private final int maxLife; // Vida máxima
    private int damage; //Quantidade de dado que o objeto da
    private int direction; //Direita ou esquerda
    private int teamType; // Time da entidade
    private boolean isExploding = false; 
    private boolean isInvencible = false;
    private int deathTime; // Tempo de duração da animação de morte
    private Animation deathAnimation; // Animação de morte
    private Clip deathSound; // Efeito sonoro da morte

    public GameEntity(float x, float y, int width, int height, float mass, int amountLife, GameState gameState) {
        super(x, y, gameState);
        this.width = width;
        this.height = height;
        this.mass = mass;
        this.amountLife = amountLife;
        this.maxLife = amountLife;
    }

    public int getCurrentState() {
        return this.currentState;
    }

    public void setCurrentState(int currentState) {
        this.currentState = currentState;
    }

    public int getCurrentAction() {
        return currentAction;
    }
    public void setCurrentAction(int currentAction) {
        this.currentAction = currentAction;
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

    public int getHeight() {
        return height;
    }

    public float getMass() {
        return mass;
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

    // Verifica se a entidade está nos limites da câmera do jogo
    public boolean isObjectOutOfCameraView() {
        return this.getPosX() - this.getGameState().camera.getPosX() > this.getGameState().camera.getWidthView() || this.getPosX() - this.getGameState().camera.getPosX() < -50.0F || this.getPosY() - this.getGameState().camera.getPosY() > this.getGameState().camera.getHeightView() || this.getPosY() - this.getGameState().camera.getPosY() < -50.0F;
    }

    // Retorna todos os parâmetros necessários para verificar a colisão da entidade com o mapa
    public Rectangle getBoundForCollisionWithMap() {
        Rectangle bound = new Rectangle();
        bound.x = (int) (this.getPosX() - this.getWidth() / 2);
        bound.y = (int) (this.getPosY() - this.getHeight() / 2);
        bound.width = (int) this.getWidth();
        bound.height = (int) this.getHeight();
        return bound;
    }

    // Diminui a vida da entidade com base no dano
    public void beHurt(int damageEat) {
        this.setAmountLife(this.getAmountLife() - damageEat);
        this.currentState = BEHURT;
    }

    // Aumenta a vida da entidade com base na cura recebida
    public void regen(int life) {
        this.setAmountLife(Math.min(this.getAmountLife() + life, maxLife));
    }

    // Atualiza a entidade com base em qual estado ela está
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
                // Se entidade morrer, irá rodar um cronômetro para
                // definir o tempo da animação de morte da entidade
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
                // Se o efeito sonoro de morte não ocorreu, ele é iniciado
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

    // Retorna parâmetros que serão utilizados para verificar a colisão com inimigos
    public abstract Rectangle getBoundForCollisionWithEnemy();

    // Desenha a entidade na tela
    public abstract void draw(Graphics2D var1);
    
    // Desenha a animação de morte da entidade
    public void drawDeathAnimation(Graphics2D g2d) {
        deathAnimation.Update(System.nanoTime());
        deathAnimation.draw(
            (int)(getPosX() - getGameState().camera.getPosX()), 
            (int) (getPosY() - getGameState().camera.getPosY()),
            g2d
        );
    }
}


