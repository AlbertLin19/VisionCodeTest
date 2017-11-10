

public class ServoControl {
	static int n = 18;
	public ServoControl() {

	}
	
	public static void testingGpio() throws InterruptedException {
		com.pi4j.wiringpi.Gpio.wiringPiSetupGpio();
		System.out.println("Config Servo PWM with pin number: " + n);
	    com.pi4j.wiringpi.Gpio.pinMode(n, com.pi4j.wiringpi.Gpio.PWM_OUTPUT);
	    com.pi4j.wiringpi.Gpio.pwmSetMode(com.pi4j.wiringpi.Gpio.PWM_MODE_MS);
	    com.pi4j.wiringpi.Gpio.pwmSetClock(192);
	    com.pi4j.wiringpi.Gpio.pwmSetRange(2000);
	        System.out.println("Set Servo");
	        com.pi4j.wiringpi.Gpio.pwmWrite(n, 140);

	        Thread.sleep(1000);

	        System.out.println("Change servo state...");
	        com.pi4j.wiringpi.Gpio.pwmWrite(n, 160);

	        Thread.sleep(1000);
	        
	        com.pi4j.wiringpi.Gpio.pwmWrite(n, 150);

	}
	
	public static void updateServo(double x, double widthIn) {
		double offset = widthIn/2-x;

		if (offset < -1*widthIn/10.0) {
			System.out.println("Setting to 160");
			com.pi4j.wiringpi.Gpio.pwmWrite(n, 160);
			try {
				Thread.sleep((long) (offset*-1/widthIn*2750));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Stopping Servo...");
			com.pi4j.wiringpi.Gpio.pwmWrite(n, 150);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (offset > widthIn/10.0) {
			System.out.println("Setting to 140");
			com.pi4j.wiringpi.Gpio.pwmWrite(n, 140);
			try {
				Thread.sleep((long) (offset/widthIn*2750));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Stopping Servo...");
			com.pi4j.wiringpi.Gpio.pwmWrite(n, 150);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			System.out.println("Setting to 150");
			com.pi4j.wiringpi.Gpio.pwmWrite(n, 150);
		}
		
	}
	

}
