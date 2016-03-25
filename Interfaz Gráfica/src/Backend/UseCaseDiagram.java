package Backend;
import java.util.ArrayList;
import java.util.HashMap;
/**
 * Representa un diagrama de casos de uso
 * @author Rodrigo
 *
 */
public class UseCaseDiagram extends Diagram {
	/**
	 * Constructor, guarda entidades varias y detecta errores dado el doc XML
	 * @param root raiz del XML
	 */
	public UseCaseDiagram(NodeXML root)
	{
		int completedNode=0;
		entities=new HashMap<>();
		associations=new ArrayList<>();
		error = 1;
		name=root.attributes.get("name");
		if(name==null){errorId=3;return;}
		for(NodeXML nodo: root.listaNodos)
		{
			if(nodo.type.equals("actors"))
			{
				error++;
				for(NodeXML nodo2: nodo.listaNodos)
				{
					error++;
					if(!nodo2.type.equals("actor")){errorId=5;return;}
					boolean wd=addEntity(nodo2);
					if(!wd)return;

				}
				error++;
				completedNode=1;

			}
			else if(nodo.type.equals("usecases"))
			{
				error++;
				for(NodeXML nodo2: nodo.listaNodos)
				{
					error++;
					if(!nodo2.type.equals("usecase")){errorId=5;return;}
					boolean wd=addEntity(nodo2);
					if(!wd)return;
				}
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
		if(nodo.type.equals("actor"))
		{
			//si falta alg�n atributo
			if(att.get("name")==null||att.get("type")==null||att.get("id")==null){errorId=3;return false;}
			String[] text=new String[1];
			text[0]=remove(att.get("name"));
			int type=1;
			if(att.get("type").equals("\"primary\""))type=1;
			else if(att.get("type").equals("\"secondary\""))type=2;
			else {errorId=4;return false;}

			if(entities.get(remove(att.get("id")))!=null){errorId=2;return false;}
			entities.put(remove(att.get("id")), new Entity(0.0, 0.0, 90.0, 125.0, text, type));
		}
		
		else
		{
			//si falta alg�n atributo
			if(att.get("name")==null||att.get("id")==null){errorId=3;return false;}

			String[] text=new String[1];
			text[0]=remove(att.get("name"));	
			if(entities.get(remove(att.get("id")))!=null){errorId=2;return false;}
			Entity ent=new Entity(0.0, 0.0, 150.0, 80.0, text, 3);
			ent.calculateSize();
			entities.put(remove(att.get("id")), ent);
			int intId =entities.size()-1;
		}
		
		

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

		if(!type.equals("basic")&&!type.equals("isa")&&!type.equals("extend")&&!type.equals("include"))
		{
			errorId=4;
			return false;
		}		
		double[][] pos=new double[2][2];
		associations.add(new Association(type, pos, id1, id2));
		return true;
	
	}	
	/**
	 * Crea la distribucion del diagrama, calcula las posiciones de las distintas entidades
	 */

	public void createDiagram()
	{
		ArrayList<String> prim=new ArrayList<>();
		ArrayList<String> sec=new ArrayList<>();
		ArrayList<String> usecase1=new ArrayList<>();
		ArrayList<String> usecase2=new ArrayList<>();
		ArrayList<String> usecase3=new ArrayList<>();

		
		for(String key : entities.keySet()) 
		{
			Entity ent=entities.get(key);
			if(ent.type==1)
			{
				prim.add(key);
			}
			else if(ent.type==2)
			{
				sec.add(key);
			}
		}			

		for(String key1 : entities.keySet()) 
		{
			if(prim.contains(key1)||sec.contains(key1))continue;
			boolean added=false;
			
			for(String key2 : prim)
			{
				if(IsNeighbor(key1, key2))
				{
					usecase1.add(key1);
					added=true;
				}
			}
			for(String key2 : sec)
			{
				if(IsNeighbor(key1, key2))
				{
					usecase3.add(key1);
					added=true;
				}
			}
			if(!added)
			{
				usecase2.add(key1);
				added=true;
			}
		}
		
	
		int n1=prim.size();
		int n2=sec.size();
		int n3=usecase1.size();
		int n4=usecase2.size();
		int n5=usecase3.size();
		
		int h=Math.max(Math.max(Math.max(Math.max(n1, n2),n3),n4),n5);
		height=h*250;
		width=1000;
		
		double sN1=height/(n1+1);
		double sN2=height/(n2+1);
		double sN3=height/(n3+1);
		double sN4=height/(n4+1);
		double sN5=height/(n5+1);
		
		double posY=0;
		for(String key: prim)
		{
			posY+=sN1;
			Entity ent=entities.get(key);
			double[] pos=calculatePosCorner(100, posY, ent.height, ent.width);
			ent.x=pos[0];
			ent.y=pos[1];
		}
		
		posY=0;
		for(String key: sec)
		{
			posY+=sN2;
			Entity ent=entities.get(key);
			double[] pos=calculatePosCorner(900, posY, ent.height, ent.width);
			ent.x=pos[0];
			ent.y=pos[1];
		}
				
		posY=0;
		for(String key: usecase1)
		{
			posY+=sN3;
			Entity ent=entities.get(key);
			double[] pos=calculatePosCorner(300, posY, ent.height, ent.width);
			ent.x=pos[0];
			ent.y=pos[1];
		}
		
		posY=0;
		for(String key: usecase2)
		{
			posY+=sN4;
			Entity ent=entities.get(key);
			double[] pos=calculatePosCorner(500, posY, ent.height, ent.width);
			ent.x=pos[0];
			ent.y=pos[1];
		}
		
		posY=0;
		for(String key: usecase3)
		{
			posY+=sN5;
			Entity ent=entities.get(key);
			double[] pos=calculatePosCorner(700, posY, ent.height, ent.width);
			ent.x=pos[0];
			ent.y=pos[1];
		}
		
		createConnections();
		

	}

	/**
	 * Indica si una entidad esta asociada con otra
	 * @param id1 id de la primera entidad
	 * @param id2 id de la segunda entidad
	 * @return True si están asociados, False si no
	 */
	private boolean IsNeighbor(String id1,String id2)
	{
		for(Association aso :associations)
		{
			if((aso.id1.equals(id1)&&aso.id2.equals(id2))||(aso.id2.equals(id1)&&aso.id1.equals(id2)))
			{
				return true;
			}
		}
		return false;
	}
	


}
