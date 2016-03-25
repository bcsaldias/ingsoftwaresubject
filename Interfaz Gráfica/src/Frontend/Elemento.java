package Frontend;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import Backend.Association;
import Backend.Entity;

import java.awt.Graphics2D;

public class Elemento extends JFrame{
	private static final long serialVersionUID = 1L;
	
	public int ancho;
	public int alto;
	public int x;
	public int y;
	//public String path;
	public JLabel label;
	//public Graphics g;
	public Entity ent;
	public Association aso;
	public int x1;
	public int x2;
	public int y1;
	public int y2;
	public Visor vis;
	int t = 1;
	int p;
	JPopupMenu cp2;
	JMenuItem nom;
	int carpeta;
	String pathN;
	String pathR;
	Elemento yo;
	public Elemento(String pathN,String pathR,int w, int h, int x, int y, Entity e, final Visor vis, int p){
		this.vis = vis;
		
		this.pathN = "temp/Pestana"+vis.pestanaID+"/"+pathN;
		this.pathR = "temp/Pestana"+vis.pestanaID+"/"+pathR;
		this.ancho = w;
		this.alto = h;
		this.x = x;
		this.y = y;
		this.ent = e;
		this.p = p;
		setBounds(x, y, ancho, alto);
		setVisible(false);
		yo = this;
		if(p==-2){
			t = 0;
		}
		if(ent.type==4){
			cp2 = new JPopupMenu();
			cp2.setBackground(Color.red);
			nom = new JMenuItem("Delete");
			nom.setIcon(new ImageIcon("img/gtk_close.png"));
			
			nom.addActionListener(
		   	         new ActionListener() {
		   	            public void actionPerformed( ActionEvent evento )
		   	            {	
					    	System.out.print("koko");
					    	vis.dib.diagr.removeEntity(ent.id);
					    	label.setVisible(false);
		   	            }
		   	         });
			
			cp2.add(nom);
		}
		
		
	}
	
	
	
	
	public void crearDD(){
		label.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		    	if(ent.type ==4){
				    		if(e.getButton() == MouseEvent.BUTTON1){
		    				cp2.show(e.getComponent(), e.getX(), e.getY());
				    		}
				    	}
		    }
		    @Override
		    public void mousePressed(MouseEvent e) {
		        if (e.getButton() == MouseEvent.BUTTON3 && p==0) {
		        	
		        	try{
			            ImageIcon fot = new ImageIcon(vis.element.pathN);
			            int[] a = vis.transformar(vis.tamano, vis.element.ancho, vis.element.alto);
			    		ImageIcon icono = new ImageIcon(fot.getImage().getScaledInstance(a[0],a[1], Image.SCALE_AREA_AVERAGING));
			            fot.getImage().flush();
			            icono.getImage().flush();
			            vis.element.label.setIcon(icono);
		        	}catch(Exception ec){
		        		
		        	}
		        	
		            vis.mover = label;
		            vis.ent = ent;
		            vis.tamW = ancho;
		            vis.tamH = alto;
		            vis.element = yo;
		            //rojo
		            ImageIcon fot = new ImageIcon(pathR);
		            int[] a = vis.transformar(vis.tamano, ancho, alto);
		    		ImageIcon icono = new ImageIcon(fot.getImage().getScaledInstance(a[0],a[1], Image.SCALE_AREA_AVERAGING));
		            fot.getImage().flush();
		            icono.getImage().flush();
		            label.setIcon(icono);
		            
		        }else if (e.getButton() == MouseEvent.BUTTON3 && vis.mover!=null){
		        	
		            ImageIcon fot = new ImageIcon(vis.element.pathN);
		            int[] a = vis.transformar(vis.tamano, vis.element.ancho, vis.element.alto);
		    		ImageIcon icono = new ImageIcon(fot.getImage().getScaledInstance(a[0],a[1], Image.SCALE_AREA_AVERAGING));
		            fot.getImage().flush();
		            icono.getImage().flush();
		            vis.element.label.setIcon(icono);
		        	
		        	vis.mover = null;
		        	vis.ent = null;
		        	vis.element = null;

		        	//negro
		    	}else if(e.getButton() == MouseEvent.BUTTON3){
		    		if(p == -2){
		    		System.out.print("aff");
		    		vis.dib.dibujarLineas();
		    		}else
		    		{
		    			vis.BorrarLineas();
		    		}
		    	}
		    	else{
		    		System.out.println("jijiji");

		          	vis.mouseMoved(e);
		        }
		        
		    }
		    @Override
		    public void mouseReleased(MouseEvent e) {
		   	}
		    
		});
		
	   
	}
	private static int tt = 0;
	
	
	public void dibujarseCD(String Color){
		
		//dibujador.dibujar(path, ancho,alto);
		
		try {
			BufferedImage b = new BufferedImage(ancho,alto, BufferedImage.TYPE_INT_ARGB); 			
			Graphics g = b.createGraphics();
			paint(g,Color);
			g.dispose();

			try{
				
				String extra = "";
					if(Color.equals("Rojo")){
						extra="r";
						tt++;
						pathR = pathR.replace(".png","")+tt+extra+".png";
						ImageIO.write(b,"png",new File(pathR));
					}else{
						extra="n";
						tt++;
						pathN = pathR.replace(".png","")+tt+extra+".png";
						ImageIO.write(b,"png",new File(pathN));
					}
				
			
			}catch (Exception e) {}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	public void dibujarseE(String Color){
	
		//dibujador.dibujar(path, ancho,alto);		
		try {
			BufferedImage b = new BufferedImage(ancho,alto, BufferedImage.TYPE_INT_ARGB); 
			if(ent.type ==1 || ent.type == 2){
				
				String[] a = ent.text[0].split(" ");
				StringBuilder k = new StringBuilder();
				for(int i =0;i<a.length-1;i++){
					if(a[i].length()+a[i+1].length()<15){
						k.append(a[i]+" "+a[i+1]+"#");
						i++;
					}else{
						k.append(a[i]+" "+"#");
					}
				}
				k.append(a[a.length-1]);
				String[] u = k.toString().split("#");
				int pp = 150;
				for(String s:u){
					pp+=13;
				}	
				
				
				b = new BufferedImage(ancho,pp, BufferedImage.TYPE_INT_ARGB); 
			}
			
			Graphics g = b.createGraphics();
			paint(g,Color);
			g.dispose();

			String extra = "";
			if(Color.equals("Rojo")){
				extra="r";
				tt++;
				pathR = pathR.replace(".png","")+tt+extra+".png";
				ImageIO.write(b,"png",new File(pathR));
			}else{
				extra="n";
				tt++;
				pathN = pathR.replace(".png","")+tt+extra+".png";
				ImageIO.write(b,"png",new File(pathN));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void paint(Graphics g,String C){
		super.paint(g);	
	if(ent.text.length==3){
		if(ent.type==4)
		{
			if(C.equals("Rojo")){
				g.setColor(Color.red);
			}else{
				g.setColor(Color.black);
			}
			g.drawRect(0,0,(int)ent.width,(int)ent.height);
			String[] a = ent.text[0].split("\n");
			StringBuilder k = new StringBuilder();
			for(int i =0;i<a.length;i++){
				k.append(a[i]+"#");
			}
			String[] u = k.toString().split("#");
			int b = 20;
			for(String s:u){
				g.drawString(s, 10,b);
				b+=13;
			}	
		}
		else
		{
			if(C.equals("Rojo")){
				g.setColor(Color.red);
			}else{
				g.setColor(Color.black);
			}
			g.drawRect(0,0,(int)ent.width-1,(int)ent.height-1);
			g.drawString(ent.text[0], 10,20);
			g.drawLine(0, 25,(int)ent.width,25);
			String[] a = ent.text[1].split("\n");
			StringBuilder k = new StringBuilder();
			for(int i =0;i<a.length;i++){
				k.append(a[i]+"#");
			}
			String[] u = k.toString().split("#");
			int b = 40;
			for(String s:u){
				g.drawString(s, 10,b);
				b+=13;
			}	

			g.drawLine(0, b-8,(int)ent.width,b-8);

			a = ent.text[2].split("\n");
			k = new StringBuilder();
			for(int i =0;i<a.length;i++){
				k.append(a[i]+"#");
			}
			u = k.toString().split("#");
			b += 10;
			for(String s:u){
				g.drawString(s,10,b);
				b+=13;
			}	
		}
		
	}else{
		
		if(ent.type==1 || ent.type==2){	
			if(C.equals("Rojo")){
				g.setColor(Color.red);
			}else{
				g.setColor(Color.black);
			}
			ImageIcon fot = new ImageIcon("img/monito.png");
			g.drawImage(fot.getImage(), 0,0, null);
			fot.getImage().flush();
			String[] a = ent.text[0].split(" ");
			StringBuilder k = new StringBuilder();
			for(int i =0;i<a.length-1;i++){
				if(a[i].length()+a[i+1].length()<15){
					k.append(a[i]+" "+a[i+1]+"#");
					i++;
				}else{
					k.append(a[i]+" "+"#");
				}
			}
			k.append(a[a.length-1]);
			String[] u = k.toString().split("#");
			int b = 160;
			for(String s:u){
				g.drawString(s, 0,0+b);
				b+=13;
			}
			alto = b;

		}
		else if(ent.type==3){
			g.setColor (Color.white);
			g.fillOval(0,0,(int)ent.width,(int)ent.height);//, 20, 20);
			g.setColor (Color.DARK_GRAY);
			g.drawOval(0,0,(int)ent.width,(int)ent.height);//, 20, 20);
			if(C.equals("Rojo")){
				g.setColor(Color.red);
			}else{
				g.setColor(Color.black);
			}
			String[] a = ent.text[0].split(" ");
			StringBuilder k = new StringBuilder();
			for(int i =0;i<a.length-1;i++){
				if(a[i].length()+a[i+1].length()<15){
					k.append(a[i]+" "+a[i+1]+"#");
					i++;
				}else{
					k.append(a[i]+" "+"#");
				}
			}
			k.append(a[a.length-1]);
			String[] u = k.toString().split("#");
			int b = 30;
			for(String s:u){
				g.drawString(s, 0+35,0+b);
				b+=13;
			}	
		}else if(ent.type==4)
		{
			//g.setColor (Color.lightGray);
			//g.fillRect(0,0,(int)ent.width,(int)ent.height);//, 20, 20);
			if(C.equals("Rojo")){
				g.setColor(Color.red);
			}else{
				g.setColor(Color.black);
			}
			g.drawLine (0,0,0,(int)ent.height-1);
			g.drawLine (0,(int)ent.height-1,(int)ent.width-1,(int)ent.height-1);
			g.drawLine ((int)ent.width-1,(int)ent.height-1,(int)ent.width-1,11);
			g.drawLine ((int)ent.width-1,11,(int)ent.width-11,11);
			g.drawLine ((int)ent.width-11,11,(int)ent.width-11,0);
			g.drawLine ((int)ent.width-11,0,(int)ent.width-1,11);
			g.drawLine ((int)ent.width-11,0,0,0);
			String[] a = ent.text[0].split(" ");
			StringBuilder k = new StringBuilder();
			for(int i =0;i<a.length-1;i++){
				if(a[i].length()+a[i+1].length()<15){
					k.append(a[i]+" "+a[i+1]+"#");
					i++;
				}else{
					k.append(a[i]+" "+"#");
				}
			}
			k.append(a[a.length-1]);
			String[] u = k.toString().split("#");
			int max = 0;
			int b = 30;
			for(String s:u){
				g.drawString(s, 0+15 ,0+b);
				b+=13;
				if(s.length()>max){
					max = s.length();
				}
			}	
			//alto = b+20;
			//ancho = max*20+5;
			
			
		
	}}

/*	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		Visor.mover = label;
		Visor.AL = ancho;
		Visor.AN = alto;
		
	}*/

}


	public String dibujarseE2(String Color) {
		try {
			BufferedImage b = new BufferedImage(ancho,alto, BufferedImage.TYPE_INT_ARGB); 
			Graphics g = b.createGraphics();
			paint(g,Color);
			g.dispose();

			try{
				String extra = "";
				if(Color.equals("Rojo")){
					extra="r";
					tt++;
					pathR = pathR.replace(".png","")+tt+extra+".png";
					ImageIO.write(b,"png",new File(pathR));
					return pathR;
				}else{
					extra="n";
					tt++;
					pathN = pathR.replace(".png","")+tt+extra+".png";
					ImageIO.write(b,"png",new File(pathN));
					return pathN;
				}
				
			}catch (Exception e) {}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
}