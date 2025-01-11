package com.gamestudio.elements;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ButtonMenu {
	protected int x;
	protected int y;
	protected boolean isEnabled;
	protected Image image;

	public ButtonMenu(int x, int y) {
		this.x = x;
		this.y = y;
		this.isEnabled = false;
		try {
			this.image = ImageIO.read(new File("Assets/Menu/seta.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public void update() {
		this.isEnabled = !isEnabled;
	}


	public void draw(Graphics g) {
		if(isEnabled) {
			g.drawImage(image, x, y, null);
		} else {
			g.setColor(Color.black);
			g.fillRect(x, y, 39, 48);
		}
	}
}
