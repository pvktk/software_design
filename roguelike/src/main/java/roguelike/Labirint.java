package roguelike;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
 * Исключение, бросаемое если при загрузке лабиринта оказалось, что он некорректен.
 */
class InvalidLabirintException extends Exception {}

public class Labirint {
	private List<Drawable> listDraw = new ArrayList<>();
	private List<WithCoordsAndSpeed> listCoord = new ArrayList<>();
	private List<ZombeeBot> listZomb = new ArrayList<>();
	private List<SourceOfLife> listSrc = new ArrayList<>();
	
	private MainHeroBot mainBot;
	
	private int width = -1, height = -1;
	
	Labirint(String filename) throws IOException, InvalidLabirintException {
		BufferedReader br = new BufferedReader((new FileReader(filename)));
		
		int y = -1;
		
		
		while (br.ready()) {
			y++;
			String s = br.readLine();
			if (getWidth() == -1) {
				width = s.length();
			}
			if (s.length() != getWidth()) {
				RogueStarter.logger.error("different lenght of rows in labirint file");
				throw new InvalidLabirintException();
			}
			
			for (int x = 0; x < getWidth(); x++) {
				//Добавляем кирпичи
				if (s.charAt(x) == '#') {
					Brick b = new Brick(x, y);
					getListDraw().add(b);
					getListCoord().add(b);
				}
				
				//Добавляем главного бота
				if (s.charAt(x) == 'I') {
					if (getMainBot() != null) {
						RogueStarter.logger.error("More than one main bot in labirint");
						throw new InvalidLabirintException();
					}
					mainBot = new MainHeroBot(x, y);
					getListDraw().add(getMainBot());
					getListCoord().add(getMainBot());
				}
				
				//Зомби-боты
				if (s.charAt(x) == 'Z') {
					ZombeeBot zb = new ZombeeBot(x, y);
					getListDraw().add(zb);
					getListCoord().add(zb);
					getListZomb().add(zb);
				}
				
				//Источник жизни
				if (s.charAt(x) == 'S') {
					SourceOfLife sr = new SourceOfLife(x, y);
					getListDraw().add(sr);
					getListCoord().add(sr);
					getListSrc().add(sr);
				}
			}
		}
		
		height = y + 1;
		
	}

	public List<WithCoordsAndSpeed> getListCoord() {
		return listCoord;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public MainHeroBot getMainBot() {
		return mainBot;
	}

	public List<SourceOfLife> getListSrc() {
		return listSrc;
	}

	public List<ZombeeBot> getListZomb() {
		return listZomb;
	}

	public List<Drawable> getListDraw() {
		return listDraw;
	}
}
