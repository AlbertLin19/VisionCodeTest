import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;
/**
 * This class starts the vision tracking stream and pipeline.
 * @author Albert Lin
 *
 */
public class GripPipelineWithoutWPILibVideoCapture {
	
	//create the VideoCapture object
	VideoCapture capture = new VideoCapture();
	
	//mat used to store the input frames
	Mat frame = new Mat();
	
	//timer object for the thread
	ScheduledExecutorService timer;
	
	//create an object of the class with the pipeline
	//perhaps change the methods to static so that you do not need to?
	GripPipelineWithoutWPILibPink pipeline = new GripPipelineWithoutWPILibPink();
	
	//moment object used to store the ArrayList of the
	//contour report published by the pipeline
	Moments moments = new Moments();
	
	//ArrayList object used to store the array of contour points
	ArrayList<MatOfPoint> contourArray;
	
	//ArrayList object used to store the array of moments
	ArrayList<Moments> momentsArray;
	
	//Point object to hold center point
	Point center;
	
	//creating the JFrame window object with title "Viewer"
	JFrame windowViewer = new JFrame("Viewer");
	
	//create custom StreamPanel object to use for content pane...?
	StreamPanel streamPanel = new StreamPanel();
	
	//boolean for controlling when to execute servo vision track code
	boolean doServoTrack = false;
	
	public void videoCaptureTest() {
		
		//setting up the window
		windowViewer.setVisible(true);
		windowViewer.setSize(1080, 720);
		windowViewer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		windowViewer.setContentPane(streamPanel);
		
		//open the camera with user input
		Scanner tempScanner = new Scanner(System.in);
		System.out.println("Do you want to control a servo?\nY/N: ");
		doServoTrack = tempScanner.next().equalsIgnoreCase("Y");
		System.out.println("Type the index of the video device (int) and hit enter: ");
		int videoIndex = tempScanner.nextInt();
		this.capture.open(videoIndex);
		System.out.println("Enter the FPS (int) that you want, then hit enter: ");
		int fpsIn = tempScanner.nextInt();
		capture.set(Videoio.CAP_PROP_FPS, fpsIn);
		System.out.println("Enter the width (int) that you want, then hit enter: ");
		int widthIn = tempScanner.nextInt();
		capture.set(Videoio.CAP_PROP_FRAME_WIDTH, widthIn);
		System.out.println("Enter the height (int) that you want, then hit enter: ");
		int heightIn = tempScanner.nextInt();
		capture.set(Videoio.CAP_PROP_FRAME_HEIGHT, heightIn);
		System.out.println("FPS: " + capture.get(Videoio.CAP_PROP_FPS));
		System.out.println("Width: " + capture.get(Videoio.CAP_PROP_FRAME_WIDTH));
		System.out.println("Height: " + capture.get(Videoio.CAP_PROP_FRAME_HEIGHT));
		if (this.capture.isOpened()) {
			Runnable frameGrabber = new Runnable() {
				public void run() {
					
					//assign frame (type Mat) with the next image in the capture stream
					frame = getFrame();
					
					//send the frame to pipeline to process
					pipeline.process(frame);
					
					showResult(frame);
					
					//give contourArray (ArrayList of MatOfPoint)
					//the output of the contour report
					contourArray = pipeline.filterContoursOutput();
					
					//give momentsArray (ArrayList of Moments)
					//a size that is the number of the points
					//in the contourArray (ArrayList of MatOfPoint)
					momentsArray = new ArrayList<Moments>(contourArray.size());
					
					for (int i = 0; i < contourArray.size(); i++) {
						
						//take the MatOfPoint at contourArray's index of i
						//and change it to a moment by method Imgproc.moments()
						//and assign it to the index i of the momentsArray
						momentsArray.add(i, Imgproc.moments(contourArray.get(i)));
						
						//assign moments (Moment) with the moment that
						//is at the index i of the momentsArray (the one just previously copied)
						moments = momentsArray.get(i);
						
						//get the point values and print the coordinates out
						center = new Point(moments.get_m10() / moments.get_m00(), moments.get_m01() / moments.get_m00());
						System.out.println("center x= " + center.x);
						System.out.println("center y= " + center.y);
						showResult(frame, (int) center.x, (int) center.y);
					}
					System.out.println("frame done");
				}
			};
			System.out.println("Please type in the fps that you want the thread to run at (int) and hit enter: ");
			int millisecondWaitTime = (1000/tempScanner.nextInt());
			tempScanner.close();
			System.out.println("The thread wait time is: " + millisecondWaitTime);
			System.out.println("This thread will run at " + 1/(millisecondWaitTime/1000.0) + " fps.");
			this.timer = Executors.newSingleThreadScheduledExecutor();
			this.timer.scheduleAtFixedRate(frameGrabber, 0, millisecondWaitTime, TimeUnit.MILLISECONDS);
		} else {
			System.out.println("uh oh, camera not found or camera cannot be opened");
		}
		
	}
	
	//reads the image next in the capture stream
	public Mat getFrame() {
		this.capture.read(frame);
		return frame;
	}
	
	//show the processed stream in a window
	public void showResult(Mat imgIn, int xIn, int yIn) {
		//Imgproc.resize(imgIn, imgIn, new Size(720, 480));
		MatOfByte matOfByte = new MatOfByte();
		Imgcodecs.imencode(".jpg", imgIn, matOfByte);
		byte[] byteArray = matOfByte.toArray();
		BufferedImage bufImage = null;
		try {
			InputStream in = new ByteArrayInputStream(byteArray);
			bufImage = ImageIO.read(in);
			streamPanel.setStream(bufImage, xIn, yIn);
			streamPanel.repaint();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void showResult(Mat imgIn) {
		//Imgproc.resize(imgIn, imgIn, new Size(720, 480));
		MatOfByte matOfByte = new MatOfByte();
		Imgcodecs.imencode(".jpg", imgIn, matOfByte);
		byte[] byteArray = matOfByte.toArray();
		BufferedImage bufImage = null;
		try {
			InputStream in = new ByteArrayInputStream(byteArray);
			bufImage = ImageIO.read(in);
			streamPanel.setStream(bufImage);
			streamPanel.repaint();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
