package Backend;
import java.io.*;
import java.util.*;

import Backend.ErrorManager;

public class Parser {

	public ErrorManager error;
	public Parser()
	{
		error = new ErrorManager();
	}
	
	public String fileToString(String path)
	{
		String retString = "";
		boolean copiar = true;
		try {
			BufferedReader in = new BufferedReader(new FileReader(path));
			String str;
			
			while ((str = in.readLine()) != null) {
				String aux = str.trim();
				if(startCmt(aux)&&copiar)
				{
					copiar = false;
				}
				if(copiar)
				{
					retString += str+"\n";
				}
				else
				{
					retString += "\n";
				}
				if(endCmt(aux)&&!copiar)
				{
					copiar = true;
				}
			}
			in.close();
		}
		catch (IOException e) {
		}
		
		retString = retString.substring(0,retString.length()-1);
		if(!copiar)
		{
			return "";
		}
		return retString;
		
	}
	
	public String deleteComment(String file)
	{
		String retString = "";
		boolean copiar = true;

		Scanner scanner = new Scanner(file);
		String str;

		while (scanner.hasNextLine()) {
			str = scanner.nextLine();
			String aux = str.trim();
			if(startCmt(aux)&&copiar)
			{
				copiar = false;
			}
			if(copiar)
			{
				retString += str+"\n";
			}
			else
			{
				retString += "\n";
			}
			if(endCmt(aux)&&!copiar)
			{
				copiar = true;
			}
		}
		scanner.close();



		retString = retString.substring(0,retString.length()-1);
		if(!copiar)
		{
			return retString+"<!";
		}
		return retString;
		
	}
	
	public boolean startCmt(String line)
	{
		if (line.length()>=4)
		{
			if(line.charAt(0)=='<' && line.charAt(1)=='!' && line.charAt(2)=='-' && line.charAt(3)=='-')
			{
				return true;
			}
		}
		return false;
	}
	public boolean endCmt(String line)
	{
		if (line.length()>=3)
		{
			if(line.charAt(line.length()-1)=='>' && line.charAt(line.length()-2)=='-' && line.charAt(line.length()-3)=='-')
			{
				return true;
			}
		}
		return false;
	}
	
	
	public String parseNodeNameSingle(String file, int posBegin)
	{
		int index = posBegin+1;
		int indexFinal = index;
		while(true)
		{
			if(file.charAt(indexFinal)!=' ' || file.charAt(indexFinal)!='>')
			{
				indexFinal++;
			}
			else
			{
				break;
			}
		}
		return file.substring(index, indexFinal);
	}
	
	/**
	 * Comprueba si los paréntesis están correctamente balanceados.
	 * @param file Archivo XML entregado como texto.
	 * @return True si están bien balanceados.
	 */
	public boolean wellDesignedBracket(String file)
	{
		Stack<String> parentesis = new Stack<>();
		boolean analizar = true;
		
		for(int i=0;i<file.length();i++)
		{
			if(file.charAt(i)=='"')
			{
				analizar = !analizar;
			}
			if(analizar)
			{
				if(file.charAt(i)=='<')
				{
					parentesis.push("<");
				}
				else if(file.charAt(i)=='>')
				{
					parentesis.pop();
					if (parentesis.size()>0)
					{
						return false;
					}
				}
			}
		}
			
		if(!analizar)
		{
			return false;
		}
		return true;
	}
	
	/**
	 * Comprueba si el XML está correctamente balanceado
	 * en términos de nodos y paréntesis.
	 * @param file Archivo XML entregado como texto.
	 * @return True si está bien formado.
	 */
	public boolean wellDesigned(String fi)
	{
		String file = fi.replace("\r", "");
		int lineCount = 1;
		Stack<String> brackets = new Stack<>();
		Stack<String> nodes = new Stack<>();
		boolean analizar = true;
		boolean textFinder = false;
		for(int i=0;i<file.length();i++)
		{
			if(file.charAt(i)=='"' )
			{
				analizar = !analizar;
			}
			if(file.charAt(i)=='\n')
			{
				lineCount+=1;
			}
			if(analizar)
			{
				if(file.charAt(i)=='<')
				{
					textFinder = true;
					if(i+1>=file.length())
					{
						this.error.lineNumber = lineCount;
						this.error.description= "EOF not expected at line "+lineCount+".";
						return false;
					}
					if(file.charAt(i+1)=='!')
					{
						this.error.lineNumber = lineCount;
						this.error.description= "Comment not closed.";
						return false;
					}
					if(file.charAt(i+1)==' ' || file.charAt(i+1)=='>' || file.charAt(i+1)=='\n')
					{
						this.error.lineNumber = lineCount;
						this.error.description= "EOT not expected at line "+lineCount+".";
						return false;
					}
					brackets.push("<");
					if(file.charAt(i+1)!='/')
					{						
						String aux = "";
						boolean hasAtt = false;
						for (int j=i+1; j<file.length();j++)
						{					
							if(file.charAt(j)==' ')
							{
								hasAtt = true;
								break;
							}
							else if(file.charAt(j)=='>')
							{	
								break;
							}
							aux = aux+file.charAt(j);
						}
						
						nodes.push(aux);
						
						//Comprobar atributos si es que tiene.
						if(hasAtt)
						{
							String att = "";
							boolean quotes = false;
							for (int j=i+1; j<file.length();j++)
							{	
								if(file.charAt(j)=='"' )
								{
									quotes = !quotes;
									if(quotes)
									{
										att = att+file.charAt(j);
									}
								}
								if(!quotes)
								{
									if(file.charAt(j)=='>')
									{
										break;
									}
									else if(file.charAt(j)=='/')
									{
										break;
									}
									att = att+file.charAt(j);
								}
							}
							if(att.contains("\n"))
							{
								this.error.lineNumber = lineCount;
								this.error.description= "Line Feed Symbol not expected at line "+lineCount+".";
								return false;
							}
							String[] attributes = att.split(" ");
							if(attributes.length <2)
							{
								this.error.lineNumber = lineCount;
								this.error.description= "Attribute expected at line "+lineCount+".";
								return false;
							}
							boolean foundSomething = false;
							for(int k = 1; k<attributes.length;k++)
							{
								if(!attributes[k].equals(""))
								{
									foundSomething = true;
									if(!attributes[k].contains("="))
									{
										this.error.lineNumber = lineCount;
										this.error.description= "'=' Symbol expected at line "+lineCount+".";
										return false;
									}
									String[] auxArr = attributes[k].split("=");
									if(auxArr.length!=2)
									{
										this.error.lineNumber = lineCount;
										this.error.description= "'=' Symbol not expected at line "+lineCount+".";
										return false;
									}
									if(auxArr[0].length() <1 || auxArr[1].length() <2)
									{
										this.error.lineNumber = lineCount;
										this.error.description= "Attributes error at line "+lineCount+".";
										return false;
									}
									if(auxArr[1].charAt(0)!='"' )
									{
										this.error.lineNumber = lineCount;
										this.error.description= "Quotes not expected at line "+lineCount+".";
										return false;
									}
									if(auxArr[1].charAt(auxArr[1].length()-1)!='"' )
									{
										this.error.lineNumber = lineCount;
										this.error.description= "End of Attribute expected at line "+lineCount+".";
										return false;
									}
								}
							}
							if(!foundSomething)
							{
								this.error.lineNumber = lineCount;
								this.error.description= "Attribute not found at line "+lineCount+".";
								return false;
							}
						}

					}
					else
					{
						String aux = "";
						String aux2 = "";
						if(i+2 >= file.length())
						{
							this.error.lineNumber = lineCount;
							this.error.description= "EOF not expected at line "+lineCount+".";
							return false;
						}
						for (int j=i+2; j<file.length();j++)
						{
							if(file.charAt(j)=='>')
							{
								break;
							}
							else if(file.charAt(j)==' ')
							{
								this.error.lineNumber = lineCount;
								this.error.description= "Space Symbol not expected at line "+lineCount+".";
								return false;
							}
							aux = aux+file.charAt(j);
						}
						if(nodes.size()==0)
						{
							this.error.lineNumber = lineCount;
							this.error.description= "NODE error at line "+lineCount+".";
							return false;
						}
						aux2 = nodes.pop();
						if(!aux.equals(aux2))
						{
							this.error.lineNumber = lineCount;
							this.error.description= "NODE error at line "+lineCount+".";
							return false;
						}
					}
					
				}
				else if(file.charAt(i)=='>')
				{
					textFinder = false;
					if(brackets.size()==0)
					{
						this.error.lineNumber = lineCount;
						this.error.description= "'>' not expected at line "+lineCount+".";
						return false;
					}
					brackets.pop();
					if (brackets.size()>0)
					{
						this.error.lineNumber = lineCount;
						this.error.description= "'>' not expected at line "+lineCount+".";
						return false;
					}
				}
				else if(file.charAt(i)=='/')
				{
					if(i+1>=file.length())
					{
						this.error.lineNumber = lineCount;
						this.error.description= "EOF not expected at line "+lineCount+".";
						return false;
					}

					if(file.charAt(i+1)=='>')
					{
						if(nodes.size()==0)
						{
							this.error.lineNumber = lineCount;
							this.error.description= "NODE error at line "+lineCount+".";
							return false;
						}
						nodes.pop();
						
					}
				}
				if(!textFinder && file.charAt(i)!=' ' && file.charAt(i)!='<' && file.charAt(i)!='>' && file.charAt(i)!='\n' && file.charAt(i)!='\t')
				{
					this.error.lineNumber = lineCount;
					this.error.description= "Reserved CHAR not expected at line "+lineCount+".";
					return false;
				}
			}
		}
			
		if(!analizar || brackets.size()>0 || nodes.size()>0)
		{
			if(!analizar)
			{
				this.error.lineNumber = lineCount;
				this.error.description= "Quotes opened at EOF.";
			}
			else
			{
				this.error.lineNumber = lineCount;
				this.error.description= "Unexpected error an line "+lineCount+".";
			}
			return false;
		}
		
		return true;
	}

	/**
	 * Dado un string que representa el XML se genera el árbol XML asociado.
	 * @param file Archivo XML entregado como texto.
	 * @return DocXML parseado. Retorna null en caso de no poder ser parseado.
	 */
	public DocXML getDocument(String file)
	{
		Stack<NodeXML> nodes = new Stack<>();
		boolean analizar = true;
		for(int i=0;i<file.length();i++)
		{
			if(file.charAt(i)=='"' )
			{
				analizar = !analizar;
			}
			if(analizar)
			{
				if(file.charAt(i)=='<')
				{
					if(file.charAt(i+1)!='/')
					{						
						String aux = "";
						boolean hasAtt = false;
						for (int j=i+1; j<file.length();j++)
						{					
							if(file.charAt(j)==' ')
							{
								hasAtt = true;
								break;
							}
							else if(file.charAt(j)=='>')
							{	
								break;
							}
							aux = aux+file.charAt(j);
						}
						NodeXML node = new NodeXML();
						node.type = aux;
						if(nodes.size() == 0)
						{
							nodes.push(node);
						}
						else
						{
							NodeXML auxNode = nodes.pop();
							auxNode.listaNodos.add(node);
							nodes.push(auxNode);
							nodes.push(node);
						}
						
						//Comprobar atributos si es que tiene.
						if(hasAtt)
						{
							String att = "";
							boolean quotes = false;
							for (int j=i+1; j<file.length();j++)
							{	
								if(file.charAt(j)=='"' )
								{
									quotes = !quotes;
									
								}
								if(!quotes)
								{
									if(file.charAt(j)=='>')
									{
										break;
									}
									else if(file.charAt(j)=='/')
									{
										break;
									}
									if(file.charAt(j)==' ')
									{
										att = att+'%';
									}
									else
									{
										att = att+file.charAt(j);
									}
								}
								else
								{
									att = att+file.charAt(j);
								}
								
							}
							String[] attributes = att.split("%");
							boolean foundSomething = false;
							for(int k = 1; k<attributes.length;k++)
							{
								if(!attributes[k].equals(""))
								{
									String[] auxArr = new String[2];
									auxArr[0] = "";
									auxArr[1] = "";
									boolean found = false;
									for(int r=0;r<attributes[k].length();r++)
									{
										char c = (attributes[k]).charAt(r);
										if(c=='=')
										{
											found = !found;
										}
										if(!found)
										{
											auxArr[0] = auxArr[0]+(attributes[k]).charAt(r);
										}
										if(found && c!='=')
										{
											auxArr[1] = auxArr[1]+(attributes[k]).charAt(r);
										}
									}
									NodeXML auxNode = nodes.pop();
									auxNode.attributes.put(auxArr[0], auxArr[1]);
									nodes.push(auxNode);
								}
							}
						}

					}
					else
					{
						NodeXML retNode = nodes.pop();
						if(nodes.size()==0)
						{
							DocXML retDoc = new DocXML();
							retDoc.root = retNode;
							return retDoc;
						}
					}
					
				}
				else if(file.charAt(i)=='/')
				{
					

					if(file.charAt(i+1)=='>')
					{
						NodeXML retNode = nodes.pop();
						if(nodes.size()==0)
						{
							DocXML retDoc = new DocXML();
							retDoc.root = retNode;
							return retDoc;
						}
					}
				}
			}
		}
			
		
		return null;
	}
}
