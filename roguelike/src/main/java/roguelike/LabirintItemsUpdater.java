package roguelike;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.Random;

import javax.swing.JPanel;

import roguelike.MainHeroBot.Weapon;

/*
 * Класс, обеспечивающий взаимодействие между объектами в лабиринте и обновляющий их состояния
 */
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
		while (true) {
			int ret = IterateOnce();
			if (ret == -1) {
				break;
			}
		}
	}
	
	public int IterateOnce() {
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
		}
		
		/*
		 * Перебираем все объекты, кроме кирпичей
		 */
		for (WithCoordsAndSpeed ob : lab.getListCoord()) {
			if (!ob.getClass().isAssignableFrom(Brick.class)) {
				int dx = ob.getVx();
				int dy = ob.getVy();
				
				/*
				 * Проверяем выход за границы поля
				 */
				if (ob.getX() + dx >= lab.getWidth() || ob.getX() + dx < 0) {
					dx = 0;
				}
				
				if (ob.getY() + dy >= lab.getHeight() || ob.getY() + dy < 0) {
					dy = 0;
				}
				
				/*
				 * Проверяем, нет ли на пути другого твёрдого объекта. Если есть, то обнуляем перемещение.
				 */
				for (WithCoordsAndSpeed ob2 : lab.getListCoord()) {
					if (ob.getX() + dx == ob2.getX() && ob.getY() == ob2.getY() && !ob2.isTransparent) {
						dx = 0;
					}
					
					if (ob.getY() + dy == ob2.getY() && ob.getX() == ob2.getX() && !ob2.isTransparent) {
						dy = 0;
					}
					
				}
				
				ob.setX(ob.getX() + dx);
				ob.setVx(0);
				ob.setY(ob.getY() + dy);
				ob.setVy(0);
			}
		}
		/*
		 * Если наш бот пришел к источнику жизни, то подзаряжаем его.
		 */
		MainHeroBot mb = lab.getMainBot();
		for (SourceOfLife src : lab.getListSrc()) {
			if (src.getX() == mb.getX() && src.getY() == mb.getY()) {
				mb.setLife(mb.getLife() + 10);
				if (mb.getLife() > MainHeroBot.maxLife) {
					mb.setLife(MainHeroBot.maxLife);
				}
			}
		}
		
		/*
		 * Перебираем всех зомби-ботов и обеспечиваем взаимодействие с ними с учетом настроек оружия.
		 */
		for (ZombeeBot zb : lab.getListZomb()) {
			if (zb.getLife() <= 0)
				continue;
			
			int dx = zb.getX() - mb.getX();
			int dy = zb.getY() - mb.getY();
			
			if (dx == 0 && Math.abs(dy) <= 1
					|| dy == 0 && Math.abs(dx) <= 1)
			{
				if (mb.weapon == Weapon.Knife && dx == mb.getWeaponX() && dy == mb.getWeaponY()) {
					zb.setLife(zb.getLife() - ZombeeBot.decLife);
					if (zb.getLife() <= 0) {
						zb.isTransparent = true;
						killedZombies++;
						if (killedZombies >= lab.getListZomb().size()) {
							/*
							 * Если все зомби побеждены, то мы выиграли.
							 */
							Graphics g = panel.getGraphics();
							g.setColor(Color.RED);
							g.setFont(new Font(Font.SERIF, Font.PLAIN,  50));
							g.drawString("VICTORY!", 10, 60);
							RogueStarter.logger.info("User won");
							return -1;
						}
					}
				}
				
				if (!(mb.weapon == Weapon.Shield && dx == mb.getWeaponX() && dy == mb.getWeaponY())) {
					mb.setLife(mb.getLife() - MainHeroBot.decLife);
					if (mb.getLife() <= 0) {
						/*
						 * У нашего бота кончились жизни, мы проиграли
						 */
						Graphics g = panel.getGraphics();
						g.setColor(Color.RED);
						g.setFont(new Font(Font.SERIF, Font.PLAIN,  50));
						g.drawString("GAME OVER", 10, 60);
						RogueStarter.logger.info("User lost");
						return -1;
					}
				}
			}
		}
		
		/*
		 * Придаём зомби-ботам движение.
		 */
		for (ZombeeBot zb : lab.getListZomb()) {
			if (zb.getLife() > 0) {
				zb.setVx(getRandVel());
				zb.setVy(getRandVel());
			}
		}
		
		panel.repaint();
		
		return 0;
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

