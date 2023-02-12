package run;
import javax.swing.*;
import java.awt.*;

public class Obstacle {
	Image image = new ImageIcon("src/run_images/obstacle.png").getImage();
	int x, y;
	int width = image.getWidth(null);
	int height = image.getHeight(null);
	int attack = 5;
	int speed = 12;

	

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public Obstacle(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void produce() {
		this.y += speed;
	}
}
