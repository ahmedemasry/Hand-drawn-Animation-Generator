package whiteboard;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Tracker {
	
	private static BufferedImage image;
	private static BufferedImage PEN;
	private static boolean[][] imageMap;
	private static boolean[][] visited;
	private static ImagePrinter imagePrinter;
	private static int resolution = 0;
	private static int pointsCount = 0;
	
	Tracker(BufferedImage image, BufferedImage PEN, int resolution, int pointsCount,int NOFrames){
		this.image = image;
		this.PEN = PEN;
		this.resolution = resolution;
		this.pointsCount = pointsCount;
		visited = new boolean[(image.getHeight() / resolution)][image.getWidth() / resolution];
		imageMap = new boolean[visited.length][visited[0].length];
		imagePrinter = new ImagePrinter(image, PEN, resolution, pointsCount, NOFrames);
	}
	
	public static void track(boolean[][] subImagMap) {
		//initialization

		imageMap = subImagMap;
		printImageMap(imageMap);
		
		//Tracking
		for(int i = 0; i < imageMap.length; i++){
			for(int j = 0; j < imageMap[0].length; j++){
				if( !visited[i][j] && imageMap[i][j]){
					navigate(i,j);
				}
			}
		}
		
		
//		saveToFile(); //print the source image
		
	}
	
	private static void printImageMap(boolean[][] imageMap) {
		// //print imageMap
		for(int i = 0; i < imageMap.length; i++){
			for(int j = 0; j < imageMap[0].length; j++){
				if(imageMap[i][j] == true){
					System.out.print("* ");					
				}else{
					System.out.print("  ");
				}
			}
			System.out.println();
		}
		
	}

	private static void navigate(int i, int j) {
		//print image
		visited[i][j] = true;
		
		if(i > 0 && i < imageMap.length-1 && j > 0 && j < imageMap[0].length-1){
			imagePrinter.printFrame(i * resolution, j * resolution);
			for(int k = 0; k < 8; k++){
				int x=0; int y=0;
				switch (k){
				case 0: x=-1;y=0; break;
				case 1: x=-1;y=1; break;
				case 2: x=0;y=1; break;
				case 3: x=1;y=1; break;
				case 4: x=1;y=0; break;
				case 5: x=1;y=-1; break;
				case 6: x=0;y=-1; break;
				case 7: x=-1;y=-1; break;
				}
				if(!visited[i+x][j+y] && imageMap[i+x][j+y]){
					navigate(i+x, j+y);
					k = 0;
				}
			}
		}
		
	}
	
	static void printSourceImage(){
		File f = new File("frames/" + (imagePrinter.getFileCounterValue())+ ".png");
		try {
			ImageIO.write(image, "png", f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	


}
