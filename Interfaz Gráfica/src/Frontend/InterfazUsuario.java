package Frontend;
import Backend.Parser;

import javax.swing.BorderFactory;

import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;

import javax.swing.plaf.ScrollBarUI;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.plaf.metal.*;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JPopupMenu.Separator;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.plaf.metal.MetalBorders.PopupMenuBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Label;
import java.awt.PopupMenu;
import java.awt.Scrollbar;
import java.awt.TextArea;
import java.awt.event.ActionListener;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.channels.SelectionKey;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import Backend.Association;
import Backend.ClassDiagram;
import Backend.Diagram;
import Backend.DocXML;
import Backend.Entity;
import Backend.NodeXML;
import Backend.Parser;
import Backend.UseCaseDiagram;

@SuppressWarnings({ "unused", "serial" })


/**
 * @author Belï¿½n Saldï¿½as F
 */
public class InterfazUsuario extends JFrame implements ActionListener{
	private int position;
	JPopupMenu cp ;
	static Pestana pestana;
	static Editor editor;
	static Visor visor;
	static JCheckBoxMenuItem UCC;
	static JCheckBoxMenuItem UDC;
	static JToolBar toolbarDC;
	static JToolBar toolbarUC;
	JToolBar toolbar2;
	JFileChooser chooser;
	static Pestana[] pestanas;
	Label numLinea;
	JButton AddComment1;
	JButton AddComment2;
	JFrame comentario;
	/**
	 * Esta clase es la encargada de interactuar con el usuario.
	 */
	public InterfazUsuario(){	
		this.setTitle("UML Diagram Editor");
		pestanas = new Pestana[100];
		ImageIcon img = new ImageIcon("img/mac_love.png");
		this.setIconImage(img.getImage());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		chooser = new JFileChooser();
		this.setSize(1200, 719);
		this.setResizable(false);

		
		 File theDir = new File("temp");
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
		
		//
		// Editor de Texto
		//	
		//Distintas pestaï¿½as para editar texto
		toolbar2 = new JToolBar();
		toolbar2.setAlignmentX(0);



		AddComment1 = new JButton("AddComment");
		AddComment2 = new JButton("AddComment");

		//
		// MENï¿½
		//

		//Este es el menï¿½ principal
		JMenuBar menubar = new JMenuBar();
		setJMenuBar(menubar);

		// Botï¿½n file
		JMenu file = new JMenu("File");
		JMenuItem eMenuExit = new JMenuItem("Close All", new ImageIcon("img/exit.png"));
		JMenuItem eMenuNew = new JMenuItem("New XML", new ImageIcon("img/new.png"));
		JMenuItem eMenuOpen = new JMenuItem("Open XML", new ImageIcon("img/open.png"));
		JMenuItem eMenuSave = new JMenuItem("Save XML", new ImageIcon("img/save.png"));
		JMenuItem eMenuSaveAs = new JMenuItem("Save XML As", new ImageIcon("img/saveas.png"));
		JMenuItem eMenuImage = new JMenuItem("Export UML",new ImageIcon("img/image_export.png"));
		//JMenu eMenuImageAs = new JMenu("Export UML");
		//JMenuItem eMenuJpg = new JMenuItem(".jpg");
		//JMenuItem eMenuPng = new JMenuItem(".png");
		//eMenuImageAs.add(eMenuJpg);
		//eMenuImageAs.add(eMenuPng);
		file.add(eMenuNew);
		file.add(eMenuOpen);
		file.add(eMenuSave);
		file.add(eMenuSaveAs);
		file.addSeparator();
		//file.add(eMenuImage);
		file.add(eMenuImage);
		file.addSeparator();
		file.add(eMenuExit);
		menubar.add(file);
		eMenuExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				int r = JOptionPane.showConfirmDialog(new JFrame(), "¿Estás seguro de que quieres Cerrar Todo?\nSi no has guardado, te recomendamos hacerlo.",null,0,1, new ImageIcon("exit.png"));
				if(r==0){
					System.exit(0);}}});

		eMenuImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				String b = (String) JOptionPane.showInputDialog(null,"Image Name","Nombre", JOptionPane.QUESTION_MESSAGE,new ImageIcon("rename.png"), null,editor.nombre);
				JFileChooser file=new JFileChooser();
				//file.setApproveButtonText("Guardar");
				//file.setDialogTitle("Guardar");
				file.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); 
				file.showSaveDialog(null);
				Parser p = new Parser();
				String s = editor.textArea.getText();
				DocXML aux = p.getDocument(s);
				NodeXML root=aux.root;
				Diagram diagr = null;
				if(root.type.equals("UseCaseDiagram")){
					diagr=new UseCaseDiagram(root);
				}
				else if(root.type.equals("ClassDiagram")){
					diagr=new ClassDiagram(root);
				}

				diagr.createDiagram();
				visor.guardarFoto(editor,file,b);
			}
		});

		eMenuNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				nuevaPestana(editor.state);
			}
		});
		eMenuOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				abrirArchivo();
			}
		});
		eMenuSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				guardarArchivo();
			}
		});

		eMenuSaveAs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				guardarArchivoComo();
			}
		});        

		// Menï¿½ Template
		JMenu template = new JMenu("Template");
		JMenuItem DC = new JMenuItem("Class Diagram");
		JMenuItem UC = new JMenuItem("User Case Diagram");
		template.add(DC);
		template.add(UC);
		menubar.add(template);
		DC.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				state(0);
				editor.template(0);
				try {
					editor.colorearPalabras();
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
			}
		});
		UC.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				state(1);		
				editor.template(1);
				try {
					editor.colorearPalabras();
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
				UDC.setSelected(false);	
			}
		});


		// Menï¿½ Options
		JMenu options = new JMenu("Options");
		JMenuItem tab_size = new JMenuItem("Set Tab Size");
		options.add(tab_size);
		tab_size.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				try{
					int tam = Integer.parseInt((String)JOptionPane.showInputDialog(null,"Tamaño: ","Set Tab Size", JOptionPane.QUESTION_MESSAGE,new ImageIcon("img//identar.png"), null,editor.tamTab.length()));
					StringBuilder tab = new StringBuilder();
					for(int i = 0;i<tam;i++){
						tab.append(" ");
					}
					editor.tamTab = tab.toString();
				}catch(Exception ex){
					System.out.print("imposible cambiar tamaño");
				}
			}
		});

		JMenuItem text_size = new JMenuItem("Set Text Size");
		options.add(text_size);
		text_size.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				try{
					int tam = Integer.parseInt((String)JOptionPane.showInputDialog(null,"Tamaño: ","Set Text Size", JOptionPane.QUESTION_MESSAGE,new ImageIcon("img//format_font_size_less.png"), null,editor.tamText));
					editor.tamText = tam;
					editor.textArea.setText(editor.textArea.getText());
					editor.lines.setText(editor.getText());
				}catch(Exception ex){
					System.out.print("imposible cambiar tamaño");
				}
			}
		});



		menubar.add(options);


		// Menu help
		JMenu help = new JMenu("Help");

		JMenuItem U = new JMenuItem("Documentation");
		help.add(U);
		U.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				//String b = (String) JOptionPane.showInputDialog(null,"Buscar: ","Documentaciï¿½n", JOptionPane.QUESTION_MESSAGE,null, null,"jo");
				Documentacion win = new Documentacion();

			}
		});


		JMenuItem B = new JMenuItem("Contact");
		B.setVisible(false);
		help.add(B);

		menubar.add(Box.createHorizontalGlue());
		menubar.add(help);

		//Panel de JToolBars
		JPanel panel; 
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		add(panel, BorderLayout.NORTH);

		// Barra de botones generales
		JToolBar toolbar = new JToolBar();      
		JButton clearTodo = new JButton(new ImageIcon("img//clearAll.png"));
		JButton play = new JButton(new ImageIcon("img//play.png"));
		JButton back = new JButton(new ImageIcon("img//back.png"));
		JButton forward = new JButton(new ImageIcon("img//forward.png"));
		JButton ag = new JButton(new ImageIcon("img//ag.png"));
		JButton ach = new JButton(new ImageIcon("img//ach.png"));
		JButton comment = new JButton(new ImageIcon("img//comment.png"));
		JButton clearIM = new JButton(new ImageIcon("img//clearIm.png"));
		JButton newFile = new JButton(new ImageIcon("img//new2.png"));
		JButton identar = new JButton(new ImageIcon("img//identar.png"));

		clearTodo.setBorder(null);
		play.setBorder(null);
		back.setBorder(null);
		forward.setBorder(null);
		ag.setBorder(null);
		ach.setBorder(null);
		comment.setBorder(null);
		clearIM.setBorder(null);
		newFile.setBorder(null);
		identar.setBorder(null);

		toolbar.add(newFile);
		toolbar.add(clearTodo);
		toolbar.add(comment);
		toolbar.add(back);
		toolbar.add(forward);
		toolbar.add(identar);


		UCC = new JCheckBoxMenuItem("Create User Case Diagram");
		UDC = new JCheckBoxMenuItem("Create Class Diagram");

		toolbar.add(Box.createHorizontalGlue());
		toolbar.add(UCC);
		toolbar.add(UDC);       

		toolbar.add(Box.createHorizontalGlue());
		toolbar.add(play);
		toolbar.add(clearIM);
		toolbar.add(ach);
		toolbar.add(ag);


		toolbar.setAlignmentX(0);
		panel.add(toolbar);
		
		ag.setVisible(false);
		ach.setVisible(false);

		ag.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				//visor.label.setSize(visor.label.getSize().width+20, visor.label.getSize().height+20);  
				//visor.cambiarImagen(visor.path);
			}
		});

		ach.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				//visor.label.setSize(visor.label.getSize().width-20, visor.label.getSize().height-20);  
				//visor.cambiarImagen(visor.path);    
				if(visor.dib!=null && visor.dib.componentes!=null){
					for(Elemento c: visor.dib.componentes){
					c.label.setVisible(false);
					}
					visor.BorrarLineas();
					
					ArrayList<Elemento> aux = new ArrayList<Elemento>();
				for(Elemento c: visor.dib.componentes){
					int[] t = visor.tamano;
					t[0]+=20;
					t[1]+=20;
					visor.ponerObjeto(c.pathN, t, c.ancho, c.alto, c.x, c.y, c, c.p);
				}
				 visor.dib.componentes = aux;
				}
				
			
			}
		});


		identar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				editor.tabFuera();    				
			}
		});

		newFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {

				nuevaPestana(editor.state);

			}
		});

		//J
		//Por ahora carga una imagen de muestra.
		play.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {    

				Parser p = new Parser();
				String s = editor.textArea.getText();
				if(s.length()!=0)
				{
					s=p.deleteComment(s);
				}
				if(!p.wellDesigned(s)){
					try {
						editor.colorearLineasDeError(p.error.lineNumber, p.error.description);
					} catch (BadLocationException e) {
						e.printStackTrace();
					}
				}else if(editor.textArea.getText().length()>0){
					DocXML aux = p.getDocument(s);
					if(aux!=null)
					{
						System.out.println(aux);
						NodeXML root=aux.root;
						Diagram diagr = null;
						if(root.type.equals("UseCaseDiagram")){
							diagr=new UseCaseDiagram(root);
						}
						else if(root.type.equals("ClassDiagram")){
							diagr=new ClassDiagram(root);
						}
						diagr.createDiagram();
						if(diagr.errorId!=0){
							try {
								//System.out.println(diagr.error);
								editor.colorearLineasDeError(error(diagr.error,s), diagr.returnError());
							} catch (BadLocationException e) {
								e.printStackTrace();
							}
						}else{
							//visor = new Visor(new JLabel());
							String nombre = "diagrama";//JOptionPane.showInputDialog("nombre:");

							if(visor.dib!=null && visor.dib.componentes!=null){
								for(Elemento c: visor.dib.componentes){
								c.label.setVisible(false);
								}
								visor.BorrarLineas();
								visor.dib.componentes = null;
								}

							visor.dibujarDiagrama(diagr, editor, nombre);

							visor.dib.dibujarLineas();

						}
						/*
        				System.out.println(diagr.formatTextBySize("lalsdgiqwedkqOIFWOGUFWFAA", 4));
        				System.out.println("("+diagr.width+","+diagr.height+")");
        				for(String key : diagr.entities.keySet()) 
        				{
        					Entity ent = diagr.entities.get(key);
        					System.out.println(ent.text[0]+" "+ent.type+" ("+ent.x+","+ent.y+")" + " ("+ent.width+","+ent.height+")");
        				}
        				System.out.println("----------------");
        				for(Association aso: diagr.associations)
        				{
        					System.out.println(aso.type+" "+aso.id1+" "+aso.id2+" ("+aso.points[0][0]+","+aso.points[0][1]+")" +" ("+aso.points[1][0]+","+aso.points[1][1]+")");
        				}
        				System.out.println((diagr.error)+" "+diagr.errorId);*/


					}
				}



				//DocXML asd = p.getDocument(s);
			}
		});

		clearIM.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				if(visor.dib!=null && visor.dib.componentes!=null){
					for(Elemento c: visor.dib.componentes){
					c.label.setVisible(false);
					}
					visor.BorrarLineas();
					visor.dib.componentes = null;
					}	
			}
		});

		comment.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				editor.escritor.comment();
				editor.lines.setText(editor.getText());
				try {
					editor.colorearPalabras();
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
			}
		});

		clearTodo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				editor.clearAll();
				//editor.lines.setText("01");
			}
		});
		back.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				editor.back();
			}
		});
		forward.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				editor.forward();
			}
		});

		UCC.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				int r = JOptionPane.showConfirmDialog(new JFrame(), "¿Quieres editar User Cases Diagram?",null,0,1, new ImageIcon("exit.png"));
				if(r==0){
					state(1);
				}else{
					UCC.setSelected(false);
				}
			}
		});

		UDC.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				int r = JOptionPane.showConfirmDialog(new JFrame(), "¿Quieres editar Class Diagram?",null,0,1, new ImageIcon("exit.png"));
				if(r==0){
					state(0);
				}else{
					UDC.setSelected(false);

				}
			}
		});





		//Barras de botones de programaciï¿½n rï¿½pida DiagramClass
		toolbarDC = new JToolBar();

		JButton ClassDiagram = new JButton("ClassDiagram");
		// JButton Classes = new JButton("Classes");
		JButton Class = new JButton("Class");
		JButton Attributes = new JButton("Attributes");
		JButton Attribute = new JButton("Attribute");
		JButton Methods = new JButton("Methods");
		JButton Method = new JButton("Method");
		JButton Parameters = new JButton("Parameters");
		JButton Parameter = new JButton("Parameter");
		JButton Connections2 = new JButton("Connections");
		JButton Connection2 = new JButton("Connection");

		toolbarDC.add(ClassDiagram);
		//  toolbarDC.add(Classes);
		toolbarDC.add(Class);
		toolbarDC.add(Attributes);
		toolbarDC.add(Attribute);
		toolbarDC.add(Methods);
		toolbarDC.add(Method);
		toolbarDC.add(Parameters);
		toolbarDC.add(Parameter);
		toolbarDC.add(Connections2);
		toolbarDC.add(Connection2);

		Connections2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				editor.escritor.Connections();
				editor.lines.setText(editor.getText());
				try {
					editor.colorearPalabras();
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
			}
		});        
		Connection2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				editor.escritor.Connection(editor.state);
				editor.lines.setText(editor.getText());
				try {
					editor.colorearPalabras();
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
			}
		});
		ClassDiagram.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				editor.escritor.ClassDiagram();
				editor.lines.setText(editor.getText());
				try {
					editor.colorearPalabras();
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
			}
		});
		/* Classes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
            	editor.escritor.Classes();
            	editor.lines.setText(editor.getText());
            	try {
					editor.colorearPalabras();
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
            }
        });  */
		Class.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				editor.escritor.Class();
				editor.lines.setText(editor.getText());
				try {
					editor.colorearPalabras();
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
			}
		});        
		Attributes.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				editor.escritor.Attributes();
				editor.lines.setText(editor.getText());
				try {
					editor.colorearPalabras();
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
			}
		});
		Attribute.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				editor.escritor.Attribute();
				editor.lines.setText(editor.getText());
				try {
					editor.colorearPalabras();
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
			}
		});        
		Methods.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				editor.escritor.Methods();
				editor.lines.setText(editor.getText());
				try {
					editor.colorearPalabras();
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
			}
		});        
		Method.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				editor.escritor.Method();
				editor.lines.setText(editor.getText());
				try {
					editor.colorearPalabras();
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
			}
		});
		Parameters.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				editor.escritor.Parameters();
				editor.lines.setText(editor.getText());
				try {
					editor.colorearPalabras();
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
			}
		});        
		Parameter.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				editor.escritor.Parameter();
				editor.lines.setText(editor.getText());
				try {
					editor.colorearPalabras();
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
			}
		});        

		toolbarDC.setAlignmentX(0);
		panel.add(toolbarDC);
		toolbarDC.setVisible(false);

		//Barras de botones de programaciï¿½n rï¿½pida UseCase
		toolbarUC = new JToolBar();

		JButton UseCaseDiagram = new JButton("UseCaseDiagram");
		JButton Actors = new JButton("Actors");
		JButton Actor = new JButton("Actor");
		JButton UseCases = new JButton("UseCases");
		JButton UseCase = new JButton("UseCase");
		JButton Connections = new JButton("Connections");
		JButton Connection = new JButton("Connection");

		toolbarUC.add(UseCaseDiagram);
		toolbarUC.add(Actors);
		toolbarUC.add(Actor);
		toolbarUC.add(UseCases);
		toolbarUC.add(UseCase);
		toolbarUC.add(Connections);
		toolbarUC.add(Connection);


		AddComment1 = new JButton("AddComment");
		AddComment2 = new JButton("AddComment");
		AddComment1.setVisible(true);
		AddComment2.setVisible(true);
		AddComment1.setIcon(new ImageIcon("img//cmm2.png"));
		AddComment2.setIcon(new ImageIcon("img//cmm2.png"));
		AddComment1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				comentario = new comentar(visor);

			}
		});

		AddComment2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				comentario = new comentar(visor);

			}
		});

		toolbarUC.add(Box.createHorizontalGlue());
		toolbarUC.add(AddComment1);
		toolbarDC.add(Box.createHorizontalGlue());
		toolbarDC.add(AddComment2);


		UseCaseDiagram.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				editor.escritor.UseCaseDiagram();
				editor.lines.setText(editor.getText());
				try {
					editor.colorearPalabras();
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
			}
		});
		Actors.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				editor.escritor.Actors();
				editor.lines.setText(editor.getText());
				try {
					editor.colorearPalabras();
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
			}
		});        
		Actor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				editor.escritor.Actor();
				editor.lines.setText(editor.getText());
				try {
					editor.colorearPalabras();
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
			}
		});        
		UseCases.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				editor.escritor.UseCases();
				editor.lines.setText(editor.getText());
				try {
					editor.colorearPalabras();
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
			}
		});
		UseCase.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				editor.escritor.UseCase();
				editor.lines.setText(editor.getText());
				try {
					editor.colorearPalabras();
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
			}
		});
		Connections.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				editor.escritor.Connections();
				editor.lines.setText(editor.getText());
				try {
					editor.colorearPalabras();
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
			}
		});        
		Connection.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				editor.escritor.Connection(editor.state);
				editor.lines.setText(editor.getText());
				System.out.println("h");
				try {
					editor.colorearPalabras();
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
			}
		});

		toolbarUC.setAlignmentX(0);
		panel.add(toolbarUC);
		toolbarUC.setVisible(false);



		final JScrollPane ad = new JScrollPane(toolbar2){
			private static final long serialVersionUID = 1L;
			@Override
			public Dimension getPreferredSize() {
				return new Dimension(600, 48);
			}
		};
		ad.createHorizontalScrollBar();

		ad.setAlignmentX(0);
		panel.add(ad);


		//Detalles de inicializaciï¿½n
		this.setLocationRelativeTo ( null );


		//se crea una pestala inicial
		nuevaPestana(0);
		state(0);

		toolbar.setFloatable(false);
		toolbar2.setFloatable(false);
		toolbarDC.setFloatable(false);
		toolbarUC.setFloatable(false);



	}

	private int error(int falso,String ese){
		String[] sep = ese.replace("\r","").split("\n");
		int count = 0;
		int lineareal = 0;
		for(String s : sep)
		{
			if(!(s.equals("")))
			{
				count+=1;
			}
			lineareal+=1;
			if (count==falso)
			{
				break;
			}
		}
		return lineareal;
	}

	private int lineaInit(){
		char[] caracteres = editor.textArea.getText().toCharArray();
		int retornar = 1;
		int j = 0;
		while(caracteres[j] != '<'){
			if(caracteres[j] == '\n'){
				retornar+=1;
			}
			j++;
		}
		return retornar;
	}

	private static int numero = 0;
	static int onumero = 0;
	void nuevaPestana(int s){
		desmarcar();
		if(numero<100){
			pestanas[numero] = new Pestana(numero,onumero,s);
			add(pestanas[numero].editor,BorderLayout.WEST);
			add(pestanas[numero].visor);
			toolbar2.add(pestanas[numero]);
			cambiarPestana(onumero);
			numero++;
			onumero++;
		}
	}

	public static void cambiarPestana(int id){
		for(Pestana p:pestanas){
			if(p!=null && p.iD == id){
				pestana = p;
				editor = p.editor;
				visor = p.visor;
			}
		}

		desmarcar();
		editor.setVisible(true);
		visor.setVisible(true);
		pestana.setBackground(Color.GRAY);
		state(editor.state);

	}

	public static void state(int nuevo){
		if(nuevo ==0){
			UCC.setSelected(false);
			UDC.setSelected(true);	
			toolbarUC.setVisible(false);
			toolbarDC.setVisible(true);
			editor.state =0;
		}else{
			UCC.setSelected(true);
			UDC.setSelected(false);	
			toolbarUC.setVisible(true);
			toolbarDC.setVisible(false);
			editor.state =1;
		}
	}

	private static void desmarcar(){
		for(int j =0; j<100 && pestanas[j]!=null ;j++){
			pestanas[j].editor.setVisible(false);
			pestanas[j].visor.setVisible(false);
			pestanas[j].setBackground(Color.LIGHT_GRAY);
		}
	}
	public static void eliminarPestana(int i){
		Pestana[] aux = new Pestana[100];
		int k = 0;
		for(int j=0;j<numero;j++){
			if(pestanas[j].iD!=i){
				aux[k] = pestanas[j];
				k++;
			}
		}
		numero--;
		pestanas = aux;
		desmarcar();

	}
	private void abrirArchivo() {
		JFileChooser file=new JFileChooser();
		file.addChoosableFileFilter(new FileFilter() {
			public String getDescription() {
				return "TXT Documents (*.txt)";
			}
			public boolean accept(File f) {
				if (f.isDirectory()) {
					return true;
				} else {
					return f.getName().endsWith(".txt");
				}
			}
		});
		file.showOpenDialog(this);
		if(file!=null){
			nuevaPestana(editor.state);
			String n = editor.abrir(file);
			pestana.rename(1, n.replace(".txt", ""));
		}
	}

	private void guardarArchivo(){
		if(editor.guardado){
			editor.sobreEscribir();
		}else{
			guardarArchivoComo();
		}
	}

	private void guardarArchivoComo(){
		String b = (String) JOptionPane.showInputDialog(null,"File Name","Nombre", JOptionPane.QUESTION_MESSAGE,new ImageIcon("rename.png"), null,editor.nombre);
		pestana.rename(1,b);
		JFileChooser file=new JFileChooser();
		file.setApproveButtonText("Guardar");
		file.setDialogTitle("Guardar");
		file.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); 
		file.showSaveDialog(this);
		editor.guardar(file);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
	}


}