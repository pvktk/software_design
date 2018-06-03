package roguelike;

import java.awt.Color;
import java.awt.Graphics;

/*
 * Кирпич, из которого строятся стены лабиринта
 */
class Brick extends WithCoordsAndSpeed implements Drawable {
	
	public Brick(int x, int y) {
		super(x, y);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void drawSelf(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(getX()*RogueStarter.boxW, getY()*RogueStarter.boxH, RogueStarter.boxW, RogueStarter.boxH);
	}
	
}
