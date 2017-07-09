
public class Point {
	public int row,column,value;
	public Point pred;
	public EditType alignParent;
	public Point(int row,int column, int value) {
		this.row=row;
		this.column=column;
		this.value=value;
		pred=null;
		alignParent=null;
		
	}
	
}
