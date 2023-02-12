package pingpong;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;

public class Game extends Thread {
	private int delay = 20;
	private long pretime;
	private int cnt;
	private int scoreLeft, scoreRight; // p1, p2 스코어 저장 변수

	private Player p1 = new Player(10, 200, 1);
	private Player p2 = new Player(840, 200, 2);

	private boolean w, s, up, down; // 움직임 변수
	private boolean p1win, p2win;	// p1 승리, p2 승리 조건 변수

	private ArrayList<Obstacle> obstacleList = new ArrayList<Obstacle>(); // 장애물 리스트

	private Ball ball = new Ball(PingPongGame.SCREEN_WIDTH / 2, PingPongGame.SCREEN_HEIGHT / 2);
	private Obstacle obstacle;

	// 쓰레드 런 메소드 호출
	@Override
	public void run() {

		reset();
		while (true) {
			while (!p1win && !p2win) {
				pretime = System.currentTimeMillis();
				if (System.currentTimeMillis() - pretime < delay) {
					try {
						Thread.sleep(delay - System.currentTimeMillis() + pretime);
						keyProcess();
						ballMoveProcess();
						obstacleProcess();
						cnt++;
						obstacleAppearProcess();
						if(scoreLeft==3)
							p1win=true;
						if(scoreRight==3)
							p2win=true;
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	// 재시작
	public void reset() {
		p2win = false;
		p1win = false;
		cnt = 0;
		scoreLeft = 0;
		scoreRight = 0;
		p1.x = 10;
		p1.y = PingPongGame.SCREEN_HEIGHT / 2 - p1.height / 2;
		p2.x = PingPongGame.SCREEN_WIDTH - p2.width -10;
		p2.y = PingPongGame.SCREEN_HEIGHT / 2 - p2.height / 2;
		if(ball.movex < 0)
			ball.x = PingPongGame.SCREEN_WIDTH / 2 +200;
		else
			ball.x = PingPongGame.SCREEN_WIDTH / 2 -200;
		ball.y = PingPongGame.SCREEN_HEIGHT / 2;

		obstacleList.clear();
	}
	// 플레이어 움직임
	private void keyProcess() {
		if (w && p1.y - p1.moveY > 0)
			p1.y -= p1.moveY;
		if (s && p1.y + p1.height + p1.moveY < PingPongGame.SCREEN_HEIGHT)
			p1.y += p1.moveY;
		if (up && p2.y - p2.moveY > 0)
			p2.y -= p2.moveY;
		if (down && p2.y + p2.height + p2.moveY < PingPongGame.SCREEN_HEIGHT)
			p2.y += p2.moveY;
	}

	// 공 움직임
	private void ballMoveProcess() {
		ball.move();
		if(ball.x+ball.width<0) {
			scoreRight++;
			ball.x = PingPongGame.SCREEN_WIDTH / 2 +200;
			ball.y = PingPongGame.SCREEN_HEIGHT / 2;
		}
		if(ball.x>PingPongGame.SCREEN_WIDTH) {
			scoreLeft++;
			ball.x = PingPongGame.SCREEN_WIDTH / 2 -200;
			ball.y = PingPongGame.SCREEN_HEIGHT / 2;
		}

		if (ball.y <= 0 || ball.y >= PingPongGame.SCREEN_HEIGHT - ball.height)
			ball.movey = ball.movey * -1;

		if ((ball.x <= p1.x+p1.width && ball.y >= p1.y - ball.height && ball.y+ball.height <= p1.y+p1.height+ball.height)||
				(ball.x+ball.width >= p2.x && ball.y >= p2.y-ball.height && ball.y+ball.height <= p2.y+p2.height+ball.height))
			ball.movex = ball.movex * -1;

	}

	// 장애물 생성
	private void obstacleAppearProcess() {
		if (cnt % 100 == 0) {
			obstacle = new Obstacle((int) (Math.random() * PingPongGame.SCREEN_WIDTH / 3) + PingPongGame.SCREEN_WIDTH / 3, 
					(int) (Math.random() * (PingPongGame.SCREEN_HEIGHT - 20)));
			obstacleList.add(obstacle);
		}
	}

	// 장애물 충돌
	private void obstacleProcess() {
		if (obstacle != null) {
			for (int i = 0; i < obstacleList.size(); i++) {
				obstacle = obstacleList.get(i);
				int ball_x = ball.x + ball.width / 2;
				int ball_y = ball.y + ball.height / 2;
				double ball_radius = Math.sqrt((int)Math.pow(ball_x-ball.x, 2)+(int)Math.pow(ball_y-ball.y, 2));
				if (Math.sqrt((int)Math.pow(ball_x-obstacle.x, 2)+(int)Math.pow(ball_y-obstacle.y, 2))<= ball_radius ||
						Math.sqrt((int)Math.pow(ball_x-obstacle.x, 2)+(int)Math.pow(ball_y-obstacle.y+(obstacle.r)/2, 2))<= ball_radius ||
						Math.sqrt((int)Math.pow(ball_x-obstacle.x+(obstacle.r)/2, 2)+(int)Math.pow(ball_y-obstacle.y, 2))<= ball_radius ||
						Math.sqrt((int)Math.pow(ball_x-obstacle.x+(obstacle.r)/2, 2)+(int)Math.pow(ball_y-obstacle.y+(obstacle.r)/2, 2))<= ball_radius) {

					ball.movex = ball.movex * -1;
					ball.movey = ball.movey * -1;
					obstacleList.remove(obstacle);
				}
			}
		}
	}

	public void gameDraw(Graphics g) {
		playerDraw(g);
		ballDraw(g);
		infoDraw(g);
		obstacleDraw(g);
	}

	// 정보 글씨
	public void infoDraw(Graphics g) {
		g.setColor(Color.red);
		g.setFont(new Font("둥근모꼴", Font.BOLD, 40));
		g.drawString(scoreLeft+"", PingPongGame.SCREEN_WIDTH / 2 - 80, 60);
		g.drawString(scoreRight+"", PingPongGame.SCREEN_WIDTH / 2 + 60, 60);

		if (p1win) {
			g.setColor(Color.WHITE);
			g.setFont(new Font("둥근모꼴", Font.BOLD, 40));
			g.drawString("1P 승리!", 120, 200);
			g.setFont(new Font("둥근모꼴", Font.BOLD, 24));
			g.drawString("게임을 끝내려면 esc키를 누르세요", 13, 240);
			g.drawString("다시 시작하려면 R키를 누르세요", 28, 270);
		}
		if (p2win) {
			g.setColor(Color.WHITE);
			g.setFont(new Font("둥근모꼴", Font.BOLD, 40));
			g.drawString("2P 승리!", 550, 200);
			g.setFont(new Font("둥근모꼴", Font.BOLD, 24));
			g.drawString("게임을 끝내려면 esc키를 누르세요", 433, 240);
			g.drawString("다시 시작하려면 R키를 누르세요", 448, 270);
		}
	}

	// 플레이어 그리기
	public void playerDraw(Graphics g) {
		g.drawImage(p1.image, p1.x, p1.y, null);
		g.drawImage(p2.image, p2.x, p2.y, null);
	}

	// 공 그리기
	public void ballDraw(Graphics g) {
		g.setColor(new Color((int)(Math.random()*255),(int)(Math.random()*255),(int)(Math.random()*255)));
		g.fillOval(ball.x, ball.y, ball.width, ball.height);
	}

	// 장애물 그리기
	public void obstacleDraw(Graphics g) {
		if (obstacle != null) {
			for (int i = 0; i < obstacleList.size(); i++) {
				g.setColor(Color.green);
				obstacle = obstacleList.get(i);
				g.fillOval(obstacle.x, obstacle.y, obstacle.r, obstacle.r);
			}
		}
	}
	// 세터 함수
	public boolean isOver() {
		return p1win;
	}

	public boolean isWin() {
		return p2win;
	}

	public void setUp(boolean up) {
		this.up = up;
	}

	public void setDown(boolean down) {
		this.down = down;
	}

	public void setW(boolean w) {
		this.w = w;
	}

	public void setS(boolean s) {
		this.s = s;
	}
}