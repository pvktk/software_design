package roguelike;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class WithCoordsAndSpeed {
	int x, y, vx, vy;
	boolean isTransparent = false;
	public WithCoordsAndSpeed(int x, int y) {
		this.x = x;
		this.y = y;
	}
}

class Brick extends WithCoordsAndSpeed implements Drawable {
	
	public Brick(int x, int y) {
		super(x, y);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void drawSelf(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(x*RogueStarter.boxW, y*RogueStarter.boxH, RogueStarter.boxW, RogueStarter.boxH);
	}
	
}

class SourceOfLife extends Brick {

	public SourceOfLife(int x, int y) {
		super(x, y);
		isTransparent = true;
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void drawSelf(Graphics g) {
		g.setColor(Color.GREEN);
		g.fillRect(x*RogueStarter.boxW, y*RogueStarter.boxH, RogueStarter.boxW, RogueStarter.boxH);
	}
}

class Bot extends WithCoordsAndSpeed {
	public Bot(int x, int y) {
		super(x, y);

	}

	int life;
}

class MainHeroBot extends Bot implements KeyListener, Drawable{
	
	int weaponX, weaponY;
	final static int weaponWidth = 5, maxLife = 800, decLife = 10;
	
	enum Weapon {Shield, Knife};
	
	Weapon weapon = Weapon.Shield;
	
	MainHeroBot(int x, int y) {
		super(x, y);
		life = maxLife;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void drawSelf(Graphics g) {
		g.setColor(Color.RED);
		g.fillRect(x*RogueStarter.boxW, y*RogueStarter.boxH, RogueStarter.boxW, RogueStarter.boxH);
		
		g.setColor(weapon == Weapon.Shield ? Color.GREEN : Color.BLACK);
		
		g.fillRect(x*RogueStarter.boxW + (RogueStarter.boxW - weaponWidth) / 2 + 5*weaponX,
				y*RogueStarter.boxH + (RogueStarter.boxH - weaponWidth) / 2 + 5*weaponY, 5, 5);
		
		g.setColor(Color.BLACK);
		g.drawString(life + "", x*RogueStarter.boxW, (y + 1)*RogueStarter.boxH);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			vy = -1;
		}
		
		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			vy = 1;
		}
		
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			vx = -1;
		}
		
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			vx = 1;
		}
		
		if (e.getKeyChar() == 'h') {
			weapon = Weapon.Shield;
		}
		
		if (e.getKeyChar() == 'k') {
			weapon = Weapon.Knife;
		}
		
		if (e.getKeyChar() == 'w') {
			weaponX = 0;
			weaponY = -1;
		}
		
		if (e.getKeyChar() == 's') {
			weaponX = 0;
			weaponY = 1;
		}
		
		if (e.getKeyChar() == 'a') {
			weaponX = -1;
			weaponY = 0;
		}
		
		if (e.getKeyChar() == 'd') {
			weaponX = 1;
			weaponY = 0;
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}

class ZombeeBot extends Bot implements Drawable{
	final static int decLife = 10;
	ZombeeBot(int x, int y) {
		super(x, y);
		life = 800;
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void drawSelf(Graphics g) {
		g.setColor(life > 0 ? Color.BLUE : Color.GRAY);
		g.fillRect(x*RogueStarter.boxW, y*RogueStarter.boxH, RogueStarter.boxW, RogueStarter.boxH);
		
		g.setColor(Color.BLACK);
		g.drawString(life + "", x*RogueStarter.boxW, (y + 1)*RogueStarter.boxH);
	}
	
}
