import java.lang.reflect.Field;
import java.util.Scanner;

import org.opencv.core.Core;
/**
 * This class is the main class used to load the necessary libraries and initiate the vision code program.
 * @author Albert Lin
 *
 */
public class GripPipelineWithoutWPILibRunner {
	public static void main(String[] args) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, InterruptedException {
		//load the dll...?
		//System.out.println(System.getProperty("java.library.path"));
		
		//System.setProperty("java.library.path", ".");

	    //Field fieldSysPath = ClassLoader.class.getDeclaredField( "sys_paths" );
	    //fieldSysPath.setAccessible( true );
	    //fieldSysPath.set( null, null );
		
		//System.setProperty("java.library.path", "C:\\Users\\Albert\\OpenCV-3.3.0\\build\\java\\x64;");
		
		//System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		//String libraryPath = "C:\\Users\\Albert\\OpenCV-3.3.0\\build\\java\\x64";
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
			} finally {System.out.println("Exiting Load");} /* catch (Exception e2) {
			String libraryPath = "./";
			System.setProperty("java.library.path", libraryPath);
			Field sysPath = ClassLoader.class.getDeclaredField("sys_paths");
			sysPath.setAccessible(true); sysPath.set(null, null);
			System.out.println(System.getProperty("java.library.path"));
			System.loadLibrary("libopencv_java330.so");
			}*/
		} else {
			System.out.println(System.getProperty("os.name"));	
			System.out.println("Not Windows 10 or Linux");
			System.out.println("Trying to Load .dylib library...");
			try {
				System.load("/Volumes/Albert Lin/CodeTest/libopencv_java330.dylib");
				System.out.println("Successful Load");
			} finally {System.out.println("Exiting Load");}
		}
		//create a new object to use the video processing
		//methods within it
		//perhaps I should change the methods to static
		//so that I do not have to do this
		GripPipelineWithoutWPILibVideoCapture stream =
				new GripPipelineWithoutWPILibVideoCapture();
		
		//Start the video processing method
		Scanner tempScanner = new Scanner(System.in);
		System.out.println("Do you want to control a servo?\nY/N: ");
		if (tempScanner.next().equalsIgnoreCase("Y")) {
			System.out.println("Testing and initializing servo with the ServoControl class...");
			ServoControl.testingGpio();
			stream.videoCaptureTestWithServo();
		} else {
				stream.videoCaptureTest();
			}
		
		if (stream.doServoTrack) {
			System.out.println("Testing and initializing servo with the ServoControl class...");
			ServoControl.testingGpio();
		}
	}
}
