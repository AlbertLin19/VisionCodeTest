import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

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
				System.out.println("Cannot be loaded by loadlibrary, will try to load through relative path...");
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
			String path = "/home/pi/Desktop/CodeTest/libopencv_java330.so";
			try {
				System.load(path);
				System.out.println("Successful Load");
			} catch (Exception e2) {
				System.out.println("Cannot be loaded by absolute path to " + path + ", will try to load through relative path...");
				String libraryPath = "./";
				System.setProperty("java.library.path", libraryPath);
				Field sysPath = ClassLoader.class.getDeclaredField("sys_paths");
				sysPath.setAccessible(true); sysPath.set(null, null);
				System.out.println(System.getProperty("java.library.path"));
				System.loadLibrary("libopencv_java330.so");
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
		
		//Loading preferences and starting the video processing method
		Scanner tempScanner = new Scanner(System.in);
		System.out.println("Do you want to run off the preference file?\nY/N: ");
		if (tempScanner.next().equalsIgnoreCase("Y"))
		{
		String path = "./Preferences.txt";
		ArrayList<ArrayList<String>> Preferences = new ArrayList<ArrayList<String>>();
		try {
			System.out.println("Trying to read file at " + path);
			Preferences = reader(path);
		} catch (IOException e) {
			System.out.println("Cannot find path to " + path);
			System.out.println("Creating a default preference file at " + path);
			try {
				PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(path)));
				out.println("Pipeline: GripPipelineWithoutWPILibTape");
				out.println("Headless: true");
				out.println("Publishing: true");
				out.println("IpAddress: 10.65.60.2");
				out.println("DeviceIndex: 0");
				out.println("FPS: 5");
				out.println("Width: 640");
				out.println("Height: 360");
				out.close();
				try {
					System.out.println("Trying to read file at " + path);
					Preferences = reader(path);
				} catch (IOException e1) {
					System.out.println("Cannot find path to " + path);
					System.out.println("All settings will be the defaults hardcoded into the program.");
					e1.printStackTrace();
				}
			} catch (IOException e1) {
				System.out.println("Cannot create output file...");
				System.out.println("All settings will be the defaults hardcoded into the program.");
				e1.printStackTrace();
			}
		    
		}
		
		for (ArrayList<String> row : Preferences) {
			if (row.get(0).equalsIgnoreCase("Pipeline:")) {
				System.out.println("Setting pipeline to " + row.get(1));
				stream.setPipeline(row.get(1));
				
			} else if (row.get(0).equalsIgnoreCase("Headless:")) {
				System.out.println("Setting headless to " + row.get(1));
				stream.setIsHeadless(Boolean.parseBoolean(row.get(1)));
				
			} else if (row.get(0).equalsIgnoreCase("Publishing:")) {
				System.out.println("Setting publishing to " + row.get(1));
				stream.setIsPublishing(Boolean.parseBoolean(row.get(1)));
				
			} else if (row.get(0).equalsIgnoreCase("IpAddress:")) {
				System.out.println("Setting IpAddress to " + row.get(1));
				stream.setIpAddress(row.get(1));
				
			} else if (row.get(0).equalsIgnoreCase("DeviceIndex:")) {
				System.out.println("Setting DeviceIndex to " + row.get(1));
				stream.setDeviceIndex(Integer.parseInt(row.get(1)));
				
			} else if (row.get(0).equalsIgnoreCase("FPS:")) {
				System.out.println("Setting FPS to " + row.get(1));
				stream.setFps(Integer.parseInt(row.get(1)));
				
			} else if (row.get(0).equalsIgnoreCase("Width:")) {
				System.out.println("Setting Width to " + row.get(1));
				stream.setWidth(Integer.parseInt(row.get(1)));
				
			} else if (row.get(0).equalsIgnoreCase("Height:")) {
				System.out.println("Setting Height to " + row.get(1));
				stream.setHeight(Integer.parseInt(row.get(1)));
				
			}
		}
		}
		else
		{
			System.out.println("Type the name of the pipeline you wish to use: ");
			stream.setPipeline(tempScanner.next().trim());
			tempScanner.nextLine();
			
			System.out.println("Do you want to run in headless mode?\nY/N: ");
			stream.setIsHeadless(tempScanner.next().trim().equalsIgnoreCase("Y"));
			tempScanner.nextLine();
			
			System.out.println("Do you want to publish values?\nY/N: ");
			stream.setIsPublishing(tempScanner.next().trim().equalsIgnoreCase("Y"));
			tempScanner.nextLine();
			
			if (stream.isPublishing) {
				System.out.println("Type the ip address of the network table and hit enter: ");
				stream.setIpAddress(tempScanner.next().trim());
				tempScanner.nextLine();
			}
			
			System.out.println("Type the index of the video device (int) and hit enter: ");
			stream.setDeviceIndex(tempScanner.nextInt());
			tempScanner.nextLine();
			
			System.out.println("Enter the FPS (int) that you want, then hit enter: ");
			stream.setFps(tempScanner.nextInt());
			tempScanner.nextLine();
			
			if (!stream.isHeadless) {
				System.out.println("Enter the width (int) that you want, then hit enter: ");
				stream.setWidth(tempScanner.nextInt());
				tempScanner.nextLine();
				
				System.out.println("Enter the height (int) that you want, then hit enter: ");
				stream.setHeight(tempScanner.nextInt());
				tempScanner.nextLine();
				
			}
		}
		tempScanner.close();
		System.out.println("Running vision tracking...");
		stream.visionTrack();
		
		
	}
	
	/**
	 * This method returns a 2D arrayList of the tokens of the file whose path is specified in the call
	 * @param pathName the string of the path to the file that needs to be read
	 * @return a 2D arrayList with the tokens of the file
	 * @throws IOException 
	 */
	public static ArrayList<ArrayList<String>> reader(String pathName) throws IOException {
		ArrayList<ArrayList<String>> inputArray = new ArrayList<ArrayList<String>>();
	    BufferedReader in = new BufferedReader(new FileReader(pathName));
	    boolean hasMore = true;
	    int index = 0;
	    while (hasMore) {
	    	String nextLine = null;
	    	nextLine = in.readLine();
	    	
	    	if (nextLine!=null) {
	    		StringTokenizer stringTokenizer = new StringTokenizer(nextLine);
	    		inputArray.add(new ArrayList<String>());
	    		while (stringTokenizer.hasMoreTokens()) {
	    			inputArray.get(index).add(stringTokenizer.nextToken());
	    		}
	    		index++;
	    	} else {
	    		hasMore = false;
	    	}
	    	
	    }
	    in.close();
	    return inputArray;
	}
}
