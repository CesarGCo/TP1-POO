package com.gamestudio.physical;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import com.gamestudio.state.GameState;
import com.gamestudio.elements.Camera;
import com.gamestudio.elements.GameElement;
import com.gamestudio.elements.SmartGameEntity;
import com.gamestudio.elements.DumbGameEntity;
import com.gamestudio.manager.DataLoader;

public class PhysicalMap extends GameElement {

    public int[][] phys_map; // Matriz de colisão
    private int tileSize; // Tamanho de cada ladrilho no jogo
    
    public PhysicalMap(int x, int y, GameState gameState) {
        super(x, y, gameState);
        this.tileSize = 16;

        phys_map = DataLoader.getInstance().getPhysicalMap();
    }
    
    public int getTileSize(){
        return tileSize;
    }
    
    @Override
    public void update() {}

    // Verifica se o objeto passado por parâmetro teve colisão no topo com o mapa
    public Rectangle haveCollisionWithTop(Rectangle rect){

        int posX1 = rect.x/tileSize;
        posX1 -= 2;
        int posX2 = (rect.x + rect.width)/tileSize;
        posX2 += 2;

        int posY = rect.y/tileSize;

        if(posX1 < 0) posX1 = 0;
        
        if(posX2 >= phys_map[0].length) posX2 = phys_map[0].length - 1;
        
        for(int y = posY; y >= 0; y--){
            for(int x = posX1; x <= posX2; x++){
                
                if(phys_map[y][x] == 1){
                    Rectangle r = new Rectangle((int) getPosX() + x * tileSize, (int) getPosY() + y * tileSize, tileSize, tileSize);
                    if(rect.intersects(r))
                        return r;
                }
            }
        }
        return null;
    }
    
    // Verifica se o objeto passado por parâmetro teve colisão na parte inferior com o mapa
    public Rectangle haveCollisionWithLand(Rectangle rect, SmartGameEntity smartGameEntity){

        int posX1 = rect.x/tileSize;
        posX1 -= 2;
        int posX2 = (rect.x + rect.width)/tileSize;
        posX2 += 2;

        int posY = (rect.y + rect.height)/tileSize;

        if(posX1 < 0) posX1 = 0;
        
        if(posX2 >= phys_map[0].length) posX2 = phys_map[0].length - 1;
        for(int y = posY; y < phys_map.length;y++){
            for(int x = posX1; x <= posX2; x++){
                Rectangle r = new Rectangle((int) getPosX() + x * tileSize, (int) getPosY() + y * tileSize, tileSize, tileSize);
                if(phys_map[y][x] == 1){
                    if(rect.intersects(r))
                        return r;
                } else if(phys_map[y][x] == 2) {
                    if(rect.intersects(r))
                        smartGameEntity.setCurrentState(SmartGameEntity.DEATH);
                }
            }
        }
        return null;
    }

    //////////////////
    public Rectangle haveCollisionWithLandForDumbGameEntity(Rectangle rect, DumbGameEntity dumbGameEntity) {
        int posX1 = rect.x / tileSize - 2;
        int posX2 = (rect.x + rect.width) / tileSize + 2;

        int posY = (rect.y + rect.height) / tileSize;

        if (posX1 < 0) posX1 = 0;
        if (posX2 >= phys_map[0].length) posX2 = phys_map[0].length - 1;

        for (int y = posY; y < phys_map.length; y++) {
            for (int x = posX1; x <= posX2; x++) {
                Rectangle tileRect = new Rectangle(
                        (int) getPosX() + x * tileSize,
                        (int) getPosY() + y * tileSize,
                        tileSize,
                        tileSize
                );

                if (phys_map[y][x] == 1) {
                    if (rect.intersects(tileRect)) {
                        return tileRect;
                    }
                }
            }
        }
        return null;
    }
    
    // Verifica se o objeto passado por parâmetro teve colisão na direita com o mapa
    public Rectangle haveCollisionWithRightWall(Rectangle rect){
        
        int posY1 = rect.y/tileSize;
        posY1-=2;
        int posY2 = (rect.y + rect.height)/tileSize;
        posY2+=2;
        
        int posX1 = (rect.x + rect.width)/tileSize;
        int posX2 = posX1 + 3;
        if(posX2 >= phys_map[0].length) posX2 = phys_map[0].length - 1;
        
        if(posY1 < 0) posY1 = 0;
        if(posY2 >= phys_map.length) posY2 = phys_map.length - 1;
        
        
        for(int x = posX1; x <= posX2; x++){
            for(int y = posY1; y <= posY2;y++){
                if(phys_map[y][x] == 1){
                    Rectangle r = new Rectangle((int) getPosX() + x * tileSize, (int) getPosY() + y * tileSize, tileSize, tileSize);
                    if(r.y < rect.y + rect.height - 1 && rect.intersects(r))
                        return r;
                }
            }
        }
        return null;
        
    }
    
    // Verifica se o objeto passado por parâmetro teve colisão na esquerda com o mapa
    public Rectangle haveCollisionWithLeftWall(Rectangle rect){
        int posY1 = rect.y/tileSize;
        posY1-=2;
        int posY2 = (rect.y + rect.height)/tileSize;
        posY2+=2;
        
        int posX1 = (rect.x + rect.width)/tileSize;
        int posX2 = posX1 - 3;
        if(posX2 < 0) posX2 = 0;
        
        if(posY1 < 0) posY1 = 0;
        if(posY2 >= phys_map.length) posY2 = phys_map.length - 1;
        
        
        for(int x = posX1; x >= posX2; x--){
            for(int y = posY1; y <= posY2;y++){
                if(phys_map[y][x] == 1){
                    Rectangle r = new Rectangle((int) getPosX() + x * tileSize, (int) getPosY() + y * tileSize, tileSize, tileSize);
                    if(r.y < rect.y + rect.height - 1 && rect.intersects(r))
                        return r;
                }
            }
        }
        return null;
        
    }
    

    // Desenha a física do mapa na tela, onde se houve colisão será exibido como um quadrado cinza
    public void draw(Graphics g2){
        
        Camera camera = getGameState().camera;
        
        g2.setColor(Color.GRAY);
        for(int i = 0;i< phys_map.length;i++)
            for(int j = 0;j<phys_map[0].length;j++)
                if(phys_map[i][j]!=0) g2.fillRect((int) (getPosX() - camera.getPosX() + j*tileSize), 
                        (int) (getPosY() - camera.getPosY() + i*tileSize), tileSize, tileSize);
        
    }
    
}