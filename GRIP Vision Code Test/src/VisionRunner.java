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
		Scanner tempScanner = new Scanner(System.in);
		
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
			System.out.println("Type in the absolute path to the OpenCV library and hit the enter key: ");
			String path = tempScanner.nextLine();
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
		
		tempScanner.close();
		
		//Loading preferences and starting the video processing method
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
				out.println("IpAddress: 10.65.60.2");
				out.println("Pipeline: GripPipelineWithoutWPILibTape");
				out.println("Headless: true");
				out.println("Publishing: true");
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
		
		ArrayList<VisionThread> visionThreads = new ArrayList<VisionThread>();
		
		for (ArrayList<String> row : Preferences) {
			if (row.get(0).equalsIgnoreCase("Pipeline:")) {
				for (int i = 1; i < row.size(); i++) {
					System.out.println("Creating a thread for " + row.get(i));
					visionThreads.add(new VisionThread());
				}
				
				
			} else if (row.get(0).equalsIgnoreCase("IpAddress:")) {
				System.out.println("Setting IpAddress to " + row.get(1));
					VisionThread.setIpAddress(row.get(1));
				
			} else if (row.get(0).equalsIgnoreCase("Headless:")) {
				for (int i = 0; i < visionThreads.size(); i++) {
					System.out.println("Setting headless to " + row.get(i+1) + " for thread " + (i+1));
					visionThreads.get(i).setIsHeadless(Boolean.parseBoolean(row.get(i+1)));
				}
				
			} else if (row.get(0).equalsIgnoreCase("Publishing:")) {
				for (int i = 0; i < visionThreads.size(); i++) {
					System.out.println("Setting publishing to " + row.get(i+1) + " for thread " + (i+1));
					visionThreads.get(i).setIsPublishing(Boolean.parseBoolean(row.get(i+1)));
				}
				
			} else if (row.get(0).equalsIgnoreCase("DeviceIndex:")) {
				for (int i = 0; i < visionThreads.size(); i++) {
					System.out.println("Setting DeviceIndex to " + row.get(i+1) + " for thread " + (i+1));
					visionThreads.get(i).setDeviceIndex(Integer.parseInt(row.get(i+1)));
				}
					
			} else if (row.get(0).equalsIgnoreCase("FPS:")) {
				for (int i = 0; i < visionThreads.size(); i++) {
					System.out.println("Setting FPS to " + row.get(i+1) + " for thread " + (i+1));
					visionThreads.get(i).setFps(Integer.parseInt(row.get(i+1)));
				}
					
			} else if (row.get(0).equalsIgnoreCase("Width:")) {
				for (int i = 0; i < visionThreads.size(); i++) {
					System.out.println("Setting Width to " + row.get(i+1) + " for thread " + (i+1));
					visionThreads.get(i).setWidth(Integer.parseInt(row.get(i+1)));
				}
				
			} else if (row.get(0).equalsIgnoreCase("Height:")) {
				for (int i = 0; i < visionThreads.size(); i++) {
					System.out.println("Setting Height to " + row.get(i+1) + " for thread " + (i+1));
					visionThreads.get(i).setHeight(Integer.parseInt(row.get(i+1)));
				}
				
			}
		}
		System.out.println("Running vision tracking...");
		for (VisionThread thread : visionThreads) {
			Thread myThread = new Thread(thread);
			myThread.start();
		}
		
		
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
