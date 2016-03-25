package Backend;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

public class BackendTest {
	
	@Test
	public void testComment() {
		Parser p = new Parser();
		String s = p.fileToString("..\\doc\\XML de prueba\\Class.txt");
		assertEquals("Probando bien diseï¿½ado Clases", true, p.wellDesigned(s));
		
		DocXML aux = p.getDocument(s);
		NodeXML root=aux.root;
		Diagram diagr;
		diagr=new ClassDiagram(root);
		int size=diagr.associations.size();
		diagr.addComment("hola", "c1");
		diagr.createDiagram();
		assertEquals("Probando agregación de comentarios", "comm0:\nhola", diagr.entities.get("comm0").text[0]);
		assertEquals("Probando agregación de comentarios en asociaciones",size+1,diagr.associations.size());
		
		diagr.removeEntity("comm0");
		assertEquals("Probando eliminación de comentarios", null, diagr.entities.get("comm0"));
		assertEquals("Probando eliminación de comentarios en asociaciones",size,diagr.associations.size());
	}

	@Test
	public void testWellDesignClass() {
		Parser p = new Parser();
		String s = p.fileToString("..\\doc\\XML de prueba\\Class.txt");
		assertEquals("Probando bien diseï¿½ado Clases", true, p.wellDesigned(s));
		
		DocXML aux = p.getDocument(s);
		NodeXML root=aux.root;
		Diagram diagr;
		diagr=new ClassDiagram(root);

		diagr.createDiagram();
		assertEquals("Probando buen formato de clases", 0, diagr.errorId);
	}
	@Test
	public void testWellDesignUseCase() {
		Parser p = new Parser();
		String s = p.fileToString("..\\doc\\XML de prueba\\UseCase.txt");
		assertEquals("Probando bien diseï¿½ado casos de uso", true, p.wellDesigned(s));
		
		DocXML aux = p.getDocument(s);
		NodeXML root=aux.root;
		Diagram diagr;
		diagr=new UseCaseDiagram(root);
		diagr.createDiagram();		
		diagr.createDiagram();
		assertEquals("Probando buen formato de casos de uso", 0, diagr.errorId);
	}
	@Test	
	public void detectLineErrorClass()
	{
		Parser p = new Parser();
		String s = p.fileToString("..\\doc\\XML de prueba\\ClassFormatError.txt");
		assertEquals("Probando bien diseï¿½ado Clases", true, p.wellDesigned(s));		
		DocXML aux = p.getDocument(s);
		NodeXML root=aux.root;
		Diagram diagr;
		diagr=new ClassDiagram(root);
		diagr.createDiagram();
		assertEquals("Probando error formato de clases", 2, diagr.errorId);
		assertEquals("Probando linea de error formato de clases", 16, diagr.error);

		
	}
	@Test
	public void detectLineErrorUseCase()
	{
		Parser p = new Parser();
		String s = p.fileToString("..\\doc\\XML de prueba\\UseCaseFormatError.txt");
		assertEquals("Probando bien diseï¿½ado casos de uso", true, p.wellDesigned(s));
		DocXML aux = p.getDocument(s);
		NodeXML root=aux.root;
		Diagram diagr;
		diagr=new UseCaseDiagram(root);
		diagr.createDiagram();		
		diagr.createDiagram();
		assertEquals("Probando error formato de casos de uso", 3, diagr.errorId);
		assertEquals("Probando linea error formato de casos de uso", 12, diagr.error);
	}
	/*
	  	@Test
	public void QuotesErrorEnd()
	{
		Parser p = new Parser();
		String s = p.fileToString("doc/XMLdeprueba/Class2.txt");
		boolean a = p.wellDesigned(s);
		assertEquals("Probando bien diseÃ±ado casos de uso", false, a);
		int line = p.error.lineNumber;
		String message = p.error.description;
		assertEquals("Probando error formato de casos de uso", 22, line);
		assertEquals("Probando linea error formato de casos de uso", "Quotes opened at EOF.", message);
	}
	@Test
	public void QuotesError()
	{
		Parser p = new Parser();
		String s = p.fileToString("doc/XMLdeprueba/Class.txt");
		boolean a = p.wellDesigned(s);
		assertEquals("Probando bien diseÃ±ado casos de uso", false, a);
		int line = p.error.lineNumber;
		String message = p.error.description;
		assertEquals("Probando error formato de casos de uso", 1, line);
		assertEquals("Probando linea error formato de casos de uso", "Quotes not expected at line 1.", message);
	}
	@Test
	public void detectLineErrorClassDiagram()
	{
		Parser p = new Parser();
		String s = p.fileToString("doc/XMLdeprueba/ClassFormatErrorNode.txt");
		boolean a = p.wellDesigned(s);
		assertEquals("Probando bien diseÃ±ado casos de uso", false, a);
		int line = p.error.lineNumber;
		String message = p.error.description;
		assertEquals("Probando error formato de casos de uso", 13, line);
		assertEquals("Probando linea error formato de casos de uso", "NODE error at line 13.", message);
	}
	@Test
	public void detectEqualsErrorClassDiagram()
	{
		Parser p = new Parser();
		String s = p.fileToString("doc/XMLdeprueba/ClassFormatError.txt");
		boolean a = p.wellDesigned(s);
		assertEquals("Probando bien diseÃ±ado casos de uso", false, a);
		int line = p.error.lineNumber;
		String message = p.error.description;
		assertEquals("Probando error formato de casos de uso", 6, line);
		assertEquals("Probando linea error formato de casos de uso", "'=' Symbol expected at line 6.", message);
	}
	@Test
	public void detectLineErrorUseCase()
	{
		Parser p = new Parser();
		String s = p.fileToString("doc/XMLdeprueba/UseCaseFormatError.txt");
		boolean a = p.wellDesigned(s);
		assertEquals("Probando bien diseÃ±ado casos de uso", false, a);
		int line = p.error.lineNumber;
		String message = p.error.description;
		assertEquals("Probando error formato de casos de uso", 3, line);
		assertEquals("Probando linea error formato de casos de uso", "Line Feed Symbol not expected at line 3.", message);
	}
	 
	 */
	
	

}
