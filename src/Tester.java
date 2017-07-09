import java.awt.Color;
import java.util.Random;

public class Tester {
	public static void main(String[] args) {
		Picture currPic;
		/*
		for(int i=1; i<=20; i++) {
			ImageProcessor ip=new ImageProcessor("images/"+i+".jpg");
			System.out.println("Image: "+i);
			currPic=ip.reduceWidth(.05*i);
			currPic.save("modified_images/"+i+"_modified.jpg");
		}
		*/
		/*
		Random Rand=new Random();
		currPic=new Picture(2000,2000);
		for(int i=0; i<currPic.width(); i++) {
			for(int j=0; j<currPic.height(); j++) {
				if (!(i>= 600 && i<=800)) {
					Color c=new Color(Rand.nextInt(256),Rand.nextInt(256),Rand.nextInt(256));
					currPic.set(i, j, c);
				}
				else {
					Color c=new Color(255,255,255);
					currPic.set(i, j, c);
				}

				
			}
		}
		currPic.save("images/21.jpg");
		*/
		ImageProcessor ip=new ImageProcessor("images/12.jpg");
		ip.reduceWidth(.60).save("modified_images/12_modified.jpg");

		
	}
}
