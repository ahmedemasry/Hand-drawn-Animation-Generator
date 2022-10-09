package whiteboard;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class TextTracker {
	
	private static BufferedImage image;
	private static BufferedImage PEN;
	private static boolean[][] imageMap;
	private static boolean[][] visited;
	private static ImagePrinter imagePrinter;
	private static int resolution = 0;
	private static int pointsCount = 0;
	private static char ch;
	private static Point startPoint;
	private static int CharPixels = 20; //dimensions of characters images in chars folder
	
	TextTracker(BufferedImage image, BufferedImage PEN, int pointsCount, int NOFrames){
		this.image = image;
		this.PEN = PEN;
		this.resolution = resolution;
		this.pointsCount = pointsCount;
		
		visited = new boolean[CharPixels][CharPixels];
		imageMap = new boolean[CharPixels][CharPixels];
		imagePrinter = new ImagePrinter(image, PEN, resolution, pointsCount, NOFrames);
	}
	
	public static void textTrack(char ch, int si, int sj, int size) {
		//initialization
		BufferedImage characterImage = null;
		try {
			characterImage = ImageIO.read(new File("chars/" + ch + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		imageMap = mapImage(characterImage);
		
		printImageMap(imageMap);
		
		//Tracking
		navigate(startPoint.x,startPoint.y,si,sj,size);
		
		
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

	private static boolean[][] mapImage(BufferedImage image){
		//image mapping
		boolean[][] map = new boolean[image.getHeight()][image.getWidth()];
		for(int i = 0; i < map.length; i++){
			for(int j = 0; j < map[0].length; j++){
				int alpha = ((image.getRGB(j, i))>>24) & 0xff;
				if(alpha >= 200){
					map[i][j] =  true;
				}
				if(((image.getRGB(j, i))>>16 & 0x000000FF) > 200){
					startPoint.x = j;
					startPoint.y = i;
				}
			}
		}
		return map;		
	}
	
	private static void navigate(int i, int j, int si, int sj, int size) {
		//print image
		visited[i][j] = true;
		
		if(i > 0 && i < imageMap.length-1 && j > 0 && j < imageMap[0].length-1){
			imagePrinter.printFrame(((si*resolution) + (i*(size/20))), ((sj*resolution) + (i*(size/20))));
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
					navigate(i+x, j+y, si, sj, size);
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
