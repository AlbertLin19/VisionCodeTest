import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class StreamPanel extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BufferedImage image;
	int count = 0;
	private int x;
	private int y;
	
	public StreamPanel() {
		super();
		x=0;
		y=0;
	}
	
	public void setStream(BufferedImage imgIn) {
		image = imgIn;
	}
	
	public void setStream(BufferedImage imgIn, int xIn, int yIn) {
		image = imgIn;
		x=xIn;
		y=yIn;
	}
	
	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		if (this.image==null) {
			System.out.println("The image is null!");
			return;
		}
		g.drawImage(this.image, 0, 0, this.image.getWidth(), this.image.getHeight(), null);
		g.setColor(Color.CYAN);
		g.fillRect(x-200, y-1, 400, 3);
		g.fillRect(x-1, y-200, 3, 400);
	}

}
