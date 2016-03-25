package Frontend;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.*;

public class Pestana extends JButton {
	private static final long serialVersionUID = 1L;
	int state; //US-> state = 1; UC-> state=0
	Editor editor;
	Visor visor;
	int n;
	JPopupMenu cp;
	public int iD; 
	//empieza
	public JButton bClose;
	JPopupMenu cp2;
	JMenuItem nom;
	//termina
	public Pestana(int numero, int id, int s){
		super("D"+id);
		iD = id;
		editor = new Editor(s);
		editor.nombre="D"+id;
		state = s;
		visor = new Visor(new JLabel(), iD);
		editor.guardado = false;
		n = numero;
		
		
		
		
		 File theDir = new File("temp/Pestana"+iD);
		  if (!theDir.exists()) {
		    boolean result = false;
		    try{
		        theDir.mkdir();
		        result = true;
		     } catch(SecurityException se){
		     }        
		     if(result) {    
		       System.out.println("DIR created");  
		     }
		  }
		
		
		
		
		
		cp = new JPopupMenu();
		JMenuItem cerrar = new JMenuItem("Cerrar",new ImageIcon("img//close.png"));
		cp.add(cerrar);	
		JMenuItem cNombre = new JMenuItem("Renombrar",new ImageIcon("img//rename.png"));
		cp.add(cNombre);	
		
		//empieza
		
		bClose = new JButton(new ImageIcon("img//gtk_close.png"));
		
		bClose.setBorder(null);
		bClose.setBackground(Color.lightGray);
        
		
        cp2 = new JPopupMenu();
		nom = new JMenuItem(getText());
		cp2.add(nom);
		addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseEntered(MouseEvent e) {
		    	 cp2.show(e.getComponent(), e.getX(), e.getY());

		    }
		    @Override
		    public void mouseExited(MouseEvent e) {
		    	 cp2.setVisible(false);

		    }    
		});
		bClose.addActionListener(
	   	         new ActionListener() {
	   	            public void actionPerformed( ActionEvent evento )
	   	            {	
	   	            	setVisible(false);
	   	            	editor.setVisible(false);
	   	            	visor.setVisible(false);
	   	            	InterfazUsuario.eliminarPestana(iD);
	   	            }
	   	         });
		
		add(bClose);
        add(Box.createHorizontalGlue());
		//termina
		
		
		addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseReleased(MouseEvent e) {
		        if (e.getButton() == MouseEvent.BUTTON3) {
		            cp.show(e.getComponent(), e.getX(), e.getY());
		        }
		        if (e.getButton() == MouseEvent.BUTTON1) {
		        	editor.setVisible(true); 
		    		visor.setVisible(true);
		    		InterfazUsuario.cambiarPestana(iD);
		        }
		    }
		});
		
		cerrar.addActionListener(
	   	         new ActionListener() {
	   	            public void actionPerformed( ActionEvent evento )
	   	            {	
	   	            	setVisible(false);
	   	            	editor.setVisible(false);
	   	            	visor.setVisible(false);
	   	            	InterfazUsuario.eliminarPestana(iD);
	   	            }
	   	         });
		
		
		
		cNombre.addActionListener(
	   	         new ActionListener() {
	   	            public void actionPerformed( ActionEvent evento )
	   	            {	
	   	            	rename(0,"");
	   	            }
	   	         });
	}
	
	public void rename(int nu, String no){
		if(nu==0){
			String b = (String) JOptionPane.showInputDialog(null,"Write the new name:","Rename", JOptionPane.QUESTION_MESSAGE,new ImageIcon("rename.png"), null,getText());
			while(b.length()>20 || b.length()<1){
				if(b.length()<1){
					b = (String) JOptionPane.showInputDialog(null,"Name very short (min 1 char)\nWrite a new name","Rename", JOptionPane.QUESTION_MESSAGE,new ImageIcon("rename.png"), null,getText());
				}
				if(b.length()>20){
					b = (String) JOptionPane.showInputDialog(null,"Name very long (max 20 char)\nWrite a new name","Rename", JOptionPane.QUESTION_MESSAGE,new ImageIcon("rename.png"), null,getText());
				}
			}
			cp2 = new JPopupMenu();
			nom = new JMenuItem(b);
			cp2.add(nom);
			editor.nombre = b;
			setText(b);
		}else{
			   if(no.length()<=20){
				   setText(no);
			   }else{
				   setText(no.substring(0, 17)+"...");
			   }
				cp2 = new JPopupMenu();
				nom = new JMenuItem(no);
				cp2.add(nom);
				editor.nombre = no;
				editor.guardado = true;
		}
		

	}

}
