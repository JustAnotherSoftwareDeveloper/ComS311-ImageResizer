import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
/**
 * 
 * @author michael
 * Static class that I have two weeks to do. 
 * Seriously, not cool
 */
public class DynamicProgramming {
	private static int[][] M;
	private static int upperY;
	private static int upperX;
	public static ArrayList<Integer> minCostVC(int[][] M) {
		if (M.length==0) {
			return new ArrayList<>();
		}
		//Init Base Case
		Point[][] costArray=new Point[M.length][M[0].length];
		for(int j=0; j<M[0].length; j++) { //Base Cases
			costArray[0][j]=new Point(0,j,M[0][j]);
		}
		//Dynamically figure out the distances 
		for(int i=1; i<M.length; i++) {
			for(int j=0; j<M[0].length; j++) {
				Point pred;
				if (j==0 && j==M[0].length-1) {
					pred=minThree(null,costArray[i-1][j],null);
				}
				else if (j==0) {
					pred=minThree(null,costArray[i-1][j],costArray[i-1][j+1]);
				}
				else if (j==M[0].length-1) {
					pred=minThree(costArray[i-1][j-1],costArray[i-1][j],null);
				}
				else {
					pred=minThree(costArray[i-1][j-1],costArray[i-1][j],costArray[i-1][j+1]);
				}
				costArray[i][j]=new Point(i,j,M[i][j]+pred.value);
				costArray[i][j].pred=pred;
			}
		}
		//Figure out the minimum bottom distance 
		Point minPoint=null;
		int lastrow=costArray.length-1;
		int minValue=Integer.MAX_VALUE;
		for(int j=0; j<M[0].length; j++) {
			if (costArray[lastrow][j].value<=minValue) {
				minPoint=costArray[lastrow][j];
				minValue=minPoint.value;
			}
		}
		//Get the Path Back
		ArrayList<Point> pathToTop=new ArrayList<>();
		Point currPoint=minPoint;
		while (currPoint!=null) {
			pathToTop.add(currPoint);
			currPoint=currPoint.pred;
		}
		Collections.reverse(pathToTop);
		//Convert to Integer Array
		ArrayList<Integer> intPath=new ArrayList<>();
		for(Point p: pathToTop) {
			intPath.add(p.row);
			intPath.add(p.column);
		}
		return intPath;
	}
	private static  Point minThree(Point one,Point two,Point three) {
		
		if (one==null && three==null) {
			return two;
		}
		else if (one==null) {
			return minTwo(two,three);
		}
		else if (three==null) {
			return minTwo(one,two);
		}
		else {
			Point firstComp;
			if (one.value<two.value) { //If they're both the same it'll take the first path. Deal
				firstComp=one;
			}
			else {
				firstComp=two;
			}
			if (firstComp.value<three.value) {
				return firstComp;
			}
			else {
				return three;
			}
		}
	}
	private static Point minTwo(Point one,Point two) {
		if (one.value>two.value) {
			return two;
		}
		else {
			return one;
		}
	}
	public static String stringAlignment(String n,String m) {
		if(n.length()<=m.length()) {
			return m;
		}
	
		return DynamicProgramming.stringAlignmentHelper(m, n);
	}
	private static int penalty(char x,char y) {
		if (x==y) { return 0;}
		else if (y=='$') { return 4;}
		else { return 2;}
	}
	private static int cost(String x,String y) {
		if (x.length()!=y.length()) {
			return -1;
		}
		int cost=0;
		for(int i=0; i<x.length(); i++) {
			if (x.charAt(i)=='$' || y.charAt(i)=='$') { cost+=4;}
			if (x.charAt(i)==y.charAt(i)) { cost=cost;}
			else { cost+=2;}
		}
		return cost;
	}
	private static int stringAlignmentGreedy(String m, String n) {
		if (n.equals("")) {
			return 2*m.length();
		}
		return stringAlignmentGreedyHelper(n,m,n.length()-1,m.length()-1);
	}
	
	private static String stringAlignmentHelper(String m,String n) {
		Point[][] costArray=new Point[m.length()+1][n.length()+1];
		//Init Array
		for(int i=0; i<=m.length();i++) {
			for(int j=0; j<=n.length(); j++) {
				costArray[i][j]=new Point(i,j,0);
				costArray[i][j].alignParent=EditType.NONE;
			}
		}
		for(int i=0; i<=n.length(); i++) {
			costArray[0][i].value=i*4;
			if (i>0) {
				costArray[0][i].alignParent=EditType.INSERT;
			}
			else {
				costArray[0][i].alignParent=EditType.NONE;
			}
		}
		//Dynamic Programming Part
		
		for(int i=1; i<=m.length(); i++) {
			for(int j=1; j<=n.length(); j++) {
				int matchCost=costArray[i-1][j-1].value;
				if (m.charAt(i-1)!=n.charAt(j-1)) {
					matchCost+=2;
				}
				int addCost=costArray[i][j-1].value + 4;
				if (addCost<matchCost) {
					costArray[i][j].value=addCost;
					costArray[i][j].alignParent=EditType.INSERT;
				}
				else {
					costArray[i][j].value=matchCost;
					costArray[i][j].alignParent=EditType.MATCH;
				}
			}
		}
		//Get a path of edits made
		ArrayList<EditType> editList=new ArrayList<>();
		Point currPoint=costArray[m.length()][n.length()];

		int i=m.length();
		int j=n.length();
		while( currPoint.alignParent!=EditType.NONE) {
			editList.add(currPoint.alignParent);
			if (currPoint.alignParent==EditType.MATCH) {
				currPoint=costArray[currPoint.row-1][currPoint.column-1];
			}
			else {
				currPoint=costArray[currPoint.row][currPoint.column-1];

			}
		}
		Collections.reverse(editList);
		ArrayList<Character> returnval=new ArrayList<>();
		for(char c: m.toCharArray()) {
			returnval.add(c);
		}
		//Apply those edits to a function
		int cursor=0;
		StringBuilder minAlign=new StringBuilder();
		for(int k=0; k<editList.size(); k++) {
			EditType e=editList.get(k);
			if (e==EditType.INSERT) {
				minAlign.append('$');
				
			}
			else  {
				minAlign.append(m.charAt(cursor));
				cursor++;
			}
		}
		
		return minAlign.toString();


		
	
	}
	private static int stringAlignmentGreedyHelper(String m, String n, int i, int j) {
		if (i==j) {
			return 0;
		}
		if (i==0) {
			return 2*j;
		}
		if (j==0) {
			return 2*i;
		}
		else {
			if (m.charAt(i)==n.charAt(j)) {
				return stringAlignmentGreedyHelper(m,n, i-1,j-1);
			}
			else {
				return 2+ stringAlignmentGreedyHelper(m,n,i,j-1);
			}
		}
	}
	
	
	
	
}
