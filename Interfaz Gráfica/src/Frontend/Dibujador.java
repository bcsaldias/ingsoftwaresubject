package Frontend;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AttributeSet.FontAttribute;

import Backend.Association;
import Backend.ClassDiagram;
import Backend.Diagram;
import Backend.DocXML;
import Backend.Entity;
import Backend.NodeXML;
import Backend.Parser;
import Backend.UseCaseDiagram;


public class Dibujador extends JFrame {

	private static JPanel contentPane;
	Diagram diagr;
	String path;
	Editor editor;
	int state =0;
	public int[] tamano;
	Visor vis;
	public ArrayList<Elemento> componentes;
	

	/**
	 * Launch the application.
	 */
	public String dibujar(String nombre, JLabel label){
		try {
			Parser p = new Parser();
			String s = editor.textArea.getText();
			boolean wd=p.wellDesigned(s);
			if(wd)
			{
				DocXML aux = p.getDocument(s);
				NodeXML root=aux.root;

				if(root.type.equals("UseCaseDiagram")){
					diagr=new UseCaseDiagram(root);

					//diagr.addComment("imnhjobjbjhbpx", "uc1");
					state = 0;
				}
				else if(root.type.equals("ClassDiagram")){
					diagr=new ClassDiagram(root);
					state = 1;
				}
				else return "";
				diagr.createDiagram();
			}
			
			
			tamano = new int[2];
			tamano[0] = getSize().width;
			tamano[1] = getSize().height;
			
			String[] pq = new String[0];
			Entity u = new Entity(0, 0, tamano[0],tamano[1],pq , 2);
			Elemento gr = new Elemento(nombre+".png","",tamano[0],tamano[1],0,0,u,vis,-2);
			componentes.add(gr);
			
			
			BufferedImage bi = new BufferedImage(getSize().width, getSize().height, BufferedImage.TYPE_INT_ARGB); 
			Graphics g = bi.createGraphics();
			paint(g);
			g.dispose();
			try{
				ImageIO.write(bi,"png",new File("temp/Pestana"+vis.pestanaID+"/"+nombre+".png"));
				return nombre+".png";
			}catch (Exception e) {}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";
	}
	

	/**
	 * Create the frame.
	 */
		public Dibujador(Diagram diag, Editor editor,Visor vis) {
			this.vis = vis;
			componentes = new ArrayList<Elemento>();
			this.editor = editor;
			this.diagr = diag;
			setBounds(0,0,1000,1000);
			setVisible(false);

		


	}

	public void dibujarUCD(Graphics g){
		
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(200, 10, (int)diagr.width-400, (int)diagr.height-10);
		g.setColor(Color.black);
		g.drawRect(200, 10, (int)diagr.width-400, (int)diagr.height-10);
		g.setColor (Color.black);
		{((Graphics2D) g).setFont(new Font("Arial", Font.PLAIN,25));
		g.drawString(diagr.name, 50+(int)(((int)diagr.width-400)/2), 50);
		}
	}

	public void dibujarCD(Graphics g){

		g.setColor (Color.black);
		{((Graphics2D) g).setFont(new Font("Arial", Font.PLAIN,25));
		g.drawString(diagr.name, 100+(int)(((int)diagr.width-400)/2), 50);
		}
			
	}
	
	public void dibujarLineas(){
		String s = editor.textArea.getText();
		

		Parser p = new Parser();
		s=p.deleteComment(s);
		DocXML aux = p.getDocument(s);
		NodeXML root=aux.root;
		if(root.type.equals("UseCaseDiagram")){
			dibujarLineasUC();
		}
		else if(root.type.equals("ClassDiagram")){
			dibujarLineasCD();
		}

	}
	
	private void dibujarLineasCD(){
		tamano = new int[2];
		tamano[0] = getSize().width;
		tamano[1] = getSize().height;
		
		//calcular Lineas
		diagr.createConnections();
		
		BufferedImage bi = new BufferedImage(getSize().width, getSize().height, BufferedImage.TYPE_INT_ARGB); 
		Graphics g = bi.createGraphics();

		dibujarLCD(g);
		g.dispose();

		try{
			ImageIO.write(bi,"png",new File("temp/Pestana"+vis.pestanaID+"/lineas.png"));
		}catch (Exception e) {}
		
		String[] pq = new String[0];
		Entity u = new Entity(0, 0, tamano[0],tamano[1],pq , 2);
		Elemento gr = new Elemento("lineas.png","",tamano[0],tamano[1],0,0,u,vis,1);
		componentes.add(gr);
		
		vis.ponerObjeto("temp/Pestana"+vis.pestanaID+"/lineas.png", tamano, tamano[0],tamano[1],0,0, gr,2);
	}

	private void dibujarLineasUC(){
		tamano = new int[2];
		tamano[0] = getSize().width;
		tamano[1] = getSize().height;
		
		//calcular Lineas
		diagr.createConnections();
		
		BufferedImage bi = new BufferedImage(getSize().width, getSize().height, BufferedImage.TYPE_INT_ARGB); 
		Graphics g = bi.createGraphics();
	
		
		dibujarLUC(g);
		g.dispose();

		try{
			ImageIO.write(bi,"png",new File("temp/Pestana"+vis.pestanaID+"/lineas.png"));
		}catch (Exception e) {}
		
		String[] pq = new String[0];
		Entity u = new Entity(0, 0, tamano[0],tamano[1],pq , 2);
		Elemento gr = new Elemento("lineas.png","",tamano[0],tamano[1],0,0,u,vis,1);
		componentes.add(gr);
		
		vis.ponerObjeto("temp/Pestana"+vis.pestanaID+"/lineas.png", tamano, tamano[0],tamano[1],0,0, gr,2);
		
	}
	
	
	public void exportar(Graphics g){
		super.paint(g);
		
		for(int i = 0; i<componentes.size();i++){
			Elemento c = componentes.get(i);
			
			
			ImageIcon fot = new ImageIcon(c.pathN);
			g.drawImage(fot.getImage(),(int)c.ent.x,(int)c.ent.y, null);
			fot.getImage().flush();
		
			
		}
	}
	
	public void dibujarLCD(Graphics g){
		super.paint(g);
		for(Association aso: diagr.associations)
		{
		int[][] puntos = aso.calculateArrow(25);
		g.setColor (Color.black);
		if(aso.type.equals("association")){
			g.drawLine ((int)aso.points[0][0],(int)aso.points[0][1],(int)aso.points[1][0],(int)aso.points[1][1]);

		}
		else if(aso.type.equals("dependency")){
			//g.setColor (Color.red);

			double m = (((int)aso.points[1][1])-((int)aso.points[0][1]));
			double m2 = (((int)aso.points[1][0])-((int)aso.points[0][0]));
			double m3 = m/m2;
			drawDashedLine(g, (int)aso.points[0][0], (int)aso.points[0][1], (int)aso.points[1][0], (int)aso.points[1][1],1);

			g.drawLine (puntos[0][0],puntos[1][0],puntos[0][1],puntos[1][1]);
			g.drawLine (puntos[0][0],puntos[1][0],puntos[0][3],puntos[1][3]);

		}else if(aso.type.equals("inheritance")){
			//	g.drawLine (puntos[0][0],puntos[1][0],puntos[0][1],puntos[1][1]);
			//g.drawLine (puntos[0][0],puntos[1][0],puntos[0][3],puntos[1][3]);
			//g.drawLine (puntos[0][1],puntos[1][1],puntos[0][3],puntos[1][3]);
			g.setColor (Color.black);
			g.drawLine ((int)aso.points[0][0],(int)aso.points[0][1],(int)aso.points[1][0],(int)aso.points[1][1]);

			int[] pX = {puntos[0][0],puntos[0][1],puntos[0][3]};
			int[] pY = {puntos[1][0],puntos[1][1],puntos[1][3]};
			g.setColor (Color.white);
			g.fillPolygon(pX,pY,3);
			g.setColor (Color.black);
			g.drawLine (puntos[0][0],puntos[1][0],puntos[0][1],puntos[1][1]);
			g.drawLine (puntos[0][0],puntos[1][0],puntos[0][3],puntos[1][3]);
			g.drawLine (puntos[0][1],puntos[1][1],puntos[0][3],puntos[1][3]);

		}else if(aso.type.equals("composition")){
			g.setColor (Color.black);
			int[] pX = {puntos[0][0],puntos[0][1],puntos[0][2],puntos[0][3]};
			int[] pY = {puntos[1][0],puntos[1][1],puntos[1][2],puntos[1][3]};
			g.fillPolygon(pX,pY,4);
			g.drawLine ((int)aso.points[0][0],(int)aso.points[0][1],(int)aso.points[1][0],(int)aso.points[1][1]);
		}
		else if(aso.type.equals("aggregation"))
		{
			int[] pX = {puntos[0][0],puntos[0][1],puntos[0][2],puntos[0][3]};
			int[] pY = {puntos[1][0],puntos[1][1],puntos[1][2],puntos[1][3]};
			g.setColor (Color.white);
			g.fillPolygon(pX,pY,4);
			g.setColor (Color.black);
			g.drawLine (puntos[0][0],puntos[1][0],puntos[0][1],puntos[1][1]);
			g.drawLine (puntos[0][0],puntos[1][0],puntos[0][3],puntos[1][3]);
			g.drawLine (puntos[0][3],puntos[1][3],puntos[0][2],puntos[1][2]);
			g.drawLine (puntos[0][1],puntos[1][1],puntos[0][2],puntos[1][2]);
			g.drawLine ((int)aso.points[0][0],(int)aso.points[0][1],puntos[0][2],puntos[1][2]);

		}
		else if(aso.type.equals("comment"))
		{
			double m = (((int)aso.points[1][1])-((int)aso.points[0][1]));
			double m2 = (((int)aso.points[1][0])-((int)aso.points[0][0]));
			double m3 = m/m2;

			drawDashedLine(g, (int)aso.points[0][0], (int)aso.points[0][1], (int)aso.points[1][0], (int)aso.points[1][1],2);

		}


	}


		
	}
	
	public void dibujarLUC(Graphics g){
		super.paint(g);
		for(Association aso: diagr.associations)
		{

			((Graphics2D) g).setStroke(new BasicStroke( 1.0f ));
			System.out.println(aso.type);
			if(aso.type.equals("basic")){
				g.setColor (Color.cyan);
				g.drawLine ((int)aso.points[0][0],(int)aso.points[0][1],(int)aso.points[1][0],(int)aso.points[1][1]);

			}else if(aso.type.equals("extend")){
				g.setColor (Color.cyan);
				((Graphics2D) g).setStroke(new BasicStroke( 3.0f ));
				double m = (((int)aso.points[1][1])-((int)aso.points[0][1]));
				double m2 = (((int)aso.points[1][0])-((int)aso.points[0][0]));
				double m3 = m/m2;

				drawDashedLine(g, (int)aso.points[0][0], (int)aso.points[0][1], (int)aso.points[1][0], (int)aso.points[1][1],4);
				int[][] puntos = aso.calculateArrow(25);
				g.drawLine (puntos[0][0],puntos[1][0],puntos[0][1],puntos[1][1]);
					g.drawLine (puntos[0][0],puntos[1][0],puntos[0][3],puntos[1][3]);
				g.setColor (Color.blue);
				g.drawString("<<extend>>", (int)aso.points[0][0]+(int)(m2/2+10),(int)aso.points[0][1]+(int)(m/2));

			}else if(aso.type.equals("include")){
				g.setColor (Color.cyan);
				double m = (((int)aso.points[1][1])-((int)aso.points[0][1]));
				double m2 = (((int)aso.points[1][0])-((int)aso.points[0][0]));
				double m3 = m/m2;
				int[][] puntos = aso.calculateArrow(25);
				g.drawLine (puntos[0][0],puntos[1][0],puntos[0][1],puntos[1][1]);
				g.drawLine (puntos[0][0],puntos[1][0],puntos[0][3],puntos[1][3]);
				
				drawDashedLine(g, (int)aso.points[0][0], (int)aso.points[0][1], (int)aso.points[1][0], (int)aso.points[1][1],1);
				g.setColor (Color.blue);
				g.drawString("<<include>>", (int)aso.points[0][0]+(int)(m2/2+10),(int)aso.points[0][1]+(int)(m/2));
			}else if(aso.type.equals("isa")){
				g.setColor (Color.cyan);
				g.drawLine ((int)aso.points[0][0],(int)aso.points[0][1],(int)aso.points[1][0],(int)aso.points[1][1]);
				
				int[][] puntos = aso.calculateArrow(25);
				int[] pX = {puntos[0][0],puntos[0][1],puntos[0][3]};
				int[] pY = {puntos[1][0],puntos[1][1],puntos[1][3]};
				g.setColor (Color.white);
				g.fillPolygon(pX,pY,3);
				g.setColor (Color.cyan);
				g.drawLine (puntos[0][0],puntos[1][0],puntos[0][1],puntos[1][1]);
				g.drawLine (puntos[0][0],puntos[1][0],puntos[0][3],puntos[1][3]);
				g.drawLine (puntos[0][1],puntos[1][1],puntos[0][3],puntos[1][3]);
			}else if(aso.type.equals("comment"))
			{
				g.setColor (Color.black);
				double m = (((int)aso.points[1][1])-((int)aso.points[0][1]));
				double m2 = (((int)aso.points[1][0])-((int)aso.points[0][0]));
				double m3 = m/m2;

				drawDashedLine(g, (int)aso.points[0][0], (int)aso.points[0][1], (int)aso.points[1][0], (int)aso.points[1][1],2);

			}

		}
	}
	
	public void paint (Graphics g)
	{
		super.paint(g);

		if(state ==0){
			dibujarUCD(g);
			for(String key : diagr.entities.keySet()) 
			{
				Entity ent = diagr.entities.get(key);
				Elemento gr = new Elemento("upsN.png","upsR.png",(int)ent.width,(int)ent.height,(int)ent.x,(int)ent.y, ent,vis,0);
				componentes.add(gr);
				gr.dibujarseE("Rojo");
				gr.dibujarseE("Negro");
			}
		
			
			
			
		}else{
			dibujarCD(g);
			for(String key : diagr.entities.keySet()) 
			{
				Entity ent = diagr.entities.get(key);
				Elemento gr = new Elemento("upsN.png","upsR.png",(int)ent.width,(int)ent.height,(int)ent.x,(int)ent.y, ent,vis,0);
				componentes.add(gr);
				gr.dibujarseCD("Rojo");
				gr.dibujarseCD("Negro");
			}
		
		}
	}
	public void drawDashedLine(Graphics g, int x1, int y1, int x2, int y2, int grosor){
        Graphics2D g2d = (Graphics2D) g;
        //float dash[] = {10.0f};
        Stroke dashed = new BasicStroke(grosor, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
        g2d.setStroke(dashed);
        g2d.drawLine(x1, y1, x2, y2);
    }
}