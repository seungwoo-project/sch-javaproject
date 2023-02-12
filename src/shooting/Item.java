package shooting;
import java.awt.Image;

import javax.swing.ImageIcon;

public class Item {
	Image image;
	int x, y;
	int width;
	int height;
	int select;

	public Item(int x, int y, int select) {
		this.x = x;
		this.y = y;
		this.select = select;
		if (select == 1)
			image = new ImageIcon("src/shooting_images/hpitem.png").getImage();
		else if (select == 2 || select == 3 || select == 4)
			image = new ImageIcon("src/shooting_images/poweritem.png").getImage();

		width = image.getWidth(null);
		height = image.getHeight(null);
	}

}
