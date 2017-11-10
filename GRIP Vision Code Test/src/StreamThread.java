import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

public class StreamThread implements Runnable{
	
	VideoCapture capture;
	Mat frame = new Mat();
	
	public StreamThread(VideoCapture captureIn) {
		capture = captureIn;
	}
	public void run() {
		while (true) {
			//this might actually be more efficient than what is in the original capture class...
			capture.grab();
		}
	}
	
	public Mat getFrame() {
		capture.retrieve(frame);
		return frame;
	}

}
