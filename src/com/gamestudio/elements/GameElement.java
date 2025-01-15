package com.gamestudio.elements;
import com.gamestudio.state.GameState;

public abstract class GameElement {
    private int posX;
	private int posY;
	
	private GameState gameState;
	
	public GameElement(int x, int y, GameState gameState){
		this.posX = x;
		this.posY = y;
		this.gameState = gameState;
	}
	
	public void setPosX(int x){
		posX = x;
	}
	
	public int getPosX(){
		return posX;
	}
	
	public void setPosY(int y){
		posY = y;
	}
	
	public int getPosY(){
		return posY;
	}
	
	public GameState getGameState(){
		return gameState;
	}
	
	public abstract void update();
}
