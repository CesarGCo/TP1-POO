package com.gamestudio.elements;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;

import com.gamestudio.effect.Animation;
import com.gamestudio.manager.DataLoader;
import com.gamestudio.state.GameState;

/*
    Esta classe representa o boss final do jogo
    Sua função basicamente é administrar as ações do boss, tais como:
        1. Ativar as animações corretas para cada ação requerida
        2. Tocar os sons do boss
        3. Definir as funcionalidade básicas do boss, como atacar e pular com base na sua ação atual
*/
public class WoodMan extends SmartGameEntity {
    // Ações que o boss pode executar
    private static final int INTRO = 0;
    private static final int BEATING_CHEST = 1;
    private static final int IDLE = 2;
    private static final int JUMPING = 3;
    private static final int LEAF_SHIELD_THROW = 4;

    private long stateStartTime;

    // Animações
    private final Animation IntroAnimation;
    private final Animation ChestBeatAnimation, ChestBeatBackAnimation;
    private final Animation IdleAnimation, IdleBackAnimation;
    private final Animation JumpingAnimation, JumpingBackAnimation;
    private final Animation LeafShieldThrowAnimation, LeafShieldThrowBackAnimation;
    // Imagem da face do boss
    private final Image face;
    // Imagem da barra de vida do boss
    private final Image lifeBar;
    // Projétil disparado pelo boss
    private Projectile leafShield;

    public WoodMan(int x, int y, GameState gameState) {
        super(x, y, 34, 31, 0.1f, 56, gameState);
        setCurrentAction(INTRO);
        stateStartTime = System.currentTimeMillis();
        this.setDirection(WoodMan.LEFT);
        setTeamType(ENEMY_TEAM);
        setDamage(10);

        // inicializando animações do boss
        IntroAnimation = DataLoader.getInstance().getAnimation("wood_man_intro");
        ChestBeatAnimation = DataLoader.getInstance().getAnimation("wood_man_beat");
        IdleAnimation = DataLoader.getInstance().getAnimation("wood_man_idle");
        JumpingAnimation = DataLoader.getInstance().getAnimation("wood_man_jumping");
        LeafShieldThrowAnimation = DataLoader.getInstance().getAnimation("wood_man_leaf_shield_throw");

        ChestBeatBackAnimation = DataLoader.getInstance().getAnimation("wood_man_beat");
        ChestBeatBackAnimation.flipAllImage();
        IdleBackAnimation = DataLoader.getInstance().getAnimation("wood_man_idle");
        IdleBackAnimation.flipAllImage();
        JumpingBackAnimation = DataLoader.getInstance().getAnimation("wood_man_jumping");
        JumpingBackAnimation.flipAllImage();
        LeafShieldThrowBackAnimation = DataLoader.getInstance().getAnimation("wood_man_leaf_shield_throw");
        LeafShieldThrowBackAnimation.flipAllImage();
        face = DataLoader.getInstance().getFrameImage("wood_man_face").getImage();
        lifeBar = DataLoader.getInstance().getFrameImage("wood_man_lifebar").getImage();

        setDeathAnimation(DataLoader.getInstance().getAnimation("explosion_effect"));
        setDeathSound(DataLoader.getInstance().getSound("enemy_death"));
        setDeathTime(400);
    }

    // O boss não possui a ação de correr
    @Override
    public void run() {}

    // Executa a ação do pular do boss alterando a velocidade vertical
    @Override
    public void jump() {
        if (!getIsJumping()) {
            setIsJumping(true);
            setSpeedY(-2);
            setSpeedX(getDirection() == LEFT ? -2 : 2);
        }
    }

    // O boss não possui a ação de correr
    @Override
    public void stopRun() {}

    // Cria um novo projétil para o boss iniciar o ataque
    public void setUpAttack() {
        this.leafShield = new LeafShield(getPosX(), getPosY(), getGameState());
        this.leafShield.setSpeedX(0);
        this.leafShield.setPosY(this.getPosY());
        this.leafShield.setPosX(this.getPosX());
        this.leafShield.setTeamType(this.getTeamType());
        getGameState().projectileManager.addObject(this.leafShield);
    }

    // Define uma velocidade para o projétil do bos
    @Override
    public void attack() {
        this.leafShield.setSpeedX(this.getDirection() == LEFT ? -1 : 1);
        this.leafShield.setPosX(this.getPosX());
        this.leafShield.setPosY(this.getPosY());
    }

    // Verifica se houve colisão com alguma entidade inimiga
    @Override
    public Rectangle getBoundForCollisionWithEnemy() {
        Rectangle rect = getBoundForCollisionWithMap();
        rect.x = (int) (getPosX() - (float) getWidth() / 2);
        rect.y = (int) (getPosY() - (float) getHeight() / 2);
        rect.width = getWidth();
        rect.height = getHeight();

        return rect;
    }

    // Atualiza os parâmetros necessários do boss com base na sua ação atual
    @Override
    public void update() {
        super.update();
        long elapsedTime = System.currentTimeMillis() - stateStartTime;

        switch (getCurrentAction()) {
            case INTRO: // Ação do boss no início da batalha
                if (elapsedTime > 2000) {
                    setCurrentAction(BEATING_CHEST);
                    stateStartTime = System.currentTimeMillis();
                }
                break;

            case BEATING_CHEST: // Boss realiza batida no peito (gorila)
                if (elapsedTime > 3000) {
                    setUpAttack();
                    setCurrentAction(LEAF_SHIELD_THROW);
                    stateStartTime = System.currentTimeMillis();
                }
                break;

            case LEAF_SHIELD_THROW: // Boss lança projétil
                if (elapsedTime > 1000) {
                    attack();
                    setCurrentAction(IDLE);
                    stateStartTime = System.currentTimeMillis();
                }
                break;

            case IDLE: // Boss parado
                if (elapsedTime > 2000) {
                    setCurrentAction(JUMPING);
                    stateStartTime = System.currentTimeMillis();
                }
                break;

            case JUMPING: // Boss pula
                if (!getIsJumping()) {
                    setCurrentAction(BEATING_CHEST);
                    stateStartTime = System.currentTimeMillis();
                }
                break;
        }

        performCurrentStateAction();

        if (!getIsJumping()) setSpeedX(0);
    }

    // Atualiza todas as animações com base na ação atual
    private void performCurrentStateAction() {
        switch (getCurrentAction()) {
            case INTRO:
                IntroAnimation.Update(System.nanoTime());
                break;

            case BEATING_CHEST:
                if (getDirection() == LEFT) {
                    ChestBeatAnimation.Update(System.nanoTime());
                } else {
                    ChestBeatBackAnimation.Update(System.nanoTime());
                }
                break;

            case LEAF_SHIELD_THROW:
                LeafShieldThrowAnimation.Update(System.nanoTime());
                break;

            case IDLE:
                if (getDirection() == LEFT) {
                    IdleAnimation.Update(System.nanoTime());
                } else {
                    IdleBackAnimation.Update(System.nanoTime());
                }
                break;

            case JUMPING:
                if (!getIsJumping()) jump();
                if (getDirection() == LEFT) {
                    JumpingAnimation.Update(System.nanoTime());
                } else {
                    JumpingBackAnimation.Update(System.nanoTime());
                }
                break;
        }
    }

    // Desenha o boss na tela
    @Override
    public void draw(Graphics2D g2) {
        int drawX = (int) (getPosX() - getGameState().camera.getPosX());
        int drawY = (int) (getPosY() - getGameState().camera.getPosY());

        Animation currentAnimation = getAnimation();

        if (getGameState().gameEntityManager.getGameState().megaMan.getPosX() > this.getPosX()) {
            this.setDirection(WoodMan.RIGHT);
        } else {
            this.setDirection(WoodMan.LEFT);
        }

        if (getIsExploding()) {
            drawDeathAnimation(g2);
        } else {
            currentAnimation.draw(drawX, drawY, g2);
        }
        drawLifeBar(g2);
    }

    // Desenha a barra de vida do boss
    public void drawLifeBar(Graphics2D g2d) {
        int life = Math.max(getAmountLife(), 0);
        g2d.setColor(Color.green);
        g2d.fillRect(350 - life, 14, life, 8);
        g2d.drawImage(lifeBar, 290, 10, 64, lifeBar.getHeight(null),null);
        g2d.drawImage(face, 360, 10, null);
    }

    // Retorna uma animação dependendo da ação no qual o boss se encontra
    private Animation getAnimation() {
        Animation currentAnimation;
        switch (getCurrentAction()) {
            case INTRO -> currentAnimation = IntroAnimation;
            case BEATING_CHEST ->
                    currentAnimation = this.getDirection() == LEFT ? ChestBeatAnimation : ChestBeatBackAnimation;
            case IDLE -> currentAnimation = this.getDirection() == LEFT ? IdleAnimation : IdleBackAnimation;
            case JUMPING -> currentAnimation = this.getDirection() == LEFT ? JumpingAnimation : JumpingBackAnimation;
            case LEAF_SHIELD_THROW ->
                    currentAnimation = this.getDirection() == LEFT ? LeafShieldThrowAnimation : LeafShieldThrowBackAnimation;
            default -> throw new IllegalStateException("Unexpected state: " + getCurrentAction());
        }
        return currentAnimation;
    }

}
