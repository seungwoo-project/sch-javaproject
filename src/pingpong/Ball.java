package pingpong;

public class Ball {
	int x, y;
	int width = 32;
	int height = 32;
	int movex = 13;
	int movey = 13;

	public Ball(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void move() {
		this.x += movex;
		this.y += movey;
	}
}
