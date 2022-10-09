package whiteboard;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageBreaker {
	
	private static BufferedImage image;
	private static BufferedImage PEN;
	private static boolean[][] imageMap;
	private static boolean[][] visited;
	private static int resolution = 0;
	private static int counter = 0;
	private static int pointsCount = 0;
	
	public static void main(String[] args) {
		//initialization
				try {
					image = ImageIO.read(new File("TrackerTest.png"));
					PEN = ImageIO.read(new File("Pen.png"));
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				//Resolution
				int NofUnits = 20;
				resolution = (image.getWidth() / NofUnits);
				visited = new boolean[(image.getHeight() / resolution)][NofUnits];
				imageMap = new boolean[visited.length][NofUnits];
				
				//Duration
				float seconds = 4;
				float sourceSeconds = 1; // how much time for still image
				float fps = 30;
				int NOFrames = (int)(fps * seconds);
				
				
				//image mapping
				for(int i = 0; i < visited.length; i++){
					for(int j = 0; j < visited[0].length; j++){
						visited[i][j] = false;
						imageMap[i][j] = false;
						if(hasPixels(i*resolution, j*resolution)){
							imageMap[i][j] =  true;
							pointsCount++;
						}
					}
				}
				
				
				//Tracking
				Tracker tracker = new Tracker(image, PEN, resolution, pointsCount, NOFrames);
//				tracker.track(imageMap);
				
				//up first
				for(int i = 0; i < imageMap.length; i++){
					for(int j = 0; j < imageMap[0].length; j++){
						if( !visited[i][j] && imageMap[i][j]){
							//
							boolean[][] subImageMap = new boolean[imageMap.length][imageMap[0].length];
							subImageMap = connectedMap(i, j, subImageMap);
							tracker.track(subImageMap);
							counter++;
						}
					}
				}
				
//				//left first
//				for(int j = 0; j < imageMap[0].length; j++){
//					for(int i = 0; i < imageMap.length; i++){
//						if( !visited[i][j] && imageMap[i][j]){
//							//
//							boolean[][] subImageMap = new boolean[imageMap.length][imageMap[0].length];
//							subImageMap = connectedMap(i, j, subImageMap);
//							tracker.track(subImageMap);
//							counter++;
//						}
//					}
//				}
				
				for(int i = 0; i < (int)(sourceSeconds*fps); i++){
					tracker.printSourceImage();
				}
				System.out.println("COUNTER : " + counter);
				
	}
	
	
	static boolean[][] connectedMap(int i, int j, boolean[][] subImageMap){
		
		visited[i][j] = true;
		subImageMap[i][j] = true;
//		System.out.println(visited[i][j] + " : " + i + "," + j);
		if(i > 0 && i < imageMap.length-1 && j > 0 && j < imageMap[0].length-1){
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
					subImageMap = connectedMap(i+x, j+y, subImageMap);
					k=0;
//					break;
				}
			}
		}
		
		return subImageMap;
	}
	
	
	
	private static void printImageMap(boolean[][] imageMap) {
		// //print imageMap
		for(int i = 0; i < visited.length; i++){
			for(int j = 0; j < visited[0].length; j++){
				if(imageMap[i][j] == true){
					System.out.print("* ");					
				}else{
					System.out.print("  ");
				}
			}
			System.out.println();
		}
		
	}
	
	
	
	static boolean hasPixels(int pi, int pj){
		int r = resolution;
		for(int i = pi; i < pi+r; i++){
			for(int j = pj; j < pj+r; j++){
				int alpha = ((image.getRGB(j, i))>>24) & 0xff;
				if(alpha >= 200){
					return true;
				}
			}
		}
		return false;
	}
	
	static int pointsCount(){
		int count = 0;
		for(int j = 0; j < imageMap[0].length; j++){
			for(int i = 0; i < imageMap.length; i++){
				if(imageMap[i][j] == true){
					count++;
				}
			}
		}
		return count;
	}
	
	
}
