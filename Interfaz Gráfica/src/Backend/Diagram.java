package Backend;
import java.util.*;

/**
 * Es una clase abstracta que representa un diagrama cualquiera
 * guarda una tabla de hash con las entidades y una lista de asociaciones
 * @author Rodrigo
 *
 */

public abstract class Diagram {
	//0 no hay error, 1 por conexion sin id, 2 por id duplicada, 3 falta un atributo, 4 tipo no valido, 5 nodo malo.
	public int errorId=0;
	public int error=0;
	
	public String name;
	
	public HashMap<String,Entity> entities;
	public ArrayList<Association> associations;
	
	public double height;
	public double width;

	
	public abstract void createDiagram();
	protected String remove(String str)
	{
		return str.substring(1, str.length()-1); 
	}
	
	/**
	 * Calcula los puntos de las asociaciones dados dos ids
	 * @param id1 id de la entidad 1
	 * @param id2 id de la entidad 2
	 * @return puntos de asociacion
	 */
	protected double[][] calculatePoints(String id1,String id2)
	{
		double[][] pos=new double[2][2];
		Entity ent1 =entities.get(id1);
		Entity ent2 =entities.get(id2);
		
		double posX1=ent1.x+(ent1.width)/2.0;
		double posY1=ent1.y+(ent1.height)/2.0;
		double posX2=ent2.x+(ent2.width)/2.0;
		double posY2=ent2.y+(ent2.height)/2.0;
		
		double distX=Math.abs(posX2-posX1);
		double distY=Math.abs(posY2-posY1);
		
		if(distY>=distX)
		{
			if(posY1>=posY2)
			{
				pos[0][0]= (ent1.x+(ent1.width)/2.0);
				pos[0][1]= ent1.y;
				pos[1][0]= (ent2.x+(ent2.width)/2.0);
				pos[1][1]= ent2.y+ent2.height;
			}
			else
			{
				pos[0][0]= (ent1.x+(ent1.width)/2.0);
				pos[0][1]= ent1.y+ent1.height;
				pos[1][0]= (ent2.x+(ent2.width)/2.0);
				pos[1][1]= ent2.y;
			}
		}
		else
		{
			if(posX1>=posX2)
			{
				pos[0][0]= ent1.x;
				pos[0][1]= (ent1.y+(ent1.height)/2.0);
				pos[1][0]= ent2.x+ent2.width;
				pos[1][1]= (ent2.y+(ent2.height)/2.0);
			}
			else
			{
				pos[0][0]= ent1.x +ent1.width;
				pos[0][1]= (ent1.y+(ent1.height)/2.0);
				pos[1][0]= ent2.x;
				pos[1][1]= (ent2.y+(ent2.height)/2.0);
			}
		}
		//if(pos[0][0]==pos[1][0])
		//{
			//pos[0][0]+=1;
		//}
		return pos;
	}
	
	
	public String formatText(String str, int lines)
	{
		String out="";
		int index = 0;
		int size = (int)(str.length()/lines);
		while (index < str.length()) {
		    out+="\n"+(str.substring(index, Math.min(index + size, str.length())));
		    index += size;
		}
		return out;
	}
	
	public String formatTextBySize(String str, int size)
	{
		String out="";
		int index = 0;
		while (index < str.length()) {
		    out+="\n"+(str.substring(index, Math.min(index + size, str.length())));
		    index += size;
		}
		return out;
	}
	

	
	public double[] calculatePosCorner(double x,double y,double height,double width)
	{
		double[] out=new double[2];
		out[0]=(x-width/2.0);
		out[1]=(y-height/2.0);
		return out;
	}
	
	public void createConnections()
	{
		for(Association aso :associations)
		{
			aso.points=calculatePoints(aso.id1, aso.id2);
		}
	}
	
	/**
	 * retorna el tipo de error
	 * @return El tipo de error
	 */
	public String returnError()
	{
		if(errorId==0)return "No error";
		else if(errorId==1)return "Conection without defined Id";
		else if(errorId==2)return "Duplicated Id at line";
		else if(errorId==3)return "Insufficient attributes";
		else if(errorId==4)return "Not defined type";
		else if(errorId==5)return "Not valid node exception";
		else return "";
	}

	
	protected String calculateCommentId()
	{
		int i=0;
		String out="comm"+i;
		while(entities.get(out)!=null)
		{
			i++;
			out="comm"+i;
		}
		return out;
	}
	
	
	/**
	 * Agrega un comentario gráfico al diagrama
	 * @return 
	 */
	
	public String addComment(String comment,String id2)
	{
		if(entities.get(id2)==null)
			return "";
		String[] text=new String[1];
		String id1=calculateCommentId();
		text[0]=id1+":\n"+comment;
		Entity ent=new Entity(0.0, 0.0, 100.0, 100.0, text, 4);
		ent.calculateSize();
		ent.id=id1;
		entities.put(id1, ent);
		double[][] pos=new double[2][2];
		associations.add(new Association("comment", pos, id1, id2));
		return id1;	
	}
	
	public boolean removeEntity(String id)
	{
		if(entities.get(id)==null)
			return false;
		entities.remove(id);
		
		Iterator<Association> i = associations.iterator();
		while (i.hasNext()) {
		   Association aso = i.next();
		   if(aso.id1.equals(id)||aso.id2.equals(id))
		    i.remove();
		}
		return true;
	}
}
