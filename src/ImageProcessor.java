import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;

public class ImageProcessor {
	private Picture imagePicture;
	
	public ImageProcessor(String imageFile) {
		this.imagePicture=new Picture(imageFile);
		
	}
	
	public Picture reduceWidth(double x) {
		int newWidth=(int) Math.ceil(x*imagePicture.width());
		Picture currPicture=imagePicture;
		//Each Step gets one iteration
		while (currPicture.width()>newWidth) { 
			int[][][] pixelArray=this.buildPixelArray(currPicture); //m*n
			int[][] importanceMatrix=this.createImportanceMatrix(pixelArray); // O(m*n)
			ArrayList<Integer> cutPoints=DynamicProgramming.minCostVC(importanceMatrix); // O(m*n)
			ArrayList<ArrayList<Color>> newPixels=new ArrayList<>(); 
			//Create new Pixel Array, sans offending pixels
			for(int i=0; i< pixelArray.length; i++) {
				newPixels.add(new ArrayList<Color>()); //Add row
				//This only works if cutPoints removes the correct format 
				int avoidRow=cutPoints.get(2*i); //Think this'll always equal 2*i
				int avoidCol=cutPoints.get((2*i)+1);
				for (int j=0; j< pixelArray[0].length; j++) { // O(m*n)
					if (!(i==avoidRow && j==avoidCol)) {
						newPixels.get(i).add(new Color(pixelArray[i][j][0],pixelArray[i][j][1],pixelArray[i][j][2])); //Creates new Color
					}
				}
			}
			int h=newPixels.get(0).size();
			int w=newPixels.size();
			//Write pixels to picture
			Picture p=new Picture(currPicture.width()-1,currPicture.height()); //All black pixels
			for(int i=0; i<newPixels.size(); i++) { // O(m*n) 
				for(int j=0; j<newPixels.get(0).size(); j++) {
					p.set(j, i, newPixels.get(i).get(j));
				}
			}
			currPicture=p;
		}
		this.imagePicture=currPicture;
		currPicture.save("Output_File.png");;
		return currPicture;
	}
	/*
	 * Method to build array of pixels, represented as integers
	 */
	private int[][][] buildPixelArray(Picture p) {
		int[][][] finalMatrix=new int[p.height()][p.width()][3];
		for(int i=0; i<p.height();i++) {
			for(int j=0; j<p.width(); j++) {
				Color c=p.get(j, i);
				finalMatrix[i][j][0]=c.getRed();
				finalMatrix[i][j][1]=c.getGreen();
				finalMatrix[i][j][2]=c.getBlue();
			}
		}
		return finalMatrix;
	}
	//Described in book
	private int Distance(int[] p1,int[] p2) {
		int rDist=(int) Math.pow((p1[0]-p2[0]), 2);
		int gDist=(int) Math.pow((p1[1]-p2[1]), 2);
		int bDist=(int) Math.pow((p1[2]-p2[2]), 2);
		return rDist+gDist+bDist;
	}
	//Described in BOok
	private int Ximportance(int[][][] M,int row, int col) {
		int importance;
		int colLength=M[0].length-1;
		if (col==0) {
			importance=this.Distance(M[row][colLength], M[row][col+1] );
		}
		else if (col==colLength) {
			importance=this.Distance(M[row][col-1], M[row][0]);
		}
		else {
			importance = this.Distance(M[row][col-1], M[row][col+1]);
		}
		return importance;
	}
	//Described in Project Description
	private int YImportance(int[][][] M, int row, int col) {
		int importance;
		int rowLength=M.length-1;
		if (row ==0 ) {
			importance=this.Distance(M[rowLength][col], M[(row+1)][col]);
		}
		else if (row == rowLength) {
			importance=this.Distance(M[(row-1)][col], M[0][col]);
		}
		else {
			importance=this.Distance(M[(row-1)][col], M[(row+1)][col]);
		}
		return importance;
	}
	//Described in Project Description
	private int totalImportance(int[][][] M,int row,int col) {
		return this.Ximportance(M, row, col) + this.YImportance(M, row, col);
	}
	//Creates an Importance 
	private int[][] createImportanceMatrix(int[][][] M) {
		int[][] importanceMatrix=new int[M.length][M[0].length];
		for(int i=0; i<M.length; i++) {
			for (int j=0; j<M[0].length; j++) {
				importanceMatrix[i][j]=this.totalImportance(M, i, j);
			}
		}
		return importanceMatrix;
	}
}
