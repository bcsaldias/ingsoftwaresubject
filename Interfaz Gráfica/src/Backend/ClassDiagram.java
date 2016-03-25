package Backend;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Representa un diagrama de clases
 * @author Rodrigo
 *
 */
public class ClassDiagram extends Diagram{

	/**
	 * Constructor, guarda entidades varias y detecta errores dado el doc XML
	 * @param root raiz del XML
	 */
	public ClassDiagram(NodeXML root)
	{
		entities=new HashMap<>();
		associations=new ArrayList<>();
		error=1;
		name=root.attributes.get("name");
		if(name==null){errorId=3;return;}
		for(NodeXML nodo: root.listaNodos)
		{
			if(nodo.type.equals("class"))
			{
				error++;
				boolean wd=addEntity(nodo);
				if(!wd)return;
				error++;
			}

			else if(nodo.type.equals("connections"))
			{
				error++;
				for(NodeXML nodo2: nodo.listaNodos)
				{
					error++;
					if(!nodo2.type.equals("connection")){errorId=5;return;}
					boolean wd = addAssociation(nodo2);
					if(!wd)return;
				}
				error++;
			}
			//nodo no valido
			else{errorId=5; return;}
		}		
		error++;
	}
	private boolean addEntity(NodeXML nodo)
	{
		HashMap<String, String> att = nodo.attributes;		
		if(att.get("name")==null||att.get("id")==null){errorId=3;return false;}
		if(entities.get(remove(att.get("id")))!=null){errorId=2;return false;}
		String id=remove(att.get("id"));
		String[] text=new String[3];	
		text[0]=remove(att.get("name"));
		int numA=0;
		int numM=0;
		String attributes="";
		String methods="";

		for(NodeXML nodoHijo: nodo.listaNodos)
		{
			error++;
			if(nodoHijo.type.equals("attributes"))
			{
				for(NodeXML nodoAtt :nodoHijo.listaNodos)
				{
					error++;
					if(nodoAtt.type.equals("att"))
					{
						HashMap<String, String> atrNA=nodoAtt.attributes;
						//si falta alg�n atributo
						if(atrNA.get("name")==null||atrNA.get("type")==null||atrNA.get("visibility")==null){errorId=3;return false;}

						String name=remove(atrNA.get("name"));
						String type=remove(atrNA.get("type"));
						String vis=remove(atrNA.get("visibility"));

						if(numA==0)
						{
							attributes+=vis+name+":"+type;
						}
						else
						{
							attributes+="\n"+vis+name+":"+type;
						}
						numA++;
					}
					else{errorId=5; return false;}
				}
			}
			else if(nodoHijo.type.equals("methods"))
			{
				for(NodeXML nodoM :nodoHijo.listaNodos)
				{
					error++;
					if(nodoM.type.equals("method"))
					{
						HashMap<String, String> atrM=nodoM.attributes;
						if(atrM.get("name")==null||atrM.get("type")==null||atrM.get("visibility")==null){errorId=3;return false;}

						String name=remove(atrM.get("name"));
						String type=remove(atrM.get("type"));
						String vis=remove(atrM.get("visibility"));
						String parameters="";						

						for(NodeXML nodoParam :nodoM.listaNodos)
						{
							error++;
							if(nodoParam.type.equals("param"))
							{
								HashMap<String, String> atrP=nodoParam.attributes;
								if(atrP.get("name")==null||atrP.get("type")==null){errorId=3;return false;}
								String nameP=remove(atrP.get("name"));
								String typeP=remove(atrP.get("type"));
								parameters+=","+nameP+":"+typeP;	
							}
							else{errorId=5; return false;}
						}						
						if(parameters.length()>0)parameters=parameters.substring(1);						
						if(numM==0)
						{
							methods+=vis+name+"("+parameters+")"+":"+type;
						}
						else
						{
							methods+="\n"+vis+name+"("+parameters+")"+":"+type;
						}
						error++;
						numM++;
					}
					else{errorId=5; return false;}
				}
			}
			//nodo no valido
			else{errorId=5; return false;}
			error++;

		}

		text[1]=attributes;
		text[2]=methods;
		Entity ent = new Entity(0.0, 0.0, 100.0, 100.0, text, 1);
		ent.calculateSize();
		entities.put(id, ent);	
		return true;

	}

	private boolean addAssociation(NodeXML nodo)
	{
		HashMap<String, String> att = nodo.attributes;
		if(att.get("type")==null||att.get("from")==null||att.get("to")==null){errorId=3;return false;}
		String type=remove(att.get("type"));
		String id1=remove(att.get("from"));
		String id2=remove(att.get("to"));

		//caso en que hay coneccion sin id creada
		if(entities.get(id1)==null||entities.get(id2)==null){errorId=1;return false;}		

		double[][] pos=new double[2][2];
		if(!type.equals("dependency")&&!type.equals("association")&&!type.equals("inheritance")&&!type.equals("composition")&&!type.equals("aggregation"))
		{
			errorId=4;
			return false;
		}
		associations.add(new Association(type, pos, id1, id2));
		return true;
	}

	/**
	 * Crea la distribucion del diagrama, calcula las posiciones de las distintas entidades
	 */
	public void createDiagram()
	{
		ArrayList<String> added=new ArrayList<>();
		ArrayList<ArrayList<String>> tree = new ArrayList<ArrayList<String>>();
	
		while(added.size()<entities.size())
		{
			ArrayList<ArrayList<String>> newtree=createTree(added);
			if(!newtree.isEmpty())
			{
				//System.out.println(added);
				tree.addAll(newtree);

			}
		}
		
		
		height=tree.size()*300;
		
		int maxGen=-1;
		for(ArrayList<String> gen: tree)
		{
			if(gen.size()>maxGen)
			{
				maxGen=gen.size();
			}
		}
		
		width=maxGen*300;
		
		double prof=150;
		for(ArrayList<String> gen: tree)
		{
			double posX=0;
			double chunk=width/(gen.size()+1);
			for(String key: gen)
			{
				posX+=chunk;
				Entity ent=entities.get(key);
				double[] pos=calculatePosCorner(posX, prof, ent.height, ent.width);
				ent.x=pos[0];
				ent.y=pos[1];
			}
			prof+=300;
		}
		
		createConnections();
	}

	/**
	 * entrega el numero de vecinos de una entidad
	 * @param id id de la entidad
	 * @return num de vecinos
	 */
	private int numInteraction(String id)
	{
		int num=0;
		for(Association aso: associations)
		{
			if(aso.id1.equals(id)||aso.id2.equals(id))num++;
		}
		return num;
	}

	/**
	 * 
	 * @param id
	 * @param added
	 * @return Entrega una lista con los vecinos de un entidad no agregados al arbol
	 */
	private ArrayList<String> neighborsNotAdded(String id, ArrayList<String> added)
	{
		ArrayList<String> out=new ArrayList<>();
		for(Association aso: associations)
		{
			if(aso.id1.equals(id)&&!added.contains(aso.id2)&&!out.contains(aso.id2))
			{
				out.add(aso.id2);
			}
			else if(aso.id2.equals(id)&&!added.contains(aso.id1)&&!out.contains(aso.id1))
			{
				out.add(aso.id1);
			}
		}
		return out;
	}

	/**
	 * crea un arbol maximo a partir de las entidades ya agregadas
	 * @param added
	 * @return
	 */
	private ArrayList<ArrayList<String>> createTree(ArrayList<String> added)
	{
		int max=-1;
		String maxInteractionId="";
		for(String key : entities.keySet()) 
		{
			if(!added.contains(key))
			{
				int num=numInteraction(key);
				if(max<num){max=num;maxInteractionId=key;}
			}
		}
		
		ArrayList<ArrayList<String>> tree = new ArrayList<ArrayList<String>>();
		if(max==-1)return tree;
		ArrayList<String> gen1 = new ArrayList<String>();
		gen1.add(maxInteractionId);
		tree.add(gen1);
		added.add(maxInteractionId);

		
		ArrayList<String> actualGen=gen1;
		for(String key : entities.keySet())
		{			
			//System.out.println(key);
			ArrayList<String> newgen=new ArrayList<>();
			for(String father: actualGen)
			{
				ArrayList<String> aux=neighborsNotAdded(father,added);
				newgen.addAll(aux);
				added.addAll(aux);
			}
			if(!newgen.isEmpty())
			{
				tree.add(newgen);
				actualGen=newgen;
			}
		}
		return tree;
	}
		
}
