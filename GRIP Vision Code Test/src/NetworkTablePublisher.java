import edu.wpi.first.wpilibj.networktables.NetworkTable;

/**
 * This class is for publishing values to network tables.
 * @author Albert Lin
 *
 */
public class NetworkTablePublisher {
	
	String ipAddress;
	NetworkTable table;
	
	public NetworkTablePublisher(String ipAddressIn) {
		ipAddress = ipAddressIn;
		NetworkTable.setClientMode();
		NetworkTable.setIPAddress(ipAddress);
		table = NetworkTable.getTable("offset");
		
	}
	
	public void setIpAddress(String ipAddressIn) {
		ipAddress = ipAddressIn;
		NetworkTable.setIPAddress(ipAddress);
	}
	
	public void publish(double xIn, double yIn) {
		table.putNumber("xOffset", xIn);
		table.putNumber("yOffset", yIn);
	}

}
