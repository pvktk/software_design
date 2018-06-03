package roguelike;

import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Hello world!
 *
 */


public class RogueStarter {
	public static final int boxH = 20, boxW = 20;
	
	private static Labirint labirint;
	
	public static final Logger logger = LogManager.getLogger("RoguelikeLogger");
	
	public static void main( String[] args ) throws IOException, InvalidLabirintException {
		
		logger.info("Game started");
		
		labirint = new Labirint("labirint1");
		JPanel panel = new MyPanel(labirint);
    	
		LabirintItemsUpdater labUpd = new LabirintItemsUpdater(labirint, panel);

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				createAndShowGUI(panel);
			}
    		
		});

		new Thread(labUpd).start();
	}

	private static void createAndShowGUI(JPanel panel) {
		JFrame frame = new JFrame("Roguelike");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.add(panel);
		frame.pack();
		frame.setVisible(true);
	}
}
