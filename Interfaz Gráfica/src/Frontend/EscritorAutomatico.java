package Frontend;


import javax.swing.JEditorPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;

public class EscritorAutomatico {

	public JEditorPane textArea;
	public DefaultStyledDocument doc;
	
	public EscritorAutomatico(JEditorPane t, DefaultStyledDocument d) {
		textArea = t; doc=d;
	}

	
	int cantD = 0;
	/**
	 * Este m�todo escribe autom�ticamente el template para ingresar un UCDiagramm
	 */
	public void  UseCaseDiagram(){
		try {
			String poner = "<UseCaseDiagram name=\"nombre"
					+ "\">\n\n";
			if(cantD>0){
				poner = "<UseCaseDiagram name=\"nombre"+cantD
						+ "\">";
			}
			cantD++;
			String poner2 = "\n\n</UseCaseDiagram>";
			doc.insertString(0,poner,null);
			doc.insertString(doc.getLength(),poner2,null);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Este m�todo escribe autom�ticamente el template para ingresar actores
	 */
	public void  Actors(){
		String poner = "<actors>\n</actors>";
		escribir(poner);
		
	}
	int idAc = 0;
	/**
	 * Este m�todo escribe autom�ticamente el template para ingresar un actor
	 */
	public void  Actor(){
		String poner = "\n<actor type=\"primary/secondary/out\" id=\"a"+idAc+"\" name=\"nombre\" />";
		if(escribir(poner)){
			idAc++;
		}
	}
	
	/**
	 * Este m�todo escribe autom�ticamente el template para ingresar use cases
	 */
	public void  UseCases(){
		String poner = "<usecases>\n</usecases>";
		escribir(poner);
		
	}
	int idUc = 0;
	/**
	 * Este m�todo escribe autom�ticamente el template para ingresar un use case
	 */
	public void  UseCase(){
		String poner = "\n<usecase id=\"uc"+idUc+"\" name=\"nombre\" />";

		if(escribir(poner)){
			idUc++;
		}
	}
	
	/**
	 * Este m�todo escribe autom�ticamente el template para ingresar conecciones
	 */
	public void  Connections(){
		String poner = "<connections>\n</connections>";
		escribir(poner);
	}

	/**
	 * Este m�todo escribe autom�ticamente el template para ingresar una conexi�n
	 */
	public void  Connection(int state){
		String poner ="";
		if(state==1){
			 poner = "\n<connection type=\"basic/extend/include/isa\" from=\"ID_Class\" to=\"ID_Class\"/>";
		}else{
			poner = "\n<connection type=\"depe/asos/agreg/comp/her\" from=\"ID_Class\" to=\"ID_Class\"/>";
		}
		escribir(poner);
	}
	
	
	
	
	int idCd =0;
	/**
	 * Este m�todo escribe autom�ticamente el template para ingresar un Diagrama de clases
	 */
	public void ClassDiagram(){
		try {
			
			String poner = "<ClassDiagram name=\"nombre"
					+ "\">\n\n";
			if(idCd>0){
				poner = "<ClassDiagram name=\"nombre"+idCd
						+ "\">\n\n";
			}
			idCd++;
			String poner2 = "\n\n</ClassDiagram>";
			doc.insertString(0,poner,null);
			doc.insertString(doc.getLength(),poner2,null);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Este m�todo escribe autom�ticamente el template para ingresar clases
	 */
	public void Classes(){
		String poner = "<classes>\n</classes>";
		escribir(poner);
	}
	
	int idCla =0 ;
	/**
	 * Este m�todo escribe autom�ticamente el template para ingresar una clase
	 */
	public void Class(){
			String poner = "\n<class id=\"c" +idCla+"\" name=\"nombre\">\n</class>";
			if(escribir(poner)){
				idCla++;
			}
	}
    
	/**
	 * Este m�todo escribe autom�ticamente el template para ingresar atributos
	 */
	public void Attributes(){
		String poner = "<attributes>\n</attributes>";
		escribir(poner);
	}
	int idAt =0 ;    
	/**
	 * Este m�todo escribe autom�ticamente el template para ingresar un atributo
	 */
	public void Attribute(){
		String poner = "\n<att name=\"name"+idAt+"\" type=\"String/int/..\" visibility=\"+/-/..\"/>";
		if(escribir(poner)){
		idAt++;
		}
    	
    }
    
	/**
	 * Este m�todo escribe autom�ticamente el template para ingresar m�todos
	 */
    public void Methods(){
		String poner = "<methods>\n</methods>\n";
		escribir(poner);
    }
    int idMe=0;
	/**
	 * Este m�todo escribe autom�ticamente el template para ingresar un m�todo
	 */
    public void Method(){
		String poner = "\n<method name=\"name"+idMe+"\" type=\"void/int/..\" visibility=\"+/-/..\"/>";
		if(escribir(poner)){
		idMe++;}
    }

	/**
	 * Este m�todo escribe autom�ticamente el template para ingresar par�metros
	 */
    public void Parameters(){
		String poner = "<parameters>\n</parameters>";
		escribir(poner);
    }
    int idPa =0;
	/**
	 * Este m�todo escribe autom�ticamente el template para ingresar un par�metro
	 */
    public void Parameter(){
		String poner = "\n<param name=\"name"+idPa+"\" type=\"String/int/..\" visibility=\"+/-/..\"/>";
		if(escribir(poner)){
		idPa++;
		}
    }
    
	/**
	 * Este m�todo escribe autom�ticamente el template para ingresar un comentario
	 */
    public void comment(){
		escribir("\n<!--Comment-->");
    }

    
    public boolean escribir(String poner){
    	int k = textArea.getCaretPosition();
		try {
			doc.insertString(k,poner,null);
		} catch (BadLocationException e) {
			e.printStackTrace();
			return false;
		}
		textArea.setCaretPosition(k+poner.length());
		
		return true;
    }

}
