package roguelike;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class InvalidLabirintException extends Exception {}

public class Labirint {
	List<Drawable> listDraw = new ArrayList<>();
	List<WithCoordsAndSpeed> listCoord = new ArrayList<>();
	List<ZombeeBot> listZomb = new ArrayList<>();
	List<SourceOfLife> listSrc = new ArrayList<>();
	
	MainHeroBot mainBot;
	
	int width = -1, height = -1;
	
	Labirint(String filename) throws IOException, InvalidLabirintException {
		BufferedReader br = new BufferedReader((new FileReader(filename)));
		
		int y = -1;
		
		
		while (br.ready()) {
			y++;
			String s = br.readLine();
			if (width == -1) {
				width = s.length();
			}
			if (s.length() != width) {
				throw new InvalidLabirintException();
			}
			
			for (int x = 0; x < width; x++) {
				if (s.charAt(x) == '#') {
					Brick b = new Brick(x, y);
					listDraw.add(b);
					listCoord.add(b);
				}
				
				if (s.charAt(x) == 'I') {
					mainBot = new MainHeroBot(x, y);
					listDraw.add(mainBot);
					listCoord.add(mainBot);
				}
				
				if (s.charAt(x) == 'Z') {
					ZombeeBot zb = new ZombeeBot(x, y);
					listDraw.add(zb);
					listCoord.add(zb);
					listZomb.add(zb);
				}
				
				if (s.charAt(x) == 'S') {
					SourceOfLife sr = new SourceOfLife(x, y);
					listDraw.add(sr);
					listCoord.add(sr);
					listSrc.add(sr);
				}
			}
		}
		
		height = y + 1;
		
	}
}
