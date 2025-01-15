package com.gamestudio.elements;
import com.gamestudio.state.State;

public abstract class GameElement {
    private int posX;
	private int posY;
	
	private State state;
	
	public GameElement(int x, int y, State state){
		this.posX = x;
		this.posY = y;
		this.state = state;
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
	
	public State getState(){
		return state;
	}
	
	public abstract void update();
}
