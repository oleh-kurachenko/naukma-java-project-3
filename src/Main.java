import javax.swing.UIManager;

import jcomponents.JWindow;

/**
 * 
 * @author Oleh Kurachenko
 *
 */
public class Main {
	
	
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		JWindow window = new JWindow("File Analyzer");
		window.setVisible(true);
	}
}
