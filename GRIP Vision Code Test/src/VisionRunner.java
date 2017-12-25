import java.lang.reflect.Field;
import java.util.Scanner;

import org.opencv.core.Core;
/**
 * This class is the main class used to load the necessary libraries and initiate the vision code program.
 * @author Albert Lin
 *
 */
public class VisionRunner {
	public static void main(String[] args) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, InterruptedException {
		if (System.getProperty("os.name").equals("Windows 10")) {
			try {
				System.out.println("This is Windows 10");
				System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
				System.out.println("Successful Load");
			} catch (Exception e) {
				String libraryPath = "./";
				System.setProperty("java.library.path", libraryPath);
				Field sysPath = ClassLoader.class.getDeclaredField("sys_paths");
				sysPath.setAccessible(true); sysPath.set(null, null);
				System.out.println(System.getProperty("java.library.path"));
				System.loadLibrary("opencv_java330.dll");
				System.out.println("Successful Load");
			} finally {System.out.println("Exiting Load");}
		} else if (System.getProperty("os.name").equals("Linux")) {
			System.out.println("Hopefully, this is Linux.");
			try {
				System.load("/home/pi/Desktop/CodeTest/libopencv_java330.so");
				System.out.println("Successful Load");
			} finally {System.out.println("Exiting Load");} 
		} else {
			System.out.println(System.getProperty("os.name"));	
			System.out.println("Not Windows 10 or Linux");
			System.out.println("Trying to Load .dylib library...");
			try {
				System.load("/Volumes/Albert Lin/CodeTest/libopencv_java330.dylib");
				System.out.println("Successful Load");
			} finally {System.out.println("Exiting Load");}
		}
		
		VisionCapture stream =
				new VisionCapture();
		
		//Start the video processing method
		Scanner tempScanner = new Scanner(System.in);
		
		System.out.println("Do you want to run in headless mode?\nY/N: ");
		stream.setIsHeadless(tempScanner.next().equalsIgnoreCase("Y"));
		System.out.println("Do you want to publish values?\nY/N: ");
		stream.setIsPublishing(tempScanner.next().equalsIgnoreCase("Y"));
		if (stream.isPublishing) {
			System.out.println("Type the ip address of the network table and hit enter: ");
			stream.setIpAddress(tempScanner.next());
		}
		System.out.println("Type the index of the video device (int) and hit enter: ");
		stream.setDeviceIndex(tempScanner.nextInt());
		System.out.println("Enter the FPS (int) that you want, then hit enter: ");
		stream.setFps(tempScanner.nextInt());
		
		if (!stream.isHeadless) {
			System.out.println("Enter the width (int) that you want, then hit enter: ");
			stream.setWidth(tempScanner.nextInt());
			System.out.println("Enter the height (int) that you want, then hit enter: ");
			stream.setHeight(tempScanner.nextInt());
		}
		
		tempScanner.close();
		System.out.println("Running vision tracking...");
		stream.visionTrack();
		
	}
}
