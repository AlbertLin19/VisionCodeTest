import edu.wpi.first.wpilibj.networktables.NetworkTable;

/**
 * This class is a thread for publishing values to network tables.
 * @author Albert Lin
 *
 */
public class NetworkTablesThread implements Runnable {
	
	String ipAddress;
	boolean publishNetworkTable;
	
	public NetworkTablesThread() {
		ipAddress = "10.65.60.1";
		publishNetworkTable = true;
	}
	
	public NetworkTablesThread(String ipAddressIn) {
		ipAddress = ipAddressIn;
		publishNetworkTable = true;
	}
	
	public void setIpAddress(String ipAddressIn) {
		ipAddress = ipAddressIn;
	}
	
	public void setPublishNetworkTable(boolean publishNetworkTableIn) {
		publishNetworkTable = publishNetworkTableIn;
	}

	public void run() {
		NetworkTable.setClientMode();
		NetworkTable.setIPAddress(ipAddress);
		NetworkTable table = NetworkTable.getTable("datatable");
		while (publishNetworkTable) {
			//code for publishing the values to network tables
		}
	}

}
