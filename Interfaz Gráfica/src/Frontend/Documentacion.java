package Frontend;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import Backend.Parser;

public class Documentacion extends JFrame {
	public Documentacion(){
		setSize(500, 700);
	  	setVisible(true);
	  	setResizable(true);
	  	setTitle("Documentation");
	  	setIconImage((new ImageIcon("img//mac_love.png")).getImage());
	  	JTextPane pane = new JTextPane();
	  	pane.setEditable(false);
	  	
	  	pane.setContentType("text/html");
	  	Parser p = new Parser();
	  	pane.setText(p.fileToString("documentacion//documentacion.txt"));
	  	
	  	JScrollPane scroll = new JScrollPane(pane);
	  	add(scroll);
	}
}
