package shooting;
import javax.swing.*;
import java.awt.*;

public class Boss {
	Image image = new ImageIcon("src/shooting_images/boss.png").getImage();
	int x, y;
	int width = image.getWidth(null);
	int height = image.getHeight(null);
	int hp = 2000;
	int movex = 4;
	int movey = 4;

	public Boss(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void move() {
		this.x += movex;
		this.y += movey;

	}
}
