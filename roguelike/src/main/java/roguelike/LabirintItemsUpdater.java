package roguelike;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.Random;

import javax.swing.JPanel;

import roguelike.MainHeroBot.Weapon;

public class LabirintItemsUpdater implements Runnable {
	
	private Labirint lab;
	private JPanel panel;
	private int killedZombies = 0;
	private Random rand;
	
	public LabirintItemsUpdater(Labirint lab, JPanel panel) {
		this.lab = lab;
		this.panel = panel;
		rand = new Random();
	}

	@Override
	public void run() {
a:		while (true) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
			}
			
			//System.out.println("Updating labirint");
			for (WithCoordsAndSpeed ob : lab.listCoord) {
				if (!ob.getClass().isAssignableFrom(Brick.class)) {
					//System.out.println("updatind coords" + lab.mainBot.x + " " + lab.mainBot.y);
					int dx = ob.vx;
					int dy = ob.vy;
					
					if (ob.x + dx >= lab.width || ob.x + dx < 0) {
						dx = 0;
					}
					
					if (ob.y + dy >= lab.height || ob.y + dy < 0) {
						dy = 0;
					}
					
					for (WithCoordsAndSpeed ob2 : lab.listCoord) {
						if (ob.x + dx == ob2.x && ob.y == ob2.y && !ob2.isTransparent) {
							dx = 0;
						}
						
						if (ob.y + dy == ob2.y && ob.x == ob2.x && !ob2.isTransparent) {
							dy = 0;
						}
						
					}
					
					ob.x += dx;
					ob.vx = 0;
					ob.y += dy;
					ob.vy = 0;
				}
			}
			
			MainHeroBot mb = lab.mainBot;
			for (SourceOfLife src : lab.listSrc) {
				if (src.x == mb.x && src.y == mb.y) {
					mb.life += 10;
					if (mb.life > MainHeroBot.maxLife) {
						mb.life = MainHeroBot.maxLife;
					}
				}
			}
			
			for (ZombeeBot zb : lab.listZomb) {
				if (zb.life <= 0)
					continue;
				
				int dx = zb.x - mb.x;
				int dy = zb.y - mb.y;
				
				if (dx == 0 && Math.abs(dy) <= 1
						|| dy == 0 && Math.abs(dx) <= 1)
				{
					if (mb.weapon == Weapon.Knife && dx == mb.weaponX && dy == mb.weaponY) {
						zb.life -= ZombeeBot.decLife;
						if (zb.life <= 0) {
							zb.isTransparent = true;
							killedZombies++;
							if (killedZombies >= lab.listZomb.size()) {
								Graphics g = panel.getGraphics();
								g.setColor(Color.RED);
								g.setFont(new Font(Font.SERIF, Font.PLAIN,  50));
								g.drawString("VICTORY!", 10, 60);
								break a;
							}
						}
					}
					
					if (!(mb.weapon == Weapon.Shield && dx == mb.weaponX && dy == mb.weaponY)) {
						mb.life -= MainHeroBot.decLife;
						if (mb.life <= 0) {
							Graphics g = panel.getGraphics();
							g.setColor(Color.RED);
							g.setFont(new Font(Font.SERIF, Font.PLAIN,  50));
							g.drawString("GAME OVER", 10, 60);
							break a;
						}
					}
				}
			}
			
			for (ZombeeBot zb : lab.listZomb) {
				if (zb.life > 0) {
					zb.vx = getRandVel();
					zb.vy = getRandVel();
				}
			}
			
			panel.repaint();
		}
	}
	
	int getRandVel() {
		int p = rand.nextInt(100);
		if (p >= 90) {
			return 1;
		}
		
		if (p >= 10) {
			return 0;
		}
		
		return -1;
	}
	
}

