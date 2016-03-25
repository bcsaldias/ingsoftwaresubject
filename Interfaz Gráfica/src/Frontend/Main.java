package Frontend;

import javax.swing.UIManager;

public class Main {

	/**
	 * Main del programa
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}
		InterfazUsuario pantalla = new InterfazUsuario();
		pantalla.setVisible(true);
		

	}

}
