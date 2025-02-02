package com.gamestudio.state;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import javax.sound.sampled.Clip;

import com.gamestudio.elements.*;
import com.gamestudio.interfaces.GameFrame;
import com.gamestudio.manager.DataLoader;
import com.gamestudio.manager.ItemManager;
import com.gamestudio.manager.ProjectileManager;
import com.gamestudio.manager.GameEntityManager;
import com.gamestudio.manager.StateManager;
import com.gamestudio.physical.PhysicalMap;

// A classe GameState representa o estado da gameplay
// Dessa forma, esta classe irá renderizar o mapa, personagem principa, inimigos, itens etc;
// 
public class GameState extends State {
    public PhysicalMap physicalMap;
    public MegaMan megaMan;
    public GameEntityManager gameEntityManager;
    public ProjectileManager projectileManager;
    public ItemManager itemManager;
    public Camera camera;
    private BufferedImage mapImage;
    private boolean drawHiboxes = false;
    private final Clip levelMusic;
    private Clip bossMusic;
    private boolean bossFightStarted = false;

    private long transformationStartTime;
    private boolean isTransformed = false;
    private boolean isOnCooldown = false;
    private long cooldownStartTime;

    public GameState(StateManager stateManager) {
        super(stateManager, new BufferedImage(GameFrame.width, GameFrame.height, BufferedImage.TYPE_INT_ARGB));
        this.levelMusic = DataLoader.getInstance().getSound("Level_soundtrack");
        this.bossMusic = DataLoader.getInstance().getSound("Boss_soundtrack");
        initState();
    }

    public BufferedImage getMapImage() {
        return mapImage;
    }

    // Inicializa a gameplay, definindo a posição do personagem e da camera, além de 
    // reiniciar variáveis utilizadas na lógica do estado
    public void initState() {
        this.gameEntityManager = new GameEntityManager(this);
        this.projectileManager = new ProjectileManager(this);
        this.itemManager = new ItemManager(this);

        this.physicalMap = new PhysicalMap(-16, 0, this);
        this.mapImage = DataLoader.getInstance().getFrameImage("new_map_fall").getImage();
        this.megaMan = new MegaMan(100, 100, this);
        this.camera = new Camera(0, 0, 400, 240, this);
        initEnemies();
        gameEntityManager.addObject(megaMan);
        megaMan.setCurrentState(SmartGameEntity.ALIVE);

        transformationStartTime = 0;
        isTransformed = false;
        isOnCooldown = false;
        cooldownStartTime = 0;
    }

    // Inicializa todos os inimigos da fase:
    private void initEnemies() {
        GameEntity bat1 = new Bat(250, 80, this);
        bat1.setDirection(GameEntity.LEFT);
        bat1.setTeamType(GameEntity.ENEMY_TEAM);
        gameEntityManager.addObject(bat1);

        GameEntity bat2 = new Bat(362, 80, this);
        bat2.setDirection(GameEntity.LEFT);
        bat2.setTeamType(GameEntity.ENEMY_TEAM);
        gameEntityManager.addObject(bat2);

        GameEntity bat3 = new Bat(630, 60, this);
        bat3.setDirection(GameEntity.LEFT);
        bat3.setTeamType(GameEntity.ENEMY_TEAM);
        gameEntityManager.addObject(bat3);

        GameEntity bat4 = new Bat(830, 70, this);
        bat4.setDirection(GameEntity.LEFT);
        bat4.setTeamType(GameEntity.ENEMY_TEAM);
        gameEntityManager.addObject(bat4);

        GameEntity bat5 = new Bat(1000, 60, this);
        bat5.setDirection(GameEntity.LEFT);
        bat5.setTeamType(GameEntity.ENEMY_TEAM);
        gameEntityManager.addObject(bat5);

        GameEntity bat6 = new Bat(1310, 20, this);
        bat6.setDirection(GameEntity.LEFT);
        bat6.setTeamType(GameEntity.ENEMY_TEAM);
        gameEntityManager.addObject(bat6);

        GameEntity bat7 = new Bat(1462, 20, this);
        bat7.setDirection(GameEntity.LEFT);
        bat7.setTeamType(GameEntity.ENEMY_TEAM);
        gameEntityManager.addObject(bat7);

        GameEntity bat8 = new Bat(1664, 12, this);
        bat8.setDirection(GameEntity.LEFT);
        bat8.setTeamType(GameEntity.ENEMY_TEAM);
        gameEntityManager.addObject(bat8);

        GameEntity bat9 = new Bat(2062, 50, this);
        bat9.setDirection(GameEntity.LEFT);
        bat9.setTeamType(GameEntity.ENEMY_TEAM);
        gameEntityManager.addObject(bat9);

        GameEntity bat10 = new Bat(2168, 30, this);
        bat10.setDirection(GameEntity.LEFT);
        bat10.setTeamType(GameEntity.ENEMY_TEAM);
        gameEntityManager.addObject(bat10);

        GameEntity bat11 = new Bat(2308, 50, this);
        bat11.setDirection(GameEntity.LEFT);
        bat11.setTeamType(GameEntity.ENEMY_TEAM);
        gameEntityManager.addObject(bat11);

        GameEntity robbit1 = new Rabbit(500, 100, this);
        robbit1.setDirection(GameEntity.RIGHT);
        robbit1.setTeamType(GameEntity.ENEMY_TEAM);
        gameEntityManager.addObject(robbit1);

        GameEntity robbit2 = new Rabbit(930, 100, this);
        robbit2.setDirection(GameEntity.RIGHT);
        robbit2.setTeamType(GameEntity.ENEMY_TEAM);
        gameEntityManager.addObject(robbit2);

        GameEntity robbit3 = new Rabbit(2030, 100, this);
        robbit3.setDirection(GameEntity.RIGHT);
        robbit3.setTeamType(GameEntity.ENEMY_TEAM);
        gameEntityManager.addObject(robbit3);

        GameEntity robbit4 = new Rabbit(2492, 100, this);
        robbit4.setDirection(GameEntity.RIGHT);
        robbit4.setTeamType(GameEntity.ENEMY_TEAM);
        gameEntityManager.addObject(robbit4);

        GameEntity goomba1 = new Goomba(384, 130, this);
        goomba1.setDirection(GameEntity.RIGHT);
        goomba1.setTeamType(GameEntity.ENEMY_TEAM);
        gameEntityManager.addObject(goomba1);

        GameEntity goomba2 = new Goomba(2244, 100, this);
        goomba2.setDirection(GameEntity.RIGHT);
        goomba2.setTeamType(GameEntity.ENEMY_TEAM);
        gameEntityManager.addObject(goomba2);
    }

    // Inicializa a Boss Fight contra o Wood Man
    private void initBossBattle() {
        this.bossFightStarted = true;
        levelMusic.stop();
        bossMusic.start();
        GameEntity woodman = new WoodMan(3000, 100, this);
        gameEntityManager.addObject(woodman);
    }

    // Atualiza todos os elementos presentes na gameplay
    // tais como a câmera  e entidades
    @Override
    public void update() {
        camera.update();
        gameEntityManager.updateObjects();
        projectileManager.updateObjects();
        itemManager.updateObjects();

        // Verifica se o Mega Man se transformou em outra variação
        if (isTransformed) {
            if (System.currentTimeMillis() - transformationStartTime >= 4000) {
                switchToNormalMegaMan();
                isTransformed = false;
                isOnCooldown = true;
                cooldownStartTime = System.currentTimeMillis();
            }
        }

        // Verifica o tempo de uso do poder do Mega Man
        if (isOnCooldown) {
            if (System.currentTimeMillis() - cooldownStartTime >= 10000) {
                isOnCooldown = false;
            }
        }

        // Verifica se o Mega Man chegou na área da boss fight
        if (this.megaMan.getPosX() == 2850 && !this.bossFightStarted) {
            initBossBattle();
        }

        // Inicia a música da boss fight, caso tenha se iniciado
        if (bossFightStarted && !bossMusic.isRunning()) {
            levelMusic.stop();
            bossMusic.setFramePosition(0);
            bossMusic.start();
        }

        // Toca a música da fase, caso a boss fight não tenha se iniciado
        if (!levelMusic.isRunning() && !this.bossFightStarted) {
            levelMusic.setFramePosition(0);
            levelMusic.start();
        }

        // Realiza a troca para o estado de Game Over caso o Mega Man morra
        if (megaMan.getCurrentState() == SmartGameEntity.DEATH && !megaMan.getIsExploding()) {
            bossMusic.stop();
            levelMusic.stop();
            getStateManager().setCurrentState(StateManager.GAMEOVER);
            initState();
            bossFightStarted = false;
        }
    }

    // Renderiza todos os elementos da gameplay
    @Override
    public void render() {
        Graphics g = getBufferedImage().getGraphics();
        Graphics2D g2 = (Graphics2D) g;
        drawMap(g2);
        projectileManager.draw(g2);
        gameEntityManager.draw(g2);
        itemManager.draw(g2);
        if (drawHiboxes) { // Caso a opção de visualizar colisões esteja ativada
            drawAllHitBox(g2);
        }
    }

    // O método abaixo renderiza o mapa,
    // Onde a tela de exibição será escalada para o tamanho da câmera do jogo
    private void drawMap(Graphics2D g2d) {
        float scaleX = (float) GameFrame.width / camera.getWidthView();
        float scaleY = (float) GameFrame.height / camera.getHeightView();
        g2d.scale(scaleX, scaleY);

        g2d.drawImage(
                mapImage,
                (int) (-camera.getPosX()),
                (int) (-camera.getPosY()),
                null
        );
    }

    public void setPressedButton(int code) {
        switch (code) {
            case KeyEvent.VK_D: // Andar para direita
                megaMan.setDirection(MegaMan.RIGHT);
                megaMan.run();
                break;

            case KeyEvent.VK_A: // Andar para esquerda
                megaMan.setDirection(MegaMan.LEFT);
                megaMan.run();
                break;

            case KeyEvent.VK_SPACE: // Pulo 
                megaMan.jump();
                break;

            case KeyEvent.VK_H: // Ataque
                megaMan.attack();
                break;

            case KeyEvent.VK_F1: // Visualizar hiboxes
                drawHiboxes = !drawHiboxes;
                break;

            case KeyEvent.VK_F: // Transformação para Fire Mega Man
                if (!isOnCooldown && !isTransformed) {
                    switchToFireMegaMan();
                    isTransformed = true;
                    transformationStartTime = System.currentTimeMillis();
                }
                break;
            case KeyEvent.VK_B: // Transformação para Water Mega Man
                if (!isOnCooldown && !isTransformed) {
                    switchToWaterMegaMan();
                    isTransformed = true;
                    transformationStartTime = System.currentTimeMillis();
                }
                break;
            case KeyEvent.VK_E: // Transformação para Eletric Mega Man
                if (!isOnCooldown && !isTransformed) {
                    switchToEletricMegaMan();
                    isTransformed = true;
                    transformationStartTime = System.currentTimeMillis();
                }
                break;
        }
    }

    // Troca o personagem para o Fire Mega Man
    private void switchToFireMegaMan() {
        FireMegaMan fireMegaMan = new FireMegaMan(
                (int) megaMan.getPosX(),
                (int) megaMan.getPosY(),
                this
        );
        fireMegaMan.setAmountLife(megaMan.getAmountLife());
        fireMegaMan.setCurrentState(megaMan.getCurrentState());
        fireMegaMan.setDirection(megaMan.getDirection());
        gameEntityManager.RemoveObject(megaMan);
        megaMan = fireMegaMan;
        gameEntityManager.addObject(megaMan);
    }

    // Troca o personagem para o Water Mega Man
    private void switchToWaterMegaMan() {
        WaterMegaMan waterMegaMan = new WaterMegaMan(
                (int) megaMan.getPosX(),
                (int) megaMan.getPosY(),
                this
        );
        waterMegaMan.setAmountLife(megaMan.getAmountLife());
        waterMegaMan.setCurrentState(megaMan.getCurrentState());
        waterMegaMan.setDirection(megaMan.getDirection());
        gameEntityManager.RemoveObject(megaMan);
        megaMan = waterMegaMan;
        gameEntityManager.addObject(megaMan);
    }

    // Troca o personagem para o Eletric Mega Man
    private void switchToEletricMegaMan() {
        EletricMegaMan eletricMegaMan = new EletricMegaMan(
                (int) megaMan.getPosX(),
                (int) megaMan.getPosY(),
                this
        );
        eletricMegaMan.setAmountLife(megaMan.getAmountLife());
        eletricMegaMan.setCurrentState(megaMan.getCurrentState());
        eletricMegaMan.setDirection(megaMan.getDirection());
        gameEntityManager.RemoveObject(megaMan);
        megaMan = eletricMegaMan;
        gameEntityManager.addObject(megaMan);
    }

    // Troca o personagem para o Mega Man padrão
    private void switchToNormalMegaMan() {
        MegaMan normalMegaMan = new MegaMan(
                (int) megaMan.getPosX(),
                (int) megaMan.getPosY(),
                this
        );
        normalMegaMan.setAmountLife(megaMan.getAmountLife());
        normalMegaMan.setCurrentState(megaMan.getCurrentState());
        normalMegaMan.setDirection(megaMan.getDirection());
        gameEntityManager.RemoveObject(megaMan);
        megaMan = normalMegaMan;
        gameEntityManager.addObject(megaMan);
    }

    // Caso alguma tecla seja solta:
    @Override
    public void setReleasedButton(int code) {
        switch (code) {
            case KeyEvent.VK_D: // Caso pare de andar para a direita
                if (megaMan.getSpeedX() > 0)
                    megaMan.stopRun();
                break;

            case KeyEvent.VK_A: // Caso pare de andar para esquerda
                if (megaMan.getSpeedX() < 0)
                    megaMan.stopRun();
                break;
        }
    }

    // Desenha todas as HitBoxes presentes na gameplay
    private void drawAllHitBox(Graphics2D g2d) {
        projectileManager.drawAllHitBox(g2d);
        gameEntityManager.drawAllHitBox(g2d);
        itemManager.drawAllHitBox(g2d);
        physicalMap.draw(g2d);
    }
}
