package Frontend;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Label;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import Backend.Diagram;
import Backend.Entity;

public class Visor extends JScrollPane implements MouseMotionListener{
	private static final long serialVersionUID = 1L;
	public Object mover;
	public int tamW;
	public int tamH;
	public Elemento element;
	private int mouseX = 200;
	private int mouseY = 200;
	
	private int ancho = 0;
	private int alto = 0;
	
	public int pestanaID;
	
	public JLabel label;
	public Dibujador dib;
	public Entity ent;
	//public String path;
	
	/**
	 * Constructor de Visor, hereda de JScrollPane
	 * @param JLabel u
	 */
	public Visor(JLabel u, int p){
		super(u);
		setVerticalScrollBarPolicy (ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);	
		label = u;
		   label.addMouseMotionListener(this);
		pestanaID = p;
		
		

	}


	public void dibujarDiagrama(Diagram diagr, Editor editor, String nombre){
		dib = new Dibujador(diagr, editor, this);
		String s = dib.dibujar(nombre, label);
		cambiarImagen(s);
		//label.setIcon(new ImageIcon(s));	
	}
	int[] tamano ;
	/**
	 * Este método cambia la imagen de acuerdo al 
	 * diagrama que se quiere generarl, desde el XML.
	 * 
	 * @param String path
	 */
	public void cambiarImagen(String path){
	/*	//label = new JLabel();
		//label.setIcon(new ImageIcon("img//paste.png"));
		
		this.path = path;
		//ImageIcon im = new ImageIcon(path);
		 ImageIcon fot = new ImageIcon(path);
         ImageIcon icono = new ImageIcon(fot.getImage().getScaledInstance(label.getWidth(), label.getHeight(), Image.SCALE_AREA_AVERAGING));
         fot.getImage().flush();
         icono.getImage().flush();
         label.setIcon(icono);

		//im.getImage().flush();
		//label.setIcon(im);	*/	
		tamano = dib.tamano;
		
		for(int i = dib.componentes.size()-1; i>=0;i--){
			Elemento c = dib.componentes.get(i);
			ponerObjeto(c.pathN,tamano,c.ancho,c.alto,c.x,c.y, c,0);
			
		}
	}
	
	public int[] fondo = new int[2];
	
    public int[] des_transformar(int[] tamano, int anch, int alt, Entity ent){
    	int cienW = label.getWidth();
		int cienH = label.getHeight();
		System.out.println("Belencita!");
		int otroW = tamano[0];
		int otroH = tamano[1];
		int finalw = (int)((float)(anch/cienW)*(otroW));
		int finalh = (int)((float)(alt/cienH)*(otroH));
		
		if(finalw == 0 || finalh ==0){
			finalw = (int)((float)(anch*otroW)/cienW);
			finalh = (int)((float)(alt*otroH)/cienH);
		}
		System.out.println("finalw " + finalw + " finalh: " + finalh + "ancho: " + anch + "alto: " + alt + " BB");
		
		if(ent !=null){
			ent.x = finalw;
			ent.y = finalh;
		}		
		int[] ret = new int[2];
		ret[0]=finalw;
		ret[1] =finalh;
		return ret;
    	
    }
	
	public int[] transformar(int[] tamano, int anch, int alt){
		int otroW = label.getWidth();
		int otroH = label.getHeight();
		//System.out.print("otros: " + otroW+ " "+ otroH);
		int cienW = tamano[0];
		int cienH = tamano[1];
		
		int finalw = (int)((float)(anch/cienW)*(otroW));
		int finalh = (int)((float)(alt/cienH)*(otroH));
		
		if(finalw == 0){
			finalw = (int)((float)(anch*otroW)/cienW);
			finalh = (int)((float)(alt*otroH)/cienH);
		}
		
		int[] ret = new int[2];
		ret[0]=finalw;
		ret[1] =finalh;
		return ret;
	}
	

	public void ponerObjeto(String path, int[] tamano, int anch,int alt, int xm, int ym, Elemento c, int ord){

		//this.path = path;

        int[] t = transformar( tamano,anch, alt);
        ancho = t[0];
        alto = t[1];
        
        t = transformar( tamano,xm, ym);
        int x = t[0];
        int y = t[1];

        System.out.print("x: "+xm+" y: "+ ym + " ancho: "+ anch+" alto: "+ alt + " ");
        System.out.print("x: "+x+" y: "+ y + " ancho: "+ ancho+" alto: "+ alto + " ");
        
		ImageIcon fot = new ImageIcon(path);
		ImageIcon icono = new ImageIcon(fot.getImage().getScaledInstance(ancho,alto, Image.SCALE_AREA_AVERAGING));
        fot.getImage().flush();
        icono.getImage().flush();
        System.out.print(path+"\n");
        
        
        c.label =   new JLabel(icono);
        c.crearDD();
        if(c.p!=-2){
        	label.add(c.label,2,0);
        }else if(c.p ==0){
        	label.add(c.label,1,0);}
        else{
        	label.add(c.label);
        }

        ((Component) c.label).setBounds(x, y, ancho,alto);
       // mover = c.label;
/*
	    c.label.*/
	
	}
	
	public void guardarFoto(Editor editor,JFileChooser file, String nombre){

		int[] f = des_transformar(tamano, getSize().width, getSize().height, null);
		BufferedImage bi = new BufferedImage(f[0],f[1], BufferedImage.TYPE_INT_ARGB); 
		Graphics g = bi.createGraphics();
		dib.exportar(g);
		g.dispose();

		try{
			ImageIO.write(bi,"png",new File(file.getSelectedFile()+"/"+nombre+".png"));
		}catch (Exception e) {}
		
		
		
		//dib.dibujar(file.getSelectedFile()+"/"+nombre, label);
        
	}

	public void BorrarLineas(){
		System.out.println("BL");
		label.getComponent(0).setVisible(false);
		//label.getComponent(0).setFocusable(false);
		//dib.componentes.remove(label.getComponent(0));
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
	    mouseX = e.getX();
	    mouseY = e.getY();

	    try{
		    int[] ups = transformar(tamano, tamW, tamH);
	    	((Component) mover).setBounds(mouseX, mouseY,ups[0],ups[1]);
	    }catch (Exception ed){}

	    if(ent!=null){
	    	des_transformar(tamano, mouseX, mouseY, ent);
	    }
	}

}
