package com.gamestudio.elements;

import com.gamestudio.manager.DataLoader;
import com.gamestudio.state.GameState;
import com.gamestudio.effect.Animation;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.Timer;

import javax.sound.sampled.Clip;

/*
    Esta classe representa o personagem principal do jogo
    Sua função basicamente é administrar as ações do personagem, tais como:
        1. Ativar as animações corretas para cada ação requerida
        2. Tocar os sons do personagem 
        3. Definir as funcionalidade básicas do personagem, como atacar e pular
*/
public class MegaMan extends SmartGameEntity {
    // Todas as animações que o personagem pode executar:
    private final Animation runForwardAnim, runBackAnim, runShootingForwarAnim, runShootingBackAnim;
    private final Animation idleForwardAnim, idleBackAnim, idleShootingForwardAnim, idleShootingBackAnim;
    private final Animation flyForwardAnim, flyBackAnim, flyShootingForwardAnim, flyShootingBackAnim;
    private final Animation behurtForwardAnim, behurtBackAnim;
    // Frames da barra de vida do personagem
    private final ArrayList<Image> lifeBar;
    // Imagem da cabeça do personagem
    private final Image face;
    private long lastShootingTime; // tempo do último dispado
    private boolean isShooting = false; // define se o persnagem está atirando ou não

    private final Clip hurtingSound;
    private final Clip shooting1;

    public MegaMan(int x, int y, GameState gameState) {
        super(x, y, 16, 24, 0.1f, 29, gameState);
        this.lifeBar = new ArrayList<>();
        // Armazena todas as animações:
        shooting1 = DataLoader.getInstance().getSound("Shoot");
        hurtingSound = DataLoader.getInstance().getSound("Mega_man_hit");

        runForwardAnim = DataLoader.getInstance().getAnimation("mega_man_running");
        runBackAnim = DataLoader.getInstance().getAnimation("mega_man_running");
        runForwardAnim.flipAllImage();

        idleForwardAnim = DataLoader.getInstance().getAnimation("mega_man_idle");
        idleBackAnim = DataLoader.getInstance().getAnimation("mega_man_idle");
        idleForwardAnim.flipAllImage();

        flyForwardAnim = DataLoader.getInstance().getAnimation("mega_man_jump");
        flyForwardAnim.setIsRepeated(false);
        flyBackAnim = DataLoader.getInstance().getAnimation("mega_man_jump");
        flyForwardAnim.setIsRepeated(false);
        flyForwardAnim.flipAllImage();

        behurtForwardAnim = DataLoader.getInstance().getAnimation("mega_man_hurt");
        behurtBackAnim = DataLoader.getInstance().getAnimation("mega_man_hurt");
        behurtForwardAnim.flipAllImage();

        idleShootingForwardAnim = DataLoader.getInstance().getAnimation("mega_man_shooting");
        idleShootingBackAnim = DataLoader.getInstance().getAnimation("mega_man_shooting");
        idleShootingForwardAnim.flipAllImage();

        runShootingForwarAnim = DataLoader.getInstance().getAnimation("mega_man_shooting_running");
        runShootingBackAnim = DataLoader.getInstance().getAnimation("mega_man_shooting_running");
        runShootingForwarAnim.flipAllImage();

        flyShootingForwardAnim = DataLoader.getInstance().getAnimation("mega_man_shooting_jumping");
        flyShootingBackAnim = DataLoader.getInstance().getAnimation("mega_man_shooting_jumping");
        flyShootingForwardAnim.flipAllImage();
        setDeathAnimation(DataLoader.getInstance().getAnimation("mega_man_death"));

        // Armazena todos os frames da barra de vida
        for(int i = 1; i < 30; i++){
            lifeBar.addFirst(DataLoader.getInstance().getFrameImage("life_bar" + i).getImage());
        }
        
        // Armazena a imagem da face do personagem
        this.face = DataLoader.getInstance().getFrameImage("mega_man_face").getImage();

        // Armazena o som de morte
        setDeathSound(DataLoader.getInstance().getSound("death"));

        // Definição de parâmetros básicos:
        this.setDirection(MegaMan.RIGHT);
        setTeamType(ALLY_TEAM);
        setDeathTime(800);
    }

    // Atualiza os parâmetros neessários do personagem
    @Override
    public void update() {
        super.update();
        if (isShooting) {
            if (System.nanoTime() - lastShootingTime > 900 * 200000) { // Controle da taxa de disparo do personagem
                isShooting = false;
            }
        }

    }

    // Retorna parâmetros que serão utilizados para verificar a colisão com inimigos
    @Override
    public Rectangle getBoundForCollisionWithEnemy() {
        Rectangle rect = getBoundForCollisionWithMap();
        rect.x =  (int) (getPosX() - 8);
        rect.y =  (int) (getPosY() - 12);
        rect.width = 18;
        rect.height = 24;

        return rect;
    }

    // Desenha a animação do Mega Man com base em qual estado ele se encontra e qual direção ele está 
    @Override
    public void draw(Graphics2D g2d) {
        if(getIsExploding()){
            drawDeathAnimation(g2d);
            return;
        }
        switch (getCurrentState()) {
            case ALIVE:
                if(!getIsInvencible()) {
                    if (getIsJumping()) {
                        if (getDirection() == RIGHT) {
                            flyForwardAnim.Update(System.nanoTime());
                            if (isShooting) {
                                flyShootingForwardAnim.setCurrentFrame(flyForwardAnim.getCurrentFrame());
                                flyShootingForwardAnim.draw((int) (getPosX() - getGameState().camera.getPosX()) + 10, (int) (getPosY() -  getGameState().camera.getPosY()), g2d);
                            } else
                                flyForwardAnim.draw((int) (getPosX() - getGameState().camera.getPosX()), (int) (getPosY() -  getGameState().camera.getPosY()), g2d);
                        } else {
                            flyBackAnim.Update(System.nanoTime());
                            if (isShooting) {
                                flyShootingBackAnim.setCurrentFrame(flyBackAnim.getCurrentFrame());
                                flyShootingBackAnim.draw((int) (getPosX() - getGameState().camera.getPosX()) - 10,(int) (getPosY() -  getGameState().camera.getPosY()), g2d);
                            } else
                                flyBackAnim.draw((int) (getPosX() - getGameState().camera.getPosX()), (int) (getPosY() -  getGameState().camera.getPosY()), g2d);
                        }
    
                    } else {
                        if (getSpeedX() > 0) {
                            setRunFowardAnimation(g2d, runForwardAnim, runShootingForwarAnim);
                        } else if (getSpeedX() < 0) {
                            setRunFowardAnimation(g2d, runBackAnim, runShootingBackAnim);
                        } else {
                            if (getDirection() == RIGHT) {
                                setIdleShootingAnimation(g2d, idleShootingForwardAnim, idleForwardAnim);
                            } else {
                                setIdleShootingAnimation(g2d, idleShootingBackAnim, idleBackAnim);
                            }
                        }
                    }
                } else {
                    if (getDirection() == RIGHT) {
                        behurtForwardAnim.Update(System.nanoTime());
                        behurtForwardAnim.draw((int) (getPosX() - getGameState().camera.getPosX()),(int) (getPosY() - getGameState().camera.getPosY()), g2d);
                    } else {
                        behurtBackAnim.Update(System.nanoTime());
                        behurtBackAnim.draw((int)(getPosX() - getGameState().camera.getPosX()), (int) (getPosY() - getGameState().camera.getPosY()), g2d);
                    }
                }
                break;

            case BEHURT:
                hurtingSound.setFramePosition(0); 
                hurtingSound.start();
                setIsInvencible(true);
                Timer timer = new Timer(500, (ActionEvent e) -> { 
                    setIsInvencible(false);
                    ((Timer) e.getSource()).stop();
                });
                timer.setRepeats(false);
                timer.start();
                break;
            default:
                break;
        }
        drawLifeBar(g2d);
    }

    // Desenha a barra de vida do Mega Man
    public void drawLifeBar(Graphics2D g2d) {
        g2d.drawImage(face, 8, 10, null);
        g2d.drawImage(lifeBar.get(Math.max(getAmountLife()-1, 0)),10, 25, null);
    }

    // Seleciona qual animação do Mega Man parado deve ser desenhada
    void setIdleShootingAnimation(Graphics2D g2d, Animation idleShootingForwardAnim, Animation idleForwardAnim) {
        if (isShooting) {
            idleShootingForwardAnim.Update(System.nanoTime());
            idleShootingForwardAnim.draw((int)(getPosX() - getGameState().camera.getPosX()), (int)(getPosY() - getGameState().camera.getPosY()), g2d);
        } else {
            idleForwardAnim.Update(System.nanoTime());
            idleForwardAnim.draw((int)(getPosX() - getGameState().camera.getPosX()), (int)(getPosY() - getGameState().camera.getPosY()), g2d);
        }
    }

    // Seleciona qual animação do Mega Man andando para trás será animada
    void setRunFowardAnimation(Graphics2D g2d, Animation runForwardAnim, Animation runShootingForwarAnim) {
        runForwardAnim.Update(System.nanoTime());
        if (isShooting) {
            runShootingForwarAnim.setCurrentFrame(runForwardAnim.getCurrentFrame() - 1);
            runShootingForwarAnim.draw((int)(getPosX() - getGameState().camera.getPosX()), (int)( getPosY() - getGameState().camera.getPosY()), g2d);
        } else
            runForwardAnim.draw((int)(getPosX() - getGameState().camera.getPosX()), (int)( getPosY() - getGameState().camera.getPosY()), g2d);
        if (runForwardAnim.getCurrentFrame() == 1) runForwardAnim.setIgnoreFrame(0);
    }

    // Seta a velocidade na horizontal do Mega man com base na direção atual
    @Override
    public void run() {
        if (getDirection() == LEFT)
            setSpeedX(-2);
        else setSpeedX(2);
    }

    // Altera a velocidade na vertical do Mega man se estiver pulando
    @Override
    public void jump() {
        if (!getIsJumping()) {
            setIsJumping(true);
            setSpeedY(-3);
            flyBackAnim.reset();
            flyForwardAnim.reset();
        }
    }

    // Encerra a animação de corrida e zera a velocidade
    @Override
    public void stopRun() {
        setSpeedX(0);
        runForwardAnim.reset();
        runBackAnim.reset();
        runForwardAnim.unIgnoreFrame(0);
        runBackAnim.unIgnoreFrame(0);
    }

    // Realiza um disparo com a arma 
    @Override
    public void attack() {
        if (!isShooting) {
            if(shooting1.isRunning()) {  
                shooting1.stop();
            } else {
                shooting1.setFramePosition(0); 
                shooting1.start();
            }
            // Define um novo projétil
            Projectile projectile = new MegaManBullet(getPosX(), getPosY(), getGameState());
            // Verifica a direção do persongem e altera a velocidade em X do projétil
            if (getDirection() == LEFT) {
                projectile.setSpeedX(-6);
                projectile.setPosX(projectile.getPosX() - 10);
                if (getSpeedX() != 0 && getSpeedY() == 0) {
                    projectile.setPosX(projectile.getPosX() - 6);
                    projectile.setPosY(projectile.getPosY());
                }
            } else {
                projectile.setSpeedX(6);
                projectile.setPosX(projectile.getPosX() + 8);
                if (getSpeedX() != 0 && getSpeedY() == 0) {
                    projectile.setPosX(projectile.getPosX() + 6);
                    projectile.setPosY(projectile.getPosY());
                }
            }
            if (getIsJumping())
                projectile.setPosY(projectile.getPosY() - 4);

            projectile.setTeamType(getTeamType());
            getGameState().projectileManager.addObject(projectile);

            lastShootingTime = System.nanoTime();
            this.isShooting = true;
        }
    }

}