package workpleaseee;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;



import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;

public class img extends JPanel {
	private BufferedImage bufferedimage;
	private static final long serialVersionUID = 1L;
	public img(){
		super();
	}
	public static void main(String[] args){
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Mat im = Highgui.imread("C:/5.JPG");
		JFrame frame = new JFrame();
		img panel = new img();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
//		frame.setContentPane(panel);
		frame.add(panel);
//		frame.setVisible(true);
//		List<Mat> channel = new ArrayList<Mat>(3);
//		Mat Hue = new Mat();
//		Mat Sat = new Mat();
//		Mat val = new Mat();
		Mat circles = new Mat();
		Imgproc.cvtColor(im, im, Imgproc.COLOR_BGR2GRAY);
//		Core.split(im, channel);		
//		Hue = channel.get(0);
//		Sat = channel.get(1);
//		val = channel.get(2);
//		Imgproc.GaussianBlur(im, im, new Size(3,3), 0);
		Imgproc.Canny(im, im, 200, 100);
//		
		Imgproc.HoughCircles(im, circles, Imgproc.CV_HOUGH_GRADIENT, 2, im.height()/8, 200, 100, 0, 0);
		
		
		Imshow imgg = new Imshow("title");
		imgg.showImage(im);
		System.out.println(circles.cols());
		
		
		
	}
}


