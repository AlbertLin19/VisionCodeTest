

public class ServoControl {
	
	public ServoControl() {

	}
	
	public static void testingGpio() throws InterruptedException {
		com.pi4j.wiringpi.Gpio.wiringPiSetupGpio();
		int n = 18;
		System.out.println("Config Servo PWM with pin number: " + n);
	    com.pi4j.wiringpi.Gpio.pinMode(n, com.pi4j.wiringpi.Gpio.PWM_OUTPUT);
	    com.pi4j.wiringpi.Gpio.pwmSetMode(com.pi4j.wiringpi.Gpio.PWM_MODE_MS);
	    com.pi4j.wiringpi.Gpio.pwmSetClock(192);
	    com.pi4j.wiringpi.Gpio.pwmSetRange(2000);

	    for(int i = 0; i < 5; i++){
	        System.out.println("Set Servo");
	        com.pi4j.wiringpi.Gpio.pwmWrite(n, 50);

	        Thread.sleep(1000);

	        System.out.println("Change servo state...");
	        com.pi4j.wiringpi.Gpio.pwmWrite(n, 250);

	        Thread.sleep(1000);

	    }
	}
	

}
