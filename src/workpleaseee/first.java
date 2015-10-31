package workpleaseee;

import static java.lang.Math.abs;
import static java.lang.Math.atan;
import static java.lang.Math.sqrt;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.AWTException;
//import java.awt.List;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;



import java.util.ArrayList;

import javax.swing.*;

import org.opencv.core.Core;
import org.opencv.core.CvException;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;

public class first extends JPanel{
	static short leftx;
	static short lefty;
	static short rightx;
	static short righty;
	byte pixVal;				//	0-255
	static float angle;
	static short distance;
	static short midPointx;
	static short midPointy;
	static boolean broken = false;
	private BufferedImage bufferedimage;
//	static int c;
	private static final long serialVersionUID = 1L;
	public first(){
		super();
	}
	public void setImage(BufferedImage temp){
		bufferedimage = temp;
		return;
	}
	public BufferedImage getImage(){
		return bufferedimage;
	}
	
	public static BufferedImage matToBufferedImage(Mat image) throws AWTException {
		int cols = image.cols();
		Robot robot = new Robot();
		robot.setAutoDelay(40);
	    int rows = image.rows();
	    int elemSize = (int)image.elemSize();  
	    byte[] data = new byte[cols * rows * elemSize];  
	    int type;  
	    image.get(0, 0, data);  
	    switch (image.channels()) {  
	    	case 1:
	    		type = BufferedImage.TYPE_BYTE_GRAY;  
	    		break;  
	    	case 3:  
	    		type = BufferedImage.TYPE_3BYTE_BGR;  
	        	//bgr to rgb  
	    		byte b;  
	    		for(int i=0; i<data.length; i=i+3) {  
	    			b = data[i];  
	    			data[i] = data[i+2];  
	    			data[i+2] = b;  
	     		}
	     		break;  
	     	default:  
	     		return null;  
	     }
	     BufferedImage image2 = new BufferedImage(cols, rows, type);  
	     image2.getRaster().setDataElements(0, 0, cols, rows, data);  
	     return image2;
	}
	
	public void paintComponent(Graphics g){
		BufferedImage temp=getImage();
		if( temp != null)
			g.drawImage(temp,10,10,temp.getWidth(),temp.getHeight(), this);
	}
	
	public static void main(String[] args) throws AWTException{
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		byte angle = 0;
		VideoCapture capture = new VideoCapture(0);
		JFrame frame = new JFrame();
		first panel = new first();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Robot robot = new Robot();
//		robot.setAutoDelay(500);
//		frame.setContentPane(panel);
//		frame.setVisible(true);
		Mat webcam_image = new Mat();
		List<Mat> channel = new ArrayList<Mat>(3);
		BufferedImage temp;
		Mat Hue = new Mat();
		Mat Sat = new Mat();
		Mat val = new Mat();
		Mat circles = new Mat();
		if(capture.isOpened()){
			while(true){
				capture.read(webcam_image);
//				frame.setSize(webcam_image.width()+40,webcam_image.height()+60);
				if(!webcam_image.empty()){
					Imgproc.cvtColor(webcam_image, webcam_image, Imgproc.COLOR_BGR2HSV);
//					Imgproc.cvtColor(webcam_image, webcam_image, Imgproc.COLOR_BGR2GRAY);
					Core.split(webcam_image, channel);
					Hue = channel.get(0);
					Sat = channel.get(1);
					val = channel.get(2);
					Imgproc.GaussianBlur(webcam_image, webcam_image, new Size(29,29), 0,0);
					Core.inRange(webcam_image, new Scalar(100,100,50), new Scalar(130,255,255), webcam_image);
//					Imgproc.Canny(webcam_image, webcam_image, 50, 100);
					
//					System.out.println(Integer.parseInt(webcam_image.col(20).row(20).dump().substring(1, webcam_image.col(20).row(20).dump().length() - 1)));
					
//					Imgproc.HoughCircles(webcam_image, circles, Imgproc.CV_HOUGH_GRADIENT, 2, webcam_image.height()/25, 50, 200, 0, 0);
					//System.out.println(circles.cols());
					
					for(short i=10;i<(short)webcam_image.cols()-10;i+=10){
						for(short j=10;j<(short)(webcam_image.rows()-10);j+=10){
//							c = Integer.parseInt(webcam_image.col(i).row(j).dump().substring(1, webcam_image.col(i).row(j).dump().length() - 1));
							if(Integer.parseInt(webcam_image.col(i).row(j).dump().substring(1, webcam_image.col(i).row(j).dump().length() - 1))==255){
								lefty= j;
								leftx= i;
								broken = true;
								break;
							}
						}
						if(broken==true){
							break;
						}
					}
					
					broken = false;
					for(short i=(short)(webcam_image.cols()-10);i>10;i-=10){
						for(short j=10;j<(short)(webcam_image.rows()-10);j+=10){
//							c = Integer.parseInt(webcam_image.col(i).row(j).dump().substring(1, webcam_image.col(i).row(j).dump().length() - 1));
							if(Integer.parseInt(webcam_image.col(i).row(j).dump().substring(1, webcam_image.col(i).row(j).dump().length() - 1))==255){
								righty= j;
								rightx= i;
								broken = true;
								break;
							}
						}
						if(broken==true){
							break;
						}
					}
					broken=false;
//					System.out.println(leftx+", "+lefty+"   "+rightx+","+righty);
					
					distance = (short) Math.sqrt(Math.abs((righty-lefty)*(righty-lefty)-(rightx-leftx)*(rightx-leftx)));
					if(leftx==rightx){
						angle=90;
					}
					else
						angle = (byte) Math.toDegrees(atan((float)(lefty-righty)/(leftx-rightx)));
					distance = (short) sqrt(abs((righty-lefty)*(righty-lefty)-(rightx-leftx)*(rightx-leftx)));
//					System.out.println(distance+"   "+angle);
					/*if(circles.cols()==2){
						
						int rows = circles.rows(); 
						int elemSize = (int)circles.elemSize();
						float[] points1 = new float[rows * elemSize/4];
						float[] points2 = new float[rows * elemSize/4];
						circles.get(0, 0, points1);
						circles.get(0, 1, points2);
							
						try {
							if(points1[0]<points2[0]){
								try {
//									System.out.println(points1[0]+","+points1[1]+"   "+points2[0]+","+points2[1]+" rad1: "+points1[2]+" rad2: "+points2[2]);
									angle = (byte) Math.toDegrees(atan((byte)(points1[1]-points2[1])/(points1[0]-points2[0])));
//									System.out.println(angle);
									
								} catch (CvException e) {
									robot.keyRelease(KeyEvent.VK_W);
									robot.keyRelease(KeyEvent.VK_D);
									robot.keyRelease(KeyEvent.VK_A);
								} catch(ArrayIndexOutOfBoundsException r){
									robot.keyRelease(KeyEvent.VK_W);
									robot.keyRelease(KeyEvent.VK_D);
									robot.keyRelease(KeyEvent.VK_A);
								}
							}
							else{
								try {
//									System.out.println(points2[0]+","+points2[1]+"   "+points1[0]+","+points1[1]+" rad1: "+points2[2]+" rad2: "+points1[2]);
									angle = (byte) Math.toDegrees(atan((byte)(points2[1]-points1[1])/(points2[0]-points1[0])));
//									System.out.println(angle);
								} catch (CvException e) {
									robot.keyRelease(KeyEvent.VK_W);
									robot.keyRelease(KeyEvent.VK_D);
									robot.keyRelease(KeyEvent.VK_A);
								} catch(ArrayIndexOutOfBoundsException r){
									robot.keyRelease(KeyEvent.VK_W);
									robot.keyRelease(KeyEvent.VK_D);
									robot.keyRelease(KeyEvent.VK_A);
								}
							}
						} catch(ArrayIndexOutOfBoundsException r){
//							System.out.println("");
						}
						
					}*/
					
					if(!(angle<-10) && !(angle>10) && ((distance<500 && distance>250) || (distance>80 && distance<200))){
						robot.keyRelease(KeyEvent.VK_A);
						robot.keyRelease(KeyEvent.VK_D);
						robot.keyRelease(KeyEvent.VK_S);
//						robot.setAutoDelay(50);
						if(distance>250){
							robot.keyPress(KeyEvent.VK_S);
						}
						if(distance<200){
							robot.keyPress(KeyEvent.VK_W);
						}
							
						if(distance <130){
							robot.keyPress(KeyEvent.VK_ALT);
						}
						robot.delay(100);
						robot.keyRelease(KeyEvent.VK_W);
						robot.keyRelease(KeyEvent.VK_ALT);
					}
					if(angle<=-10 && angle>-25 && ((distance<500 && distance>250) || (distance>80 && distance<200))){
//						System.out.println("right1");
						robot.keyRelease(KeyEvent.VK_A);
						robot.keyRelease(KeyEvent.VK_D);
						robot.keyRelease(KeyEvent.VK_S);
						robot.keyRelease(KeyEvent.VK_W);
//						robot.setAutoDelay(50);
						if(distance>250){
							robot.keyPress(KeyEvent.VK_S);
							robot.keyPress(KeyEvent.VK_D);
							robot.keyPress(KeyEvent.VK_S);
						}
						if(distance<200){
							robot.keyPress(KeyEvent.VK_W);
							robot.keyPress(KeyEvent.VK_D);
							robot.keyPress(KeyEvent.VK_W);
						}
						if(distance <130)
							robot.keyPress(KeyEvent.VK_ALT);
						robot.delay(50);
						robot.keyRelease(KeyEvent.VK_D);
						robot.keyRelease(KeyEvent.VK_W);
						robot.keyRelease(KeyEvent.VK_S);
						robot.keyRelease(KeyEvent.VK_ALT);
					}
					if(angle>=10 && angle<25 && ((distance<500 && distance>250) || (distance>80 && distance<200))){
//						System.out.println("left1");
						robot.keyRelease(KeyEvent.VK_A);
						robot.keyRelease(KeyEvent.VK_D);
						robot.keyRelease(KeyEvent.VK_S);
						robot.keyRelease(KeyEvent.VK_W);
//						robot.setAutoDelay(50);
						if(distance>250){
							robot.keyPress(KeyEvent.VK_S);
							robot.keyPress(KeyEvent.VK_A);
							robot.keyPress(KeyEvent.VK_S);
						}
						if(distance<200){
							robot.keyPress(KeyEvent.VK_W);
							robot.keyPress(KeyEvent.VK_A);
							robot.keyPress(KeyEvent.VK_W);
						}
						if(distance <130)
							robot.keyPress(KeyEvent.VK_ALT);
						robot.delay(50);
						robot.keyRelease(KeyEvent.VK_A);
						robot.keyRelease(KeyEvent.VK_W);
						robot.keyRelease(KeyEvent.VK_S);
						robot.keyRelease(KeyEvent.VK_ALT);
					}
					if(angle<=-25 && angle >-33 && ((distance<500 && distance>250) || (distance>80 && distance<200))){
//						System.out.println("right2");
						robot.keyRelease(KeyEvent.VK_A);
						robot.keyRelease(KeyEvent.VK_D);
						robot.keyRelease(KeyEvent.VK_S);
						robot.keyRelease(KeyEvent.VK_W);
//						robot.setAutoDelay(50);
						if(distance>250){
							robot.keyPress(KeyEvent.VK_S);
							robot.keyPress(KeyEvent.VK_D);
							robot.keyPress(KeyEvent.VK_S);
						}
						if(distance<200){
							robot.keyPress(KeyEvent.VK_W);
							robot.keyPress(KeyEvent.VK_D);
							robot.keyPress(KeyEvent.VK_W);
//							robot.keyPress(KeyEvent.VK_SPACE);
						}
						if(distance <130)
							robot.keyPress(KeyEvent.VK_ALT);
						robot.delay(100);
						robot.keyRelease(KeyEvent.VK_D);
						robot.keyRelease(KeyEvent.VK_W);
//						robot.keyRelease(KeyEvent.VK_SPACE);
						robot.keyRelease(KeyEvent.VK_S);
						robot.keyRelease(KeyEvent.VK_ALT);
					}
					if(angle>=25 && angle <33 && ((distance<500 && distance>250) || (distance>80 && distance<200))){
//						System.out.println("left2");
						robot.keyRelease(KeyEvent.VK_A);
						robot.keyRelease(KeyEvent.VK_D);
						robot.keyRelease(KeyEvent.VK_S);
						robot.keyRelease(KeyEvent.VK_W);
//						robot.setAutoDelay(50);
						if(distance>250){
							robot.keyPress(KeyEvent.VK_S);
							robot.keyPress(KeyEvent.VK_A);
							robot.keyPress(KeyEvent.VK_S);
						}
						if(distance<200){
							robot.keyPress(KeyEvent.VK_W);
							robot.keyPress(KeyEvent.VK_A);
							robot.keyPress(KeyEvent.VK_W);
//							robot.keyPress(KeyEvent.VK_SPACE);
						}
						if(distance <130)
							robot.keyPress(KeyEvent.VK_ALT);
						robot.delay(100);
						robot.keyRelease(KeyEvent.VK_A);
						robot.keyRelease(KeyEvent.VK_W);
						robot.keyRelease(KeyEvent.VK_S);
//						robot.keyRelease(KeyEvent.VK_SPACE);
						robot.keyRelease(KeyEvent.VK_ALT);
					}
					
					
					if(angle<=-33 && angle>-60 && ((distance<500 && distance>250) || (distance>80 && distance<200))){
//						System.out.println("right3");
						robot.keyRelease(KeyEvent.VK_A);
						robot.keyRelease(KeyEvent.VK_D);
						robot.keyRelease(KeyEvent.VK_S);
						robot.keyRelease(KeyEvent.VK_W);
//						robot.setAutoDelay(50);
						if(distance>250){
//							robot.keyPress(KeyEvent.VK_S);
							robot.keyPress(KeyEvent.VK_D);
							robot.keyPress(KeyEvent.VK_S);
						}
						if(distance<200){
//							robot.keyPress(KeyEvent.VK_W);
							robot.keyPress(KeyEvent.VK_D);
							robot.keyPress(KeyEvent.VK_W);
							robot.keyPress(KeyEvent.VK_SPACE);
						}
						if(distance <130)
							robot.keyPress(KeyEvent.VK_ALT);
						robot.delay(100);
						robot.keyRelease(KeyEvent.VK_D);
						robot.keyRelease(KeyEvent.VK_W);
						robot.keyRelease(KeyEvent.VK_SPACE);
						robot.keyRelease(KeyEvent.VK_S);
						robot.keyRelease(KeyEvent.VK_ALT);
					}
					if(angle>=33 && angle<60 && ((distance<500 && distance>250) || (distance>80 && distance<200))){
//						System.out.println("left3");
						robot.keyRelease(KeyEvent.VK_A);
						robot.keyRelease(KeyEvent.VK_D);
						robot.keyRelease(KeyEvent.VK_S);
						robot.keyRelease(KeyEvent.VK_W);
//						robot.setAutoDelay(50);
						if(distance>250){
//							robot.keyPress(KeyEvent.VK_S);
							robot.keyPress(KeyEvent.VK_A);
							robot.keyPress(KeyEvent.VK_S);
						}
						if(distance<200){
//							robot.keyPress(KeyEvent.VK_W);
							robot.keyPress(KeyEvent.VK_A);
							robot.keyPress(KeyEvent.VK_W);
							robot.keyPress(KeyEvent.VK_SPACE);
						}
						if(distance <130)
							robot.keyPress(KeyEvent.VK_ALT);
						robot.delay(100);
						robot.keyRelease(KeyEvent.VK_A);
						robot.keyRelease(KeyEvent.VK_W);
						robot.keyRelease(KeyEvent.VK_SPACE);
						robot.keyRelease(KeyEvent.VK_S);
						robot.keyRelease(KeyEvent.VK_ALT);
					}
					if(angle<=-60 && angle >-88 && ((distance<500 && distance>250) || (distance>80 && distance<200))){
//						System.out.println("right4");
						robot.keyRelease(KeyEvent.VK_A);
						robot.keyRelease(KeyEvent.VK_D);
						robot.keyRelease(KeyEvent.VK_S);
						robot.keyRelease(KeyEvent.VK_W);
//						robot.setAutoDelay(50);
						if(distance>250){
//							robot.keyPress(KeyEvent.VK_S);
							robot.keyPress(KeyEvent.VK_D);
//							robot.keyPress(KeyEvent.VK_S);
						}
						if(distance<200){
//							robot.keyPress(KeyEvent.VK_W);
							robot.keyPress(KeyEvent.VK_D);
							robot.keyPress(KeyEvent.VK_SPACE);
//							robot.keyPress(KeyEvent.VK_W);
						}
						if(distance <130)
							robot.keyPress(KeyEvent.VK_ALT);
						robot.delay(180);
						robot.keyRelease(KeyEvent.VK_D);
						robot.keyRelease(KeyEvent.VK_SPACE);
						robot.keyRelease(KeyEvent.VK_W);
						robot.keyRelease(KeyEvent.VK_S);
						robot.keyRelease(KeyEvent.VK_ALT);
					}
					if(angle>=60 && angle <88 && ((distance<500 && distance>250) || (distance>80 && distance<200))){
//						System.out.println("left4");
						robot.keyRelease(KeyEvent.VK_A);
						robot.keyRelease(KeyEvent.VK_D);
						robot.keyRelease(KeyEvent.VK_S);
						robot.keyRelease(KeyEvent.VK_W);
//						robot.setAutoDelay(50);
						if(distance>250){
//							robot.keyPress(KeyEvent.VK_S);
							robot.keyPress(KeyEvent.VK_A);
//							robot.keyPress(KeyEvent.VK_S);
						}
						if(distance<200){
//							robot.keyPress(KeyEvent.VK_W);
							robot.keyPress(KeyEvent.VK_A);
							robot.keyPress(KeyEvent.VK_SPACE);
//							robot.keyPress(KeyEvent.VK_W);
						}
						if(distance <130)
							robot.keyPress(KeyEvent.VK_ALT);
						robot.delay(180);
						robot.keyRelease(KeyEvent.VK_A);
						robot.keyRelease(KeyEvent.VK_W);
						robot.keyRelease(KeyEvent.VK_S);
						robot.keyRelease(KeyEvent.VK_SPACE);
						robot.keyRelease(KeyEvent.VK_ALT);
					}
					
					/////distance//////2d
					
					
					
					else{
						robot.keyRelease(KeyEvent.VK_W);
						robot.keyRelease(KeyEvent.VK_D);
						robot.keyRelease(KeyEvent.VK_A);
						robot.keyRelease(KeyEvent.VK_A);
					}
					
					
					
//					temp = matToBufferedImage(webcam_image);
//					panel.setImage(temp);
//					panel.repaint();
				}
				else{
					System.out.println("damnnn!!!!!!!!");
					break;
				}
			}
		}
	}
}
