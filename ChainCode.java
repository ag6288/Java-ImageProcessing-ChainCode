import java.io.*;
import java.util.Scanner;

public class ChainCode{
	static int[] neighborAry=new int[8];
	static String[] chain_data;
	static int currentCC;
	static point[] neighborCoord = new point[8];
	
	public static void main(String[] args){
		CCimage image = new CCimage(args[0]);
		CCproperty pp = new CCproperty(args[1]);
		chain(image, pp);
		try{
			BufferedWriter outFile = new BufferedWriter(new FileWriter(args[2]));
			for(int i=1; i<=pp.maxCC; i++){
				outFile.write(chain_data[i]);
				outFile.newLine();
			}
			outFile.close();
		}
		catch(Exception e){System.out.println(e);}
		try{
			BufferedWriter outFile = new BufferedWriter(new FileWriter(args[3]));
			outFile.write("for debuggings");
			outFile.newLine();
			outFile.close();
		}
		catch(Exception e){System.out.println(e);}	
		System.out.println("All work done!");
	}
	
	private static void chain(CCimage image, CCproperty pp) {
		chain_data=new String[pp.maxCC+1];
		int minRowOffset, maxRowOffset, minColOffset, maxColOffset, lastQ, nextQ, Pchain;
		for(int i=0; i<8; i++)
			neighborCoord[i] = new point(); 
		int startRow, startCol;
		int nextDirTable[]={6,0,0,2,2,4,4,6};
		for(int i=0; i<pp.maxCC; i++){
			currentCC=i+1;
			minRowOffset=pp.property[i][2];
			maxRowOffset=pp.property[i][4];
			minColOffset=pp.property[i][3];
			maxColOffset=pp.property[i][5];
			startRow=minRowOffset;
			startCol=minColOffset;
			while(image.zeroFramedAry[startRow][startCol]!=currentCC)
				++startCol;
			chain_data[currentCC]=Integer.toString(startRow)+" "+Integer.toString(startCol)+" "+Integer.toString(currentCC)+" ";
			point startP = new point(startRow, startCol);
			point currentP = startP;
			point nextP = new point();
			lastQ=4;
			while(!nextP.equal(startP)){
				loadNeighbors(image, currentP.row, currentP.col);
				nextQ=(lastQ+1)%8;
				Pchain = findNextP(currentP, nextQ);
				nextP = neighborCoord[Pchain];
				chain_data[currentCC]+=Integer.toString(Pchain)+" ";
				currentP=nextP;
				lastQ=nextDirTable[(Pchain+7)%8];
			}
		}
	}
	
	private static int findNextP(point p, int q) {
		int row = p.row;
		int col = p.col;
		loadNeighborCoord(row, col);
		int return_Pchain=0;
		for(int i=0; i<8; i++)
			if(neighborAry[(q+i)%8]==currentCC)
				return (q+i)%8;
		return return_Pchain;
	}

	private static void loadNeighborCoord(int row, int col){
		neighborCoord[0].row = row;
		neighborCoord[0].col = col+1;
		neighborCoord[1].row = row-1;
		neighborCoord[1].col = col+1;
		neighborCoord[2].row = row-1;
		neighborCoord[2].col = col;
		neighborCoord[3].row = row-1;
		neighborCoord[3].col = col-1;
		neighborCoord[4].row = row;
		neighborCoord[4].col = col-1;
		neighborCoord[5].row = row+1;
		neighborCoord[5].col = col-1;
		neighborCoord[6].row = row+1;
		neighborCoord[6].col = col;
		neighborCoord[7].row = row+1;
		neighborCoord[7].col = col+1;
	}

	private static void loadNeighbors(CCimage image, int row, int col) {
		neighborAry[0]=image.zeroFramedAry[row][col+1];
		neighborAry[1]=image.zeroFramedAry[row-1][col+1];
		neighborAry[2]=image.zeroFramedAry[row-1][col];
		neighborAry[3]=image.zeroFramedAry[row-1][col-1];
		neighborAry[4]=image.zeroFramedAry[row][col-1];
		neighborAry[5]=image.zeroFramedAry[row+1][col-1];
		neighborAry[6]=image.zeroFramedAry[row+1][col];
		neighborAry[7]=image.zeroFramedAry[row+1][col+1];
	}
}

class CCimage{
	int [][]zeroFramedAry;
	private int numRows, numCols, minVal, maxVal;
	public CCimage(String input_fileName){
		int row = 1, col = 1, order=0;
		Scanner inFile = null;
		try {
			inFile = new Scanner(new File(input_fileName));
			while(inFile.hasNext()){
				order++;
				if(order==1) numRows=inFile.nextInt();
				else if(order==2){
					numCols=inFile.nextInt();
					zeroFramedAry = new int[numRows+2][numCols+2];
					for(int i=0; i<numRows+2; i++) {
						zeroFramedAry[i][0] = 0;
						zeroFramedAry[i][numCols+1] = 0;
					}
					for(int j=0; j<numCols+2; j++) {
						zeroFramedAry[0][j] = 0;
						zeroFramedAry[numRows+1][j] = 0;
					}
				}
				else if(order==3) minVal=inFile.nextInt();
				else if(order==4) maxVal=inFile.nextInt();
				else{
					zeroFramedAry[row][col++] = inFile.nextInt();
					if(col>numCols) {
						row++;
						col=1;
					}
				}
			}
			inFile.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}

class CCproperty{
	int maxCC;
	int[][] property;
	public CCproperty(String input_fileName){
		Scanner inFile = null;
		try {
			String[] tempPP = new String[99];
			inFile = new Scanner(new File(input_fileName));
            while (inFile.hasNextLine()) {
            	tempPP[maxCC++]=inFile.nextLine();
            }
            property=new int[maxCC][6];
            for(int i=0; i<maxCC; i++){
            	String[] tempString = tempPP[i].split(" ");
            	for(int j=0; j<6; j++)
            		property[i][j]=Integer.parseInt(tempString[j]);
            }
			inFile.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}


class point{
	int row, col;
	public point(){row=0; col=0;}
	public point(int r, int l){row=r; col=l;}
	
	boolean equal(point p){
		if(this.row==p.row && this.col==p.col)
			return true;
		else
			return false;
	}
}



