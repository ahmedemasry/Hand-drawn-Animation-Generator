package whiteboard;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImagePrinter {
	static BufferedImage frame,image,Pen,imgDrawing;
	static Graphics2D imgD;
	static int counter = 0;
	static int resolution = 0;
	static int NOFrames = 0;
	private static int pointsCount = 0;
	static int FileCounter = 0;
	static int x = 0;
	static int y = 0;
	
	public ImagePrinter(BufferedImage image, BufferedImage Pen, int resolution, int pointsCount, int NOFrames) {
		this.resolution = resolution;
		this.image = image;
		this.Pen = Pen;
		this.pointsCount = pointsCount;
		this.NOFrames = NOFrames;
		imgDrawing = new BufferedImage(
				image.getWidth(), image.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		
		
	}
	
	static void printFrame(int i, int j){
		counter++;
			frame = new BufferedImage(
					image.getWidth(), image.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
			imgD = imgDrawing.createGraphics();
			imgD.drawImage(eraseArea(i,j), x, y, null);
//		imgD.drawImage(fillLayer(i - resolution * 2), 0, 0, null);	
			Graphics2D g = frame.createGraphics();
			g.drawImage(imgDrawing, 0, 0, null);
			System.out.println("file :  " + FileCounter);
			
		if((counter % (pointsCount/(NOFrames))) == 0){			
			g.drawImage(Pen, j, i, null);
			g.dispose();
			saveToFile();
		}
		
		
	}
	
	private static BufferedImage fillLayer(int i) {
		if(i > 0){
			BufferedImage layer = image.getSubimage(0, 0, image.getWidth(), i);
			return layer;
		}
		return null;
	}

	static BufferedImage eraseArea(int i, int j){
		BufferedImage erased;
		if(i+resolution >= image.getHeight() || j+resolution >= image.getWidth()){
			x = j-(resolution);
			y = i-(resolution);
		}
		else if(i-(resolution) < 0 || j-(resolution) < 0){
			x = 0;
			y = 0;
		}
		else if(i+resolution < image.getHeight() && j+resolution < image.getWidth() && i > 0 && j > 0 ){
			x = j-(resolution);
			y = i-(resolution);
		}else{
			erased = image.getSubimage(0, 0, 1, 1);
		}

		erased = image.getSubimage(x, y, resolution*2, resolution*2);
		return erased;
	}
	
	static void saveToFile(){
		File f = new File("frames/" + (FileCounter++) + ".png");
		try {
			ImageIO.write(frame, "png", f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	static int getFileCounterValue(){
		System.out.println("file :  " + FileCounter);
		return FileCounter++;
	}
	
	
}
