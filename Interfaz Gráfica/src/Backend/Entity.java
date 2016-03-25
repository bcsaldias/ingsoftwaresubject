package Backend;
/**
 * Representa una entidad del diagrama,
 * tiene una cierta posicion, tamaño y tipo asociado
 * @author Rodrigo
 *
 */
public class Entity {
	public double x,y,width,height;
	public int size,type;
	public String[] text;
	public String id="";
	public Entity(double x,double y, double width, double height ,String[] text,int type)
	{
		this.x=x;
		this.y=y;
		this.width=width;
		this.height=height;
		this.text=text;
		this.size=text.length;
		this.type=type;
	}
	
	/**
	 * Calcula el tamaño de la entidad segun el texto asociado
	 */
	public void calculateSize()
	{
		if(type==3)
		{
			String[] a = text[0].split(" ");
			StringBuilder k = new StringBuilder();
			for(int i =0;i<a.length-1;i++){
				if(a[i].length()+a[i+1].length()<15){
					k.append(a[i]+" "+a[i+1]+"#");
					i++;
				}else{
					k.append(a[i]+" "+"#");
				}
			}
			k.append(a[a.length-1]);
			String[] u = k.toString().split("#");
			int max = 0;
			for(String s:u){
				if(s.length()>max){
					max = s.length();
				}
			}
			
			height=20*u.length+25;
			width=8*max+35;
		}
		else if(type==4)
		{
			String[] a = text[0].split(" ");
			StringBuilder k = new StringBuilder();
			for(int i =0;i<a.length-1;i++){
				if(a[i].length()+a[i+1].length()<15){
					k.append(a[i]+" "+a[i+1]+"#");
					i++;
				}else{
					k.append(a[i]+" "+"#");
				}
			}
			k.append(a[a.length-1]);
			String[] u = k.toString().split("#");
			int max = 0;
			for(String s:u){
				if(s.length()>max){
					max = s.length();
				}
			}
			height=20*u.length+25;
			width=7*max+30;		
			
		}
		else
		{
			int numRows=0;
			int maxLength=0;
			for(int i=0;i<text.length;i++)
			{
				numRows+=numsOfRows(text[i]);
				if(maxLength<maxLength(text[i]))
				{
					maxLength=maxLength(text[i]);
				}
			}
			height=19*numRows+10;
			width=7*maxLength+20;
			
		}
		
	}
	
	private int numsOfRows(String s)
	{
		String[] arr = s.split("\n");
		return arr.length;
	}
	
	private int maxLength(String s)
	{
		int max=0;
		String[] parts = s.split("\n");
		for(int i=0;i<parts.length;i++)
		{
			if(max<parts[i].length())
			{
				max=parts[i].length();
			}
		}
		return max;
	}
}
