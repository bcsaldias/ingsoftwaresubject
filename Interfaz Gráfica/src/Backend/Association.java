package Backend;

/**
 * Representa una asociacion entre entidades
 * Se guardan los puntos de la linea, los id de las entidades
 * y el tipo de asociacion
 * @author Rodrigo
 *
 */

public class Association {
	
	public double[][] points;
	public String id1,id2,type;
	
	public Association(String type,double[][] points,String id1,String id2)
	{
		this.type=type;
		this.points=points;
		this.id1=id1;
		this.id2=id2;
	}
	
	/**
	 * Calcula los puntos que forman el rombo incidente de la asociacion
	 * @param size indica el tamaño del rombo
	 * @return int[][] retorna los puntos del rombo
	 */
	public int[][] calculateArrow(double size)
	{
		//[0][0] coord x del primer punto
		//[1][0] coord y del primer punto
		
		//[1][0] coord x del segundo punto
		//[1][1] coord y del segundo punto
		//...
		
		//*** el formato es distinto al de points, ya que en el segundo [] indicaba si era el x o el y
		//la salida de este metodo en cambio indica en el primer [] si es el x o el y 
		//(Hace mas el dibujo de poligonos)
		
		//si quieres dibujar el triangulo, no consideres el ultimo punto 2
		//para la flecha, dibuja 2 rectas, una partiendo en el punto 0 al punto 1
		// y desde el punto 0 hasta el 3
		
		//definimos nuestro cuadrado
		int[][] out=new int[2][4];
		double x0=points[1][0];
		double y0=points[1][1];
		
		double x1=size/2.0;
		double y1=size/2.0;
		
		double x2= size;
		double y2= 0;
		
		double x3= size/2.0;
		double y3=-size/2.0;
		
		//obtenemos el angulo de rotacion
		double ang=Math.atan2(points[0][1]-y0, points[0][0]-x0);
		double cos=Math.cos(ang);
		double sin=Math.sin(ang);
		
		//aplicamos matriz de rotacion para cada punto
		double px1=cos*x1-sin*y1;
		double py1=sin*x1+cos*y1;
		
		double px2=cos*x2-sin*y2;
		double py2=sin*x2+cos*y2;
		
		double px3=cos*x3-sin*y3;
		double py3=sin*x3+cos*y3;
				
		//asignamos output
		out[0][0]=(int)x0;
		out[1][0]=(int)y0;
		
		out[0][1]=(int)(px1+x0);
		out[1][1]=(int)(py1+y0);
		
		out[0][2]=(int)(px2+x0);
		out[1][2]=(int)(py2+y0);
		
		out[0][3]=(int)(px3+x0);
		out[1][3]=(int)(py3+y0);		
		return out;		
	}

}
