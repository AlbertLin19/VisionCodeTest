

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
	        com.pi4j.wiringpi.Gpio.pwmWrite(n, 100);

	        Thread.sleep(1000);

	        System.out.println("Change servo state...");
	        com.pi4j.wiringpi.Gpio.pwmWrite(n, 200);

	        Thread.sleep(1000);
	        
	        com.pi4j.wiringpi.Gpio.pwmWrite(n, 150);

	}
	
	public static void updateServo(double x, double widthIn) {
		double offset = widthIn/2-x;

		if (offset < widthIn/10.0) {
			System.out.println("Setting to 170");
			com.pi4j.wiringpi.Gpio.pwmWrite(n, 170);
			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Stopping Servo...");
			com.pi4j.wiringpi.Gpio.pwmWrite(n, 150);
		} else if (offset > widthIn/10.0) {
			System.out.println("Setting to 130");
			com.pi4j.wiringpi.Gpio.pwmWrite(n, 130);
			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Stopping Servo...");
			com.pi4j.wiringpi.Gpio.pwmWrite(n, 150);
		} else {
			System.out.println("Setting to 150");
			com.pi4j.wiringpi.Gpio.pwmWrite(n, 150);
		}
		
		return;
	}
	

}
