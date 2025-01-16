package com.gamestudio.state;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import com.gamestudio.elements.Camera;
import com.gamestudio.elements.MegaMan;
import com.gamestudio.interfaces.GameFrame;
import com.gamestudio.manager.DataLoader;
import com.gamestudio.manager.RobotManager;
import com.gamestudio.manager.StateManager;
import com.gamestudio.physical.PhysicalMap;

public class GameState extends State {
    public PhysicalMap physicalMap;
    public MegaMan megaMan;
    public RobotManager robotManager;
    public Camera camera;
    BufferedImage mapImage;

    public GameState(StateManager stateManager) {
       super(stateManager, new BufferedImage(GameFrame.width, GameFrame.height, BufferedImage.TYPE_INT_ARGB));
       this.physicalMap = new PhysicalMap(0, 0, this);
       this.mapImage = DataLoader.getInstance().getFrameImage("new_map").getImage();
       this.camera = new Camera(0, 0, 400, 240, this);
    }

    public void update() {}

    public void render() {
        Graphics g = getBufferedImage().getGraphics();
        drawMap(g);
        
    }
    
    public void setPressedButton(int code) {
    }
    
    private void drawMap(Graphics g) {
        // Escala para ajustar o conteúdo da câmera à tela
        float scaleX = (float) GameFrame.width / camera.getWidthView();
        float scaleY = (float) GameFrame.height / camera.getHeightView();

        // Salvar o estado original do Graphics
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform originalTransform = g2d.getTransform();

        // Aplicar a escala para desenhar proporcionalmente
        g2d.scale(scaleX, scaleY);
        
        g2d.drawImage(
            mapImage,
            -camera.getPosX(), // Ajuste horizontal
            -camera.getPosY(), // Ajuste vertical
            null
        );
    }

    public void setReleasedButton(int code) {}
}
