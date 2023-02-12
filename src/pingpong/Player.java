package pingpong;
import java.awt.Image;

import javax.swing.ImageIcon;

public class Player {
	Image image;
	int x, y;
	int width;
	int height;
	int moveY = 10;

	public Player(int x, int y, int select) {
		this.x = x;
		this.y = y;
		image = new ImageIcon("src/pingpong_images/player" + select + ".png").getImage();
		width = image.getWidth(null);
		height = image.getHeight(null);
	}
	
	public void move() {
		this.y += moveY;
	}
}