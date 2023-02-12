package shooting;
import java.awt.Image;

import javax.swing.ImageIcon;

public class BossAttack {
	Image image = new ImageIcon("src/shooting_images/boss_attack.png").getImage();
	int x, y;
	int width = image.getWidth(null);
	int height = image.getHeight(null);
	int attack = 10;

	public BossAttack(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void fire() {
		this.y += 13;
	}
}
