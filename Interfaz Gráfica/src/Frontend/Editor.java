package Frontend;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import Backend.ClassDiagram;
import Backend.Diagram;
import Backend.DocXML;
import Backend.NodeXML;
import Backend.Parser;
import Backend.UseCaseDiagram;


public class Editor extends JPanel{
	private static final long serialVersionUID = 1L;
	public int state; //US-> state = 1; UC-> state=0
	public JTextPane textArea;
	public JPopupMenu cp ;
	public ArrayList<String> flechaTextoB = new ArrayList<String>();
	public int position;
	int enter;
	public String nombre;
	public JTextPane lines;
	public DefaultStyledDocument doc;
	EscritorAutomatico escritor;
	int pos = 0;
	String tamTab = "    ";
	int tamText = 13;
	Style azul;
	Style naranjo;
	Style verde;
	Style negro;
	Style blanco;
	Style rojo;
	Style error;
	JMenuItem seleccionarTodo;
	JMenuItem Copy;
	JMenuItem Paste;
	JMenuItem Cut;
	StyleContext sc;
	
	boolean guardado = false;
	String pathArchivo;
	
	int LineError = 0;
	String MensajeError = "";
	
	JPopupMenu cp2;
	JMenuItem nom;
	
	String TemplateDC =             	
			"<ClassDiagram name=\"Content Management System\">\n"+
        	"<class id=\"c1\" name=\"BlogAccount\">\n"+
        	"<attributes>\n"+
        	"<att name=\"name\" type=\"String\" visibility=\"-\"/>\n"+
        	"<att name=\"publicURL\" type=\"URL\" visibility=\"+\"/>\n"+
        	"<att name=\"authors\" type=\"Author[1..5]\" visibility=\"-\"/>\n"+
        	"</attributes>\n"+
        	"<methods>\n"+
        	"<method name=\"addEntry\" type=\"void\"  visibility=\"+"+"\">\n"+
        	"<param name=\"newEntry\" type=\"BlogEntry\"/>\n"+
        	"</method>\n"+
        	"</methods>\n"+
        	"</class>\n"+
        	"</ClassDiagram>\n";
	String TemplateUC =
					"<UseCaseDiagram name=\"Content Management System\">"+"\n"+
        			"<actors>"+"\n"+
        			"<actor type=\"primary\" id=\"a1\" name=\"Administrator\"/>"+"\n"+
        			"<actor type=\"secondary\" id=\"a2\" name=\"Author Credentials Database\"/>"+"\n"+
        			"</actors>"+"\n"+
        			"<usecases>"+"\n"+
        			"<usecase id=\"uc1\" name=\"Create a New Personal Wiki\"/>"+"\n"+
        			"<usecase id=\"uc2\" name=\"Create a New Blog Account\"/>"+"\n"+
        			"<usecase id=\"uc3\" name=\"Record Application Failure\"/>"+"\n"+
        			"<usecase id=\"uc4\" name=\"Check Identity\"/>"+"\n"+
        			"<usecase id=\"uc5\" name=\"Create a New Regular Blog Account\"/>"+"\n"+
        			"<usecase id=\"uc6\" name=\"Create a New Editorial Blog Account\"/>"+"\n"+
        			"</usecases>"+"\n"+
        			"<connections>"+"\n"+
        			"<connection type=\"basic\" from=\"a1\" to=\"uc1\"/>"+"\n"+
        			"<connection type=\"basic\" from=\"a1\" to=\"uc2\"/>"+"\n"+
        			"<connection type=\"basic\" from=\"a2\" to=\"uc4\"/>"+"\n"+
        			"<connection type=\"extend\" from=\"uc3\" to=\"uc2\"/>"+"\n"+
        			"<connection type=\"extend\" from=\"uc3\" to=\"uc1\"/>"+"\n"+
        			"<connection type=\"include\" from=\"uc1\" to=\"uc4\"/>"+"\n"+
        			"<connection type=\"include\" from=\"uc2\" to=\"uc4\"/>"+"\n"+
        			"<connection type=\"isa\" from=\"uc5\" to=\"uc2\"/>"+"\n"+
        			"<connection type=\"isa\" from=\"uc6\" to=\"uc2\"/>"+"\n"+
        			"</connections>"+"\n"+
        			"</UseCaseDiagram>";
	
	public Editor(int state){			
		
		this.state = state;
		sc = new StyleContext();
		doc = new DefaultStyledDocument(sc);
	    textArea = new JTextPane(doc){
			private static final long serialVersionUID = 1L;
			public boolean getScrollableTracksViewportWidth()
	        {
	            return getUI().getPreferredSize(this).width 
	                <= getParent().getSize().width;
	        }
	    };
	    
	    
	    
	    textArea.setVisible(true);

	    
	    escritor = new EscritorAutomatico(textArea,doc);
	    // Se crean los estilos para colorear
		azul = sc.addStyle("ConstantWidth", null);
		StyleConstants.setForeground(azul, Color.blue);
		naranjo = sc.addStyle("ConstantWidth", null);
		StyleConstants.setForeground(naranjo, Color.ORANGE);
		verde = sc.addStyle("ConstantWidth", null);
		StyleConstants.setForeground(verde, Color.green);
    	negro = sc.addStyle("ConstantWidth", null);
    	StyleConstants.setForeground(negro, Color.black);
    	blanco = sc.addStyle("ConstantWidth", null);
    	StyleConstants.setForeground(blanco, Color.white);
    	rojo = sc.addStyle("ConstantWidth", null);
    	StyleConstants.setForeground(rojo, Color.red);
    	error = sc.addStyle("ConstantWidth", null);
    	StyleConstants.setUnderline(error,true);
    	StyleConstants.setForeground(error, Color.red);
    	
    	
    	// Se crea el espacio para editar
		//textArea.setEditorKit(new NumberedEditorKit());
	    JScrollPane scroll = new JScrollPane ( textArea ){
			private static final long serialVersionUID = 1L;
			@Override
            public Dimension getPreferredSize() {
                return new Dimension(620, 562);
            }
        };		
        
		lines = new JTextPane();
		lines.setText("1 ");
		((DefaultStyledDocument) lines.getDocument()).setCharacterAttributes(0, 2,verde, false);
		lines.setFont(new Font("Arial", Font.BOLD, tamText));
		lines.setEditable(false);
		lines.setBackground(Color.gray);
		lines.setForeground(Color.white);
		
        scroll.setRowHeaderView(lines);
		
	    scroll.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );
	    scroll.setHorizontalScrollBarPolicy( ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
	    
	    
	    
	    this.add (scroll);
	    this.setLayout(new GridBagLayout());	
	    this.setVisible(true);
    	
	    
	    
    	//Menï¿½ del click derecho del mouse
	    /**
	     * En este evento se implementarï¿½ un copy con el botï¿½n derecho
	     */
	    cp= new JPopupMenu();
		Copy = new JMenuItem(new DefaultEditorKit.CopyAction());
        Copy.setText("Copy");
        Copy.setIcon(new ImageIcon("img//copy.png"));
        //Copy.setMnemonic(KeyEvent.VK_C);
        
		Paste = new JMenuItem(new DefaultEditorKit.PasteAction());
		Paste.setText("Paste");
		Paste.setIcon(new ImageIcon("img//paste.png"));
		//Paste.setMnemonic(KeyEvent.VK_P);
		
		Cut = new JMenuItem(new DefaultEditorKit.CutAction());
		Cut.setText("Cut");
		Cut.setIcon(new ImageIcon("img//cut.png"));
		//Cut.setMnemonic(KeyEvent.VK_T);

		seleccionarTodo = new JMenuItem("Select All",new ImageIcon("img//STodo.png"));
		cp.add(Copy);
		cp.add(Paste);
		cp.add(Cut);
	    cp.add(seleccionarTodo);	    	    
	   
	    /**
	     * Este evento selecciona todo el texto del editor.
	     */
	    seleccionarTodo.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent event) {
	        	textArea.selectAll();            	
	        }
	    });
	    

		    
	    /**
	     * En este evento se ejecuta al presionar el botï¿½n derecho del mouse,
	     * muestra el menï¿½ de copy,paste,selectAll
	     */
	    textArea.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseReleased(MouseEvent e) {
		        if (e.getButton() == MouseEvent.BUTTON3) {
		            cp.show(e.getComponent(), e.getX(), e.getY());
		        }
		    }
		});
	    
	    /**
	     * 
	     * Mostrar diï¿½logo de error
	     */
	    lines.addMouseListener(new MouseAdapter() {
			
	    	public void mouseClicked(MouseEvent e) {
	    		if(lines.getSelectedText()!=null){
	    			if(lines.getSelectedText().contains(""+LineError) && (""+LineError).contains(lines.getSelectedText())){
	    				cp2 = new JPopupMenu();
	    				cp2.setBackground(Color.red);
	    				nom = new JMenuItem("Error");
	    				nom.setEnabled(false);
	    				cp2.add(nom);
	    				nom = new JMenuItem(MensajeError);
	    				nom.setEnabled(false);
	    				cp2.add(nom);
	    				cp2.show(e.getComponent(), e.getX(), e.getY());
	    			}
		    }
	    	}
		    public void mouseExited(MouseEvent e) {
		    	 //cp2.setVisible(false);

		    }  
	        
	    } 		
	    		);

	  /**
	   * En este evento estï¿½ atento a los click y colorea el texto
	   */
	    
	    textArea.addMouseListener(new MouseAdapter() {
			
	    	public void mousePressed(MouseEvent e) {
	    			try {
						enumerar();
					} catch (BadLocationException e1) {
						e1.printStackTrace();
					}
		           try {
					colorearPalabras();
				} catch (BadLocationException ev) {
					ev.printStackTrace();
				}
		        
		    }
	        
	    } 		
	    		);
	    
	    textArea.addKeyListener(new KeyAdapter() {
	        public void keyPressed( KeyEvent evento )
	        {
	           try {
	        	  
				colorearPalabras();
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
	        }
	        public void keyReleased( KeyEvent evento )
	        {	try {
				enumerar();
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
	           try {
				colorearPalabras();
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
	    		enter = textArea.getText().split("\n").length;
		    	if(
		    			enter>0 && evento.getKeyChar()==''){
		    		enter--;
		    	}
	        }
	    });
	    
	    doc.addDocumentListener(  new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				/*if(flechaTextoB.get(flechaTextoB.size()-1)!=textArea.getText()){
					flechaTextoB.add(textArea.getText());
				}*/
				//
				
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
			}
		});
	    
	    flechaTextoB.add("");
	    textArea.addKeyListener(new KeyAdapter() {
	    	
	    	public void keyReleased(KeyEvent e) {

				flechaTextoB.add(textArea.getText());
				position+=1;
				for(int i = position+1; i<flechaTextoB.size();i++){
					
					flechaTextoB.remove(i);
				}
			
	    	}
	    	
			public void keyTyped(KeyEvent e) {
		    	if(e.getKeyChar()=='\n'){
		    		int pos = textArea.getCaretPosition();
		    		doc.setCharacterAttributes(0, doc.getLength(), negro, true);
		    		tabular();
		    		lines.setText(getText());
		    		try{
		    			textArea.setCaretPosition(pos);
		    		}catch(Exception es){
		    			//textArea.setCaretPosition(pos);
		    		}
		    	}
				try {
					enumerar();
				} catch (BadLocationException e2) {
					e2.printStackTrace();
				}
	    		enter = textArea.getText().split("\n").length;
		    	try {
		    		colorearPalabras();
				} catch (BadLocationException e1) {
					e1.printStackTrace();
				}

		    	if(enter>0 && e.getKeyChar()==''){
		    		enter--;
		    	}
		    	try {
					enumerar();
				} catch (BadLocationException e1) {
					e1.printStackTrace();
				}
			}
	    }	    
			
		);

	}
	
	
	public void tabFuera(){		
		//textArea.setCaretPosition(doc.getLength());
		//escritor.escribir("");
		if(textArea.getText().length()>0){
		doc.setCharacterAttributes(0, doc.getLength(), negro, true);
		tabular();
		lines.setText(getText());		
		try {
			textArea.setCaretPosition(0);
			colorearPalabras();
			enumerar();
		} catch (BadLocationException e) {
			e.printStackTrace();
		}}
	}
	
	private void tabular(){
		//textArea.setText(textArea.getText()+"\n");		
		String[] texto = textArea.getText().split("\n");
		//String va = "";
		StringBuilder va = new StringBuilder();
		String t = tamTab;//"    ";
		if(state==0){
			for(String u:texto){
				String l = u.toLowerCase();
				char[] up = u.toCharArray();
				int co =0;
				while(co<u.length()){
					if(up[co] == '<'){
						break;
					}
					co++;
				}
				if(co>=u.length()){
					co = 0;
				}
				String li = u.substring(co,u.length());
				//li = li.replace(t, "");
				//li = u.replace("	", " ");
				if(l.contains("classdiagram")){
					va.append(li+"\n");
				}else if(l.contains("classes")){
					va.append(t+li+"\n");
				}else if(l.contains("class")){
					va.append(t+t+li+"\n");
				}else if(l.contains("attributes")){
					va.append(t+t+t+li+"\n");
				}else if(l.contains("att")){
					va.append(t+t+t+t+li+"\n");
				}else if(l.contains("methods")){
					va.append(t+t+t+li+"\n");
				}else if(l.contains("method")){
					va.append(t+t+t+t+li+"\n");
				}else if(l.contains("parameters")){
					va.append(t+t+t+t+t+li+"\n");
				}else if(l.contains("param")){				
					va.append(t+t+t+t+t+t+li+"\n");
				}else if(l.contains("connections")){
					va.append(t+li+"\n");
				}else if(l.contains("connection")){
					va.append(t+t+li+"\n");
				}//else if(l.contains("	")){}
				else if(l.contains("\n")){
					va.append(u);
				}
				else{
					va.append(u+"\n");	
				}
			}
			textArea.setText(va.toString());
			}else{
				for(String u:texto){
					//String l = u.toLowerCase();
					//String li = u.replace("	", "");
					String l = u.toLowerCase();
					char[] up = u.toCharArray();
					int co =0;
					while(co<u.length()){
						if(up[co] == '<'){
							break;
						}
						co++;
					}
					if(co>=u.length()){
						co = 0;
					}
					String li = u.substring(co,u.length());
					//li = li.replace(t, "");
					//li = u.replace("	", " ");
					
					
					
					if(l.contains("usecasediagram")){
						va.append(li+"\n");
					}else if(l.contains("actors")){
						va.append(t+li+"\n");
					}else if(l.contains("actor")){
						va.append(t+t+li+"\n");
					}else if(l.contains("usecases")){
						va.append(t+ li+"\n");
					}else if(l.contains("usecase")){					
						va.append(t+t+li+"\n");
					}else if(l.contains("connections")){
						va.append(t+li+"\n");
					}else if(l.contains("connection")){
						va.append(t+t+li+"\n");
					}//else if(l.contains("	")){}
					else if(l.contains("\n")){
						va.append(u);
					}else{
						va.append(u+"\n");
					}
				}
				textArea.setText(va.toString());
			}
	}
	
	
	public String getText(){
		lines.setFont(new Font("Arial", Font.BOLD, tamText));
		textArea.setFont(new Font("Arial", Font.PLAIN, tamText));
		StringBuilder text = new StringBuilder();
		//String text = "1" + "\n";
		text.append("1" + "\n");
		enter = textArea.getText().split("\n").length;
		for(int i = 2; i < enter +2; i++){
				text.append(i + "\n");
		}    
		try {
			enumerar();
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		return text.toString();
	}
	

	/**
	 * Colorear lï¿½neas de error
	 * 
	 */
	
	public void colorearLineasDeError(int nu, String mensaje) throws BadLocationException{
		
			String text=doc.getText(0,doc.getLength()).toLowerCase();
			Scanner scanner = new Scanner(text);
			String str="";
			int letters=0;
			int numlinea=0;
			while (scanner.hasNextLine()&&numlinea<nu) {
				numlinea++;
				str = scanner.nextLine();
				letters+=str.length()+1;				
			}			
			scanner.close();
			str=str.trim();
			doc.setCharacterAttributes(letters-str.length()-1, str.length(), error, false);
			
			LineError = nu;
			MensajeError = mensaje;
		
			int num = nu-1;
			int pint = 0;
			int c = 0;
			if(num<9){
				pint = 2;
				c =num;
			}else if (num<99){
				pint = 3;
				c = 9 + (num-9)*2; // (9-0) * 1
			}else if(num<999){
				pint = 4;
				c = 188 + (num-99)*3;//(98-9)*2+ 9 +1  (10)
			}else if(num<9999){
				pint = 5;
				c = 2889 + (num-999)*4; //(998-99)*3 + 188 + 4 (192)
			}else if(num < 99999){
				pint = 6;
				c = 38888 + (num-9999)*5; // (9998-999)*4 + 2889 + 3 (2892)
			}else if(num < 999999){
				pint = 7;
				c = 488888 + (num-99999)*6; // + 5 ()
			}
			((DefaultStyledDocument) lines.getDocument()).setCharacterAttributes(num+c, pint,rojo, false);
			
	}
	
	
	
	private void enumerar() throws BadLocationException{
		textArea.setFont(new Font("Arial", Font.PLAIN, tamText));
		((DefaultStyledDocument) lines.getDocument()).setCharacterAttributes(0, lines.getText().length() ,blanco, false);
		int num =0;
    	char[] u = doc.getText(0,doc.getLength()).toCharArray();
		for(int k =0; k<textArea.getCaretPosition();k++){
			if(k<doc.getLength() && u[k]=='\n'){
				num++;
			}
		}
		int pint = 0;
		int c = 0;
		if(num<9){
			pint = 2;
			c =num;
		}else if (num<99){
			pint = 3;
			c = 9 + (num-9)*2; // (9-0) * 1
		}else if(num<999){
			pint = 4;
			c = 188 + (num-99)*3;//(98-9)*2+ 9 +1  (10)
		}else if(num<9999){
			pint = 5;
			c = 2889 + (num-999)*4; //(998-99)*3 + 188 + 4 (192)
		}else if(num < 99999){
			pint = 6;
			c = 38888 + (num-9999)*5; // (9998-999)*4 + 2889 + 3 (2892)
		}else if(num < 999999){
			pint = 7;
			c = 488888 + (num-99999)*6; // + 5 ()
		}
		
		((DefaultStyledDocument) lines.getDocument()).setCharacterAttributes(num+c, pint,verde, false);

	
		//lines.setText(lines.getText());
	}
	
	/**
	 * Este mï¿½todo colorea el texto
	 * @throws BadLocationException
	 */
	public void colorearPalabras() throws BadLocationException{
		doc.setCharacterAttributes(0, doc.getLength(), negro, true);
		if(state==1){
			colorearPalabrasClaveUC();	
		}else{
			colorearPalabrasClaveDC();
		}
		enumerar();
	}
	
	/**
	 * Este método colorea el texto si se está trabajando con UserCases
	 * @throws BadLocationException
	 */
	public void colorearPalabrasClaveUC() throws BadLocationException{
		
			try{
			char[] text = doc.getText(0,doc.getLength()).toCharArray();
			for(int i = 0; i<doc.getLength();i++){
				if(i+6<text.length&& ( (text[i]=='u' &&text[i+3]=='c' ) || (text[i]=='U' &&text[i+3]=='C' && (text[i+7]!='s'||text[i+7]=='D')))&&text[i+1]=='s'&&text[i+2]=='e'&&text[i+4]=='a'&&text[i+5]=='s'&&text[i+6]=='e'){
					if(text.length == i+7){
						doc.setCharacterAttributes(i, 7, azul, false);		
					}
					if(i+7<text.length &&(text[i+7]==' '||text[i+7]=='='||text[i+7]=='>')){
						doc.setCharacterAttributes(i, 7, azul, false);	
					}
					if(i+8<text.length &&text[i+7]=='s'&& (text[i+8]==' '||text[i+8]=='='||text[i+8]=='>')){
						doc.setCharacterAttributes(i, 8, azul, false);	
					}
					if(i+14<text.length && text[i+7]=='D'&& text[i+8]=='i'&& text[i+9]=='a'&& text[i+10]=='g'&& text[i+11]=='r'&& text[i+12]=='a'&& text[i+13]=='m'
							&& (text[i+14]==' '||text[i+14]=='='||text[i+14]=='>')){
						doc.setCharacterAttributes(i, 14, azul, false);	
					}
				}
				
				
				if(i+4<text.length&& text[i]=='a'&&text[i+1]=='c'&&text[i+2]=='t'&&text[i+3]=='o'&&text[i+4]=='r'){
					if(text.length == i+5){
						doc.setCharacterAttributes(i, 5, azul, false);		
					}
					if(i+5<text.length &&(text[i+5]==' '||text[i+5]=='='||text[i+5]=='>')){
						doc.setCharacterAttributes(i, 5, azul, false);	
					}
					if(i+6<text.length &&text[i+5]=='s'&& (text[i+6]==' '||text[i+6]=='='||text[i+6]=='>')){
						doc.setCharacterAttributes(i, 6, azul, false);	
					}
				}
				
				if(i+9<text.length&& text[i]=='c'&&text[i+1]=='o'&&text[i+2]=='n'&&text[i+3]=='n'&&text[i+4]=='e'&&text[i+5]=='c'&&text[i+6]=='t'&&text[i+7]=='i'&&text[i+8]=='o'&&text[i+9]=='n'){
					if(text.length == i+10){
						doc.setCharacterAttributes(i, 10, azul, false);		
					}
					if(i+10<text.length &&(text[i+10]==' '||text[i+10]=='='||text[i+10]=='>')){
						doc.setCharacterAttributes(i, 10, azul, false);	
					}
					if(i+11<text.length &&text[i+10]=='s'&& (text[i+11]==' '||text[i+11]=='='||text[i+11]=='>')){
						doc.setCharacterAttributes(i, 11, azul, false);	
					}
				}
				
				
				if(i+1<text.length && text[i]=='"' && (text[i+1]==' '||text[i+1]=='/' || text[i+1]=='>' )){
					int k = i-1;//&&(text[k-1]==' '||text[k-1]=='=')
					while(k>1 && (text[k]=='"')==false){
						k--;
					}
					doc.setCharacterAttributes(k+1, i-(k+1), naranjo, false);

				}
				
				if(text[i]=='#'){
					int j = i;
					while(j<text.length && text[j]!='\n'){
						j++;
					}
					doc.setCharacterAttributes(i, j-i, verde, false);
				}
				

				if(i+1<text.length&&text[i]=='<'&&text[i+1]=='!'){
					int j = i;
					while(j<text.length &&text[j]!='>'){
						j++;
					}
					j++;
					doc.setCharacterAttributes(i, j-i, verde, false);
				}
				
			}
			
			}catch(BadLocationException e){}
	}
	
	
	/**
	 * Este método colorea el texto si se está trabajando con DiagrammClass
	 * @throws BadLocationException
	 */

	public void colorearPalabrasClaveDC() throws BadLocationException{
			
		try{
			char[] text = doc.getText(0,doc.getLength()).toCharArray(); 
			for(int i = 0; i<doc.getLength();i++){
				if(i+4<text.length&& ((text[i]=='c'  &&(text[i+5]==' ' || text[i+5]=='>')) || (text[i]=='C' && text[i+5]=='D' ))&&text[i+1]=='l'&&text[i+2]=='a'&&text[i+3]=='s'&&text[i+4]=='s'){
					if(text.length == i+5){
						doc.setCharacterAttributes(i, 5, azul, false);		
					}
					if(i+5<text.length &&(text[i+5]==' '||text[i+5]=='='||text[i+5]=='>')){
						doc.setCharacterAttributes(i, 5, azul, false);	
					}
					if(i+7<text.length &&text[i+5]=='e'&&text[i+6]=='s' &&(text[i+7]==' '||text[i+7]=='='||text[i+7]=='>')){
						doc.setCharacterAttributes(i, 7, azul, false);	
					}
					if(i+12<text.length && text[i+5]=='D'&& text[i+6]=='i'&& text[i+7]=='a'&& text[i+8]=='g'&& text[i+9]=='r'&& text[i+10]=='a'&& text[i+11]=='m'
							&& (text[i+12]==' '||text[i+12]=='='||text[i+12]=='>')){
						doc.setCharacterAttributes(i, 12, azul, false);	
					}
				}
				
				if(i+2<text.length&& text[i]=='a'&&text[i+1]=='t'&&text[i+2]=='t'){
					if(text.length == i+3){
						doc.setCharacterAttributes(i, 3, azul, false);		
					}
					if(i+3<text.length &&(text[i+3]==' '||text[i+3]=='='||text[i+3]=='>')){
						doc.setCharacterAttributes(i, 4, azul, false);	
					}
					if(i+10<text.length && text[i+3]=='r'&& text[i+4]=='i'&& text[i+5]=='b'&& text[i+6]=='u'&& text[i+7]=='t'&& text[i+8]=='e'&& text[i+9]=='s'
							&& (text[i+10]==' '||text[i+10]=='='||text[i+10]=='>')){
						doc.setCharacterAttributes(i, 10, azul, false);	
					}
				}
				
				if(i+4<text.length&& text[i]=='p'&&text[i+1]=='a'&&text[i+2]=='r'&&text[i+3]=='a'&&text[i+4]=='m'){
					if(text.length == i+5){
						doc.setCharacterAttributes(i, 5, azul, false);		
					}
					if(i+5<text.length &&(text[i+5]==' '||text[i+5]=='='||text[i+5]=='>')){
						doc.setCharacterAttributes(i, 5, azul, false);	
					}
					if(i+10<text.length && text[i+5]=='e'&& text[i+6]=='t'&& text[i+7]=='e'&& text[i+8]=='r'&& text[i+9]=='s'
							&& (text[i+10]==' '||text[i+10]=='='||text[i+10]=='>')){
						doc.setCharacterAttributes(i, 10, azul, false);	
					}
				}
				
				
				if(i+5<text.length&& text[i]=='m'&&text[i+1]=='e'&&text[i+2]=='t'&&text[i+3]=='h'&&text[i+4]=='o' &&text[i+5]=='d'){
					if(text.length == i+6){
						doc.setCharacterAttributes(i, 6, azul, false);		
					}
					if(i+6<text.length &&(text[i+6]==' '||text[i+6]=='='||text[i+6]=='>')){
						doc.setCharacterAttributes(i, 6, azul, false);	
					}
					if(i+7<text.length && text[i+6]=='s'
							&& (text[i+7]==' '||text[i+7]=='='||text[i+7]=='>')){
						doc.setCharacterAttributes(i, 7, azul, false);	
					}
				}
				
				
				if(i+9<text.length&& text[i]=='c'&&text[i+1]=='o'&&text[i+2]=='n'&&text[i+3]=='n'&&text[i+4]=='e'&&text[i+5]=='c'&&text[i+6]=='t'&&text[i+7]=='i'&&text[i+8]=='o'&&text[i+9]=='n'){
					if(text.length == i+10){
						doc.setCharacterAttributes(i, 10, azul, false);		
					}
					if(i+10<text.length &&(text[i+10]==' '||text[i+10]=='='||text[i+10]=='>')){
						doc.setCharacterAttributes(i, 10, azul, false);	
					}
					if(i+11<text.length &&text[i+10]=='s'&& (text[i+11]==' '||text[i+11]=='='||text[i+11]=='>')){
						doc.setCharacterAttributes(i, 11, azul, false);	
					}
				}
				
				if(i+1<text.length && text[i]=='"' && (text[i+1]==' '||text[i+1]=='/' || text[i+1]=='>' )){
					int k = i-1;
					while(k>1 && (text[k]=='"')==false){
						k--;
					}
					doc.setCharacterAttributes(k+1, i-(k+1), naranjo, false);

				}
				
				if(text[i]=='#'){
					int j = i;
					while(j<text.length && text[j]!='\n'){
						j++;
					}
					doc.setCharacterAttributes(i, j-i ,verde, false);
				}
				
				if(i+1<text.length&&text[i]=='<'&&text[i+1]=='!'){
					int j = i;
					while(j<text.length &&text[j]!='>'){
						j++;
					}
					j++;
					doc.setCharacterAttributes(i, j-i, verde, false);
				}
			}
			
			}catch(BadLocationException e){}
			
	}
	
	/**
	 * Se implementarála opción de back con el texto
	 */
	public void back(){
		if(position>0){
			position--;
			textArea.setText(flechaTextoB.get(position));
		}
		try {
			colorearPalabras();
		} catch (BadLocationException e) {
			e.printStackTrace();
		}	
	}
	
	/**
	 * Se implementarï¿½ la opciï¿½n de forward con el texto
	 */
	public void forward(){
		if(position<flechaTextoB.size()-1){
			position++;
			textArea.setText(flechaTextoB.get(position));
		}
		try {
			colorearPalabras();
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Escribe en el editor un ejemplo de UC o de DC
	 */
	public void template(int s){
		int k = textArea.getCaretPosition();
		if(s==0){
			try {
				doc.insertString(k, "\n" +TemplateDC,null);
				textArea.setCaretPosition(k+("\n" +TemplateDC).length());
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		}else if(state==1){
			try {
				doc.insertString(k, "\n" +TemplateUC,null);
				textArea.setCaretPosition(k+("\n" +TemplateUC).length());
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		}
		lines.setText(getText());
		
	}
	
	/**
	 * Limpia el Área de texto
	 */
	public void clearAll(){
		textArea.setText("");		
	}
	public String abrir(JFileChooser file){
		
		String nombre = this.nombre;
		   try {
			   int k = textArea.getCaretPosition();
				pathArchivo = file.getSelectedFile().getPath();
			   nombre = file.getSelectedFile().getName();
			   Parser p = new Parser();
			   String s = p.fileToString(pathArchivo)+"\n";
			   System.out.println(s);
				doc.insertString(k, s,null);
				textArea.setCaretPosition(k+(s).length());
				
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		   lines.setText(getText());
		   try {
		
			   Parser p = new Parser();
				String s = textArea.getText();
				DocXML aux = p.getDocument(s);
				NodeXML root=aux.root;
				if(root.type.equals("UseCaseDiagram")){
					InterfazUsuario.state(1);
				}
				else if(root.type.equals("ClassDiagram")){
					InterfazUsuario.state(0);
				}
			   
			enumerar();
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		   return nombre;
	}
	
	public void guardar(JFileChooser file){
		
		FileWriter fichero = null;
        PrintWriter pw = null;
        try
        {
            fichero = new FileWriter(file.getSelectedFile()+"/"+nombre+".txt");
            pw = new PrintWriter(fichero);
            pw.print(textArea.getText());
            pathArchivo = file.getSelectedFile()+"/"+nombre+".txt";
            guardado = true;
 
        } catch (Exception e) {
        } finally {
           try {
           if (null != fichero)
              fichero.close();
           } catch (Exception e2) {
           }
        }
        
		guardado = true;
		
	}
	
	public void sobreEscribir(){
		FileWriter fichero = null;
        PrintWriter pw = null;
        try
        {
            fichero = new FileWriter(pathArchivo);
            pw = new PrintWriter(fichero);
            pw.print(textArea.getText());
 
        } catch (Exception e) {
        } finally {
           try {
           if (null != fichero)
              fichero.close();
           } catch (Exception e2) {
           }
        }
	}
	
}