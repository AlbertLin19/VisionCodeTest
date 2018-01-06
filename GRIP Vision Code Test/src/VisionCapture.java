import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.ScheduledExecutorService;
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
public class VisionCapture {
	
	//create the VideoCapture object
	VideoCapture capture;
	//mat used to store the input frames
	Mat frame;
	//timer object for the thread
	ScheduledExecutorService timer;
	//create an object of the class with the pipeline
	//perhaps change the methods to static so that you do not need to?
	Pipeline pipeline;
	//moment object used to store the ArrayList of the
	//contour report published by the pipeline
	Moments moments;
	//ArrayList object used to store the array of contour points
	ArrayList<MatOfPoint> contourArray;
	//ArrayList object used to store the array of moments
	ArrayList<Moments> momentsArray;
	//Point object to hold center point
	Point center;
	JFrame windowViewer;
	//create custom StreamPanel object to use for content pane...?
	StreamPanel streamPanel;
	boolean isHeadless, isPublishing;
	int deviceIndex, fps, width, height;
	String ipAddress;
	NetworkTablePublisher publisher;
	
	public VisionCapture() {
		capture = new VideoCapture();
		frame = new Mat();
		pipeline = new GripPipelineWithoutWPILibTape();
		moments = new Moments();
		windowViewer = new JFrame("Viewer");
		streamPanel = new StreamPanel();
		isHeadless = true;
		isPublishing = true;
		ipAddress = "10.65.60.2";
		deviceIndex = 0;
		fps = 5;
		width = 640;
		height = 360;
	}
	
	public void setPipeline(String pipelineClassName) {
		if (pipelineClassName.equalsIgnoreCase("GripPipelineWithoutWPILibTape")) {
			pipeline = new GripPipelineWithoutWPILibTape();
		} else if (pipelineClassName.equalsIgnoreCase("GripPipelineWithoutWPILibGreen")) {
			pipeline = new GripPipelineWithoutWPILibGreen();
		} else {
			System.out.println("No match found for name in code. Either add the new pipeline to the setPipeline method or change the settings to an appropriate name!");
			System.out.println("The pipeline to be used will remain unchanged.");
		}
	}
	
	public void setIsHeadless(boolean isHeadlessIn) {
		isHeadless = isHeadlessIn;
	}
	public void setIsPublishing(boolean isPublishingIn) {
		isPublishing = isPublishingIn;
	}
	public void setIpAddress(String ipAddressIn) {
		ipAddress = ipAddressIn;
	}
	public void setDeviceIndex(int deviceIndexIn) {
		deviceIndex = deviceIndexIn;
	}
	public void setFps(int fpsIn) {
		fps = fpsIn;
	}
	public void setWidth(int widthIn) {
		width = widthIn;
	}
	public void setHeight(int heightIn) {
		height = heightIn;
	}

	
	public void visionTrack() {
		
		this.capture.open(deviceIndex);
		capture.set(Videoio.CAP_PROP_FPS, fps);
		capture.set(Videoio.CAP_PROP_FRAME_WIDTH, width);
		capture.set(Videoio.CAP_PROP_FRAME_HEIGHT, height);
		System.out.println("FPS: " + capture.get(Videoio.CAP_PROP_FPS));
		System.out.println("Width: " + capture.get(Videoio.CAP_PROP_FRAME_WIDTH));
		System.out.println("Height: " + capture.get(Videoio.CAP_PROP_FRAME_HEIGHT));
		if (this.capture.isOpened()) {
			StreamThread streamThread = new StreamThread(capture);
			Thread myThread = new Thread(streamThread);
			myThread.start();
			
			//setting up the window
			if (!isHeadless) {
				this.openWindow();
			}
			
			//setting up NetworkTable Publisher
			if (isPublishing) {
				publisher = new NetworkTablePublisher(ipAddress);
			}
			
			while (true) {
					//assign frame (type Mat) with the next image in the capture stream
					frame = streamThread.getFrame();
					
					//send the frame to pipeline to process
					pipeline.process(frame);
					
					if (!isHeadless) {
						showResult(frame);
					}
					
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
						if (isPublishing) {
							publisher.publish((width/2-center.x)/(width/2), (height/2-center.y)/(height/2));
						}
						if (!isHeadless) {
							showResult(frame, (int) center.x, (int) center.y);
						}
						
					}
					System.out.println("frame done");
			}
			
		} else {
			System.out.println("uh oh, camera not found or camera cannot be opened");
		}
	}
	
	public void openWindow() {
		windowViewer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		windowViewer.setContentPane(streamPanel);
		windowViewer.setSize((int) capture.get(Videoio.CAP_PROP_FRAME_WIDTH), (int) capture.get(Videoio.CAP_PROP_FRAME_HEIGHT));
		windowViewer.setVisible(true);
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
