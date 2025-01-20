package com.gamestudio.elements;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.gamestudio.effect.Animation;
import com.gamestudio.physical.PhysicalMap;
import com.gamestudio.state.GameState;

public abstract class Item extends GameEntity {
    private Animation itemaAnim;

    public Item(float x, float y, int width, int height, GameState gameState) {
        super(x, y, width, height, 0.1f, 1, gameState);
        setSpeedY(2);
        setSpeedX(0);
        setTeamType(ITEM_TEAM);
    }

    public Animation getItemaAnim() {
        return itemaAnim;
    }

    public void setItemaAnim(Animation itemaAnim) {
        this.itemaAnim = itemaAnim;
    }

    @Override
    public void update() {
        super.update();
        setPosY(getPosY() + getSpeedY());
        if(isCollisionWithLand()) {
            setSpeedY(0);
        }
    }

    private boolean isCollisionWithLand() {
        PhysicalMap physicalMap = getGameState().physicalMap;
        Rectangle rect = this.getBoundForCollisionWithMap();

        rect.y += getSpeedY() != 0 ? (int) getSpeedY() : 2;

        int posX1 = rect.x/physicalMap.getTileSize();
        posX1 -= 2;
        int posX2 = (rect.x + rect.width)/physicalMap.getTileSize();
        posX2 += 2;

        int posY = (rect.y + rect.height)/physicalMap.getTileSize();

        if(posX1 < 0) posX1 = 0;
        
        if(posX2 >= physicalMap.phys_map[0].length) posX2 = physicalMap.phys_map[0].length - 1;
        for(int y = posY; y < physicalMap.phys_map.length;y++){
            for(int x = posX1; x <= posX2; x++){
                Rectangle r = new Rectangle(
                    (int) physicalMap.getPosX() + x * physicalMap.getTileSize(), 
                    (int) physicalMap.getPosY() + y * physicalMap.getTileSize(), 
                    physicalMap.getTileSize(), 
                    physicalMap.getTileSize()
                );
                if(physicalMap.phys_map[y][x] == 1){
                    if(rect.intersects(r))
                        return true;
                } 
            }
        }
        return false;
    }

    @Override
    public abstract Rectangle getBoundForCollisionWithEnemy();

    @Override
    public abstract void draw(Graphics2D g2);
}
