package Backend;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Test;

public class ParserTest {


	@Test
	public void testWellDesigned() {
		Parser p = new Parser();
		String s = p.fileToString("holi.txt");		
		assertEquals("Probando si verifica que esta bien dise�ado", true, p.wellDesigned(s));
	}
	
	@Test
	public void testBalanced() {
		Parser p = new Parser();
		String s = p.fileToString("No_balanceado.txt");		
		assertEquals("Probando si verifica que esta balanceado", false, p.wellDesigned(s));
	}
	
	@Test
	public void testComillas() {
		Parser p = new Parser();
		String s = p.fileToString("Error_comillas.txt");		
		assertEquals("Probando si verifica que estan bien comillas", false, p.wellDesigned(s));
	}
	

	@Test
	public void testGetDocument() {
		Parser p = new Parser();
		String s = p.fileToString("holi.txt");
		DocXML aux = p.getDocument(s);

		
		//Probando parseo de raiz		
		NodeXML xml=aux.root;
		assertEquals("Probando type raiz", "ClassDiagram", xml.type);
		
		HashMap<String, String> attr = new HashMap<>();
		attr.put("name","\"Content Management System\"");
		assertEquals("Probando atributos raiz", attr, xml.attributes);
		

		//Probando parseo de clase
		xml=xml.listaNodos.get(0);
		assertEquals("Probando type clases", "class", xml.type);
		
		attr = new HashMap<>();
		attr.put("id","\"c1\"");
		attr.put("name","\"BlogAccount\"");
		assertEquals("Probando atributos clases", attr, xml.attributes);
		
		//Probando hijos
		
		//atributos de clase
		NodeXML xmlAux=xml.listaNodos.get(0);
		assertEquals("Probando type clases", "attributes", xmlAux.type);
		assertEquals("Probando atributos clases", true, xmlAux.attributes.isEmpty());
		
		NodeXML xmlClase=xmlAux.listaNodos.get(0);
		assertEquals("Probando Hojas de xml", true, xmlClase.listaNodos.isEmpty());
		assertEquals("Probando type clases", "att", xmlClase.type);
		attr = new HashMap<>();
		attr.put("name","\"name\"");
		attr.put("type","\"String\"");
		attr.put("visibility","\"\"");
		assertEquals("Probando atributos", attr, xmlClase.attributes);
		
		xmlClase=xmlAux.listaNodos.get(1);
		assertEquals("Probando Hojas de xml", true, xmlClase.listaNodos.isEmpty());
		assertEquals("Probando type clases", "att", xmlClase.type);
		attr = new HashMap<>();
		attr.put("name","\"name\"");
		attr.put("publicURLtype","\"URL\"");
		attr.put("visibility","\"+\"");
		assertEquals("Probando atributos", attr, xmlClase.attributes);
		
		xmlClase=xmlAux.listaNodos.get(2);
		assertEquals("Probando Hojas de xml", true, xmlClase.listaNodos.isEmpty());
		assertEquals("Probando type clases", "att", xmlClase.type);
		attr = new HashMap<>();
		attr.put("name","\"authors\"");
		attr.put("type","\"Author[1..5]\"");
		attr.put("visibility","\"­\"");
		//System.out.println(xmlClase.attributes);
		assertEquals("Probando atributos", attr, xmlClase.attributes);
		//Hay error en visibilidad -, escribe ­
		
		//metodos
		xml=xml.listaNodos.get(1);
		assertEquals("Probando type metodos", "methods", xml.type);
		assertEquals("Probando atributos metodos", true, xml.attributes.isEmpty());
		
		xml=xml.listaNodos.get(0);
		assertEquals("Probando type method", "method", xml.type);
		attr = new HashMap<>();
		attr.put("name","\"addEntry\"");
		attr.put("type","\"void\"");
		assertEquals("Probando atributos method", attr, xml.attributes);
		
		//parametros del metodo.
		xml=xml.listaNodos.get(0);
		assertEquals("Probando type param", "param", xml.type);
		assertEquals("Probando Hojas de xml", true, xml.listaNodos.isEmpty());
		attr = new HashMap<>();
		attr.put("name","\"newEntry\"");
		attr.put("type","\"BlogEntry\"");
		assertEquals("Probando atributos param", attr, xml.attributes);
	}
	
	

}
