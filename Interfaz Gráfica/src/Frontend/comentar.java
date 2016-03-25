package Frontend;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

import Backend.Entity;

public class comentar extends JFrame{
	private static final long serialVersionUID = 1L;
	
	Visor visor;
	
	public comentar(final Visor visor){
		this.visor = visor;
		
		setSize(400, 100);
		setVisible(true);
		setResizable(true);
		setTitle("Add Comment");
		setIconImage((new ImageIcon("img//cmm.png")).getImage());
		setLayout(new FlowLayout());
		final JTextField field = new JTextField("Comentario",20);
		final JTextField field2 = new JTextField("ID",4);
		
		
		JButton but = new JButton("Add Comment");

		but.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {

				setVisible(false);
				if(visor.dib !=null && visor.dib.diagr!=null){
					String st = visor.dib.diagr.addComment(field.getText(), field2.getText());
					if(!st.equals(""))
					{
						Entity ent = visor.dib.diagr.entities.get(st);
						Elemento gr = new Elemento("EN"+field2.getText()+".png","ER"+field2.getText()+".png",(int)ent.width,(int)ent.height,(int)ent.x,(int)ent.y, ent,visor,0);
						System.out.println(field2.getText());
						visor.dib.componentes.add(gr);
						gr.dibujarseE2("Rojo");
						String s = gr.dibujarseE2("Negro");
						visor.BorrarLineas();
						visor.ponerObjeto(s, visor.tamano, (int)ent.width,(int)ent.height,(int)ent.x,(int)ent.y, gr, 0);
						visor.dib.dibujarLineas();
					}
				}
			}
		});        

		but.setSize(100,100);
		
		add(field);
		add(field2);
		add(but);

		}
		
		
	}
	

